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
    // 55 inç TV için görünen çözünürlük ayarı (0.7 = %70, 0.8 = %80, 0.75 = %75)
    private static final float DISPLAY_SCALE = 0.70f; // %70 scale (daha küçük görüntü)

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
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false); // CSS ile zoom yapacağız
        // İlk zoom seviyesini ayarla (CSS ile override edilecek)
        webView.setInitialScale((int)(DISPLAY_SCALE * 100));
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
                
                // Sayfa yüklendiğinde 16:9 oranında görüntüle (55 inç TV için optimize)
                // CSS ile sayfanın genişliğini ve yüksekliğini 16:9 oranında ayarla
                String scaleValue = String.format("%.2f", DISPLAY_SCALE);
                
                String resolutionScript = 
                    "(function() {" +
                    "  // Mevcut style'ı kaldır" +
                    "  var oldStyle = document.getElementById('tv-resolution-style');" +
                    "  if (oldStyle) oldStyle.remove();" +
                    "  " +
                    "  // TV ekran boyutunu al (varsayılan 1920x1080 Full HD)" +
                    "  var screenWidth = window.innerWidth || 1920;" +
                    "  var screenHeight = window.innerHeight || 1080;" +
                    "  " +
                    "  // 16:9 oranını hesapla" +
                    "  var targetWidth = screenWidth * " + scaleValue + ";" +
                    "  var targetHeight = targetWidth * (9 / 16); // 16:9 oranı" +
                    "  " +
                    "  // Scale hesapla (ekrana sığdırmak için)" +
                    "  var scaleX = screenWidth / targetWidth;" +
                    "  var scaleY = screenHeight / targetHeight;" +
                    "  var finalScale = Math.min(scaleX, scaleY) * " + scaleValue + ";" +
                    "  " +
                    "  // Yeni style oluştur" +
                    "  var style = document.createElement('style');" +
                    "  style.id = 'tv-resolution-style';" +
                    "  style.innerHTML = 'html, body { " +
                    "    transform: scale(' + finalScale + '); " +
                    "    transform-origin: top left; " +
                    "    width: ' + targetWidth + 'px; " +
                    "    height: ' + targetHeight + 'px; " +
                    "    margin: 0; " +
                    "    padding: 0; " +
                    "    overflow: hidden; " +
                    "    aspect-ratio: 16 / 9; " +
                    "  }';" +
                    "  document.head.appendChild(style);" +
                    "  " +
                    "  // Body'ye de uygula" +
                    "  if (document.body) {" +
                    "    document.body.style.transform = 'scale(' + finalScale + ')';" +
                    "    document.body.style.transformOrigin = 'top left';" +
                    "    document.body.style.width = targetWidth + 'px';" +
                    "    document.body.style.height = targetHeight + 'px';" +
                    "    document.body.style.aspectRatio = '16 / 9';" +
                    "  }" +
                    "  " +
                    "  // HTML elementine de uygula" +
                    "  if (document.documentElement) {" +
                    "    document.documentElement.style.width = targetWidth + 'px';" +
                    "    document.documentElement.style.height = targetHeight + 'px';" +
                    "    document.documentElement.style.aspectRatio = '16 / 9';" +
                    "  }" +
                    "})();";
                
                // Önce sayfanın tam yüklenmesini bekle
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.evaluateJavascript(resolutionScript, null);
                    }
                }, 500);
                
                // Viewport meta tag ekle veya güncelle (16:9 oranı için)
                String viewportScript = 
                    "var meta = document.querySelector('meta[name=viewport]');" +
                    "if (!meta) {" +
                    "  meta = document.createElement('meta');" +
                    "  meta.name = 'viewport';" +
                    "  document.getElementsByTagName('head')[0].appendChild(meta);" +
                    "}" +
                    "var screenWidth = window.innerWidth || 1920;" +
                    "var targetWidth = screenWidth * " + scaleValue + ";" +
                    "meta.content = 'width=' + targetWidth + 'px, initial-scale=1.0, maximum-scale=1.0, user-scalable=no';";
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
        
        return super.onKeyDown(keyCode, event);
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

