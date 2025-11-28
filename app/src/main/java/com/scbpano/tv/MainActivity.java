package com.scbpano.tv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final String WEB_URL = "https://samsunkml.com";
    private Handler handler = new Handler();
    private boolean doubleBackToExitPressedOnce = false;
    private float currentZoom = 0.75f; // Başlangıç zoom seviyesi (%75)

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Tam ekran modu
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        // Ekranı açık tut
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        // Immersive mod (Android 4.4+)
        hideSystemUI();
        
        setContentView(R.layout.activity_main);
        
        webView = findViewById(R.id.webView);
        
        // WebView ayarları
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        // setAppCacheEnabled removed in API 33 - cache is now managed automatically
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        // Zoom özelliklerini etkinleştir
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false); // UI kontrollerini gizle (remote ile kontrol edeceğiz)
        webSettings.setSupportZoom(true);
        // İlk zoom seviyesini ayarla (55 inç TV için daha küçük görüntü - %75)
        webView.setInitialScale(75); // %75 zoom (daha küçük görüntü)
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        
        // User Agent ayarla (mobil algılamasını engelle)
        String userAgent = webSettings.getUserAgentString();
        webSettings.setUserAgentString(userAgent.replace("Mobile", ""));
        
        // WebView Client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
            
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (!isNetworkAvailable()) {
                    showError("İnternet bağlantısı yok. Lütfen bağlantınızı kontrol edin.");
                }
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Sayfa yüklendiğinde immersive modu tekrar etkinleştir
                hideSystemUI();
                // Sayfa yüklendiğinde otomatik olarak küçült (55 inç TV için optimize)
                // CSS transform ile sayfayı %75'e küçült
                String zoomScript = 
                    "(function() {" +
                    "  var style = document.createElement('style');" +
                    "  style.innerHTML = 'body { transform: scale(0.75); transform-origin: top left; width: 133.33%; height: 133.33%; }';" +
                    "  document.head.appendChild(style);" +
                    "})();";
                view.evaluateJavascript(zoomScript, null);
                
                // Viewport meta tag ekle veya güncelle
                String viewportScript = 
                    "var meta = document.querySelector('meta[name=viewport]');" +
                    "if (!meta) {" +
                    "  meta = document.createElement('meta');" +
                    "  meta.name = 'viewport';" +
                    "  document.getElementsByTagName('head')[0].appendChild(meta);" +
                    "}" +
                    "meta.content = 'width=1333px, initial-scale=0.75, maximum-scale=2.0, user-scalable=yes';";
                view.evaluateJavascript(viewportScript, null);
            }
        });
        
        // WebChrome Client
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        
        // İnternet kontrolü ve yükleme
        if (isNetworkAvailable()) {
            webView.loadUrl(WEB_URL);
        } else {
            showError("İnternet bağlantısı yok. Lütfen bağlantınızı kontrol edin.");
        }
    }
    
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Zoom kontrolleri (TV remote için - OK tuşu + yukarı/aşağı)
        // OK tuşu (DPAD_CENTER) + Yukarı tuşu = Zoom In
        // OK tuşu (DPAD_CENTER) + Aşağı tuşu = Zoom Out
        
        // Geri tuşu kontrolü
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        
        // Çift geri tuşu ile çıkış
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (doubleBackToExitPressedOnce) {
                finish();
                return true;
            }
            
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Çıkmak için tekrar geri tuşuna basın", Toast.LENGTH_SHORT).show();
            
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
            
            return true;
        }
        
        // Yön tuşları ile zoom kontrolü (OK tuşu basılı değilse normal navigasyon)
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            // Yukarı tuşu - Zoom In (yakınlaştır)
            currentZoom = Math.min(currentZoom + 0.05f, 1.5f); // Maksimum %150
            applyZoom();
            return true;
        }
        
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            // Aşağı tuşu - Zoom Out (uzaklaştır)
            currentZoom = Math.max(currentZoom - 0.05f, 0.5f); // Minimum %50
            applyZoom();
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    private void applyZoom() {
        // CSS transform ile zoom uygula
        String zoomScript = String.format(
            "(function() {" +
            "  var style = document.getElementById('tv-zoom-style');" +
            "  if (!style) {" +
            "    style = document.createElement('style');" +
            "    style.id = 'tv-zoom-style';" +
            "    document.head.appendChild(style);" +
            "  }" +
            "  var scale = %.2f;" +
            "  style.innerHTML = 'body { transform: scale(' + scale + '); transform-origin: top left; width: ' + (100/scale) + '%%; height: ' + (100/scale) + '%%; }';" +
            "})();",
            currentZoom
        );
        webView.evaluateJavascript(zoomScript, null);
        Toast.makeText(this, String.format("Zoom: %.0f%%", currentZoom * 100), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        if (webView != null) {
            webView.onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }
    
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}

