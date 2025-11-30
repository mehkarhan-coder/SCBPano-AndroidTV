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
    private String lastLoadedDate = ""; // Son yüklenen tarih
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
        // Cache'i optimize et: Network öncelikli, eski cache'i kullanma
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // Sayfa her yüklendiğinde güncel içeriği almak için
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
                
                // Tarih değişikliğini kontrol et ve gerekirse yenile
                checkAndRefreshIfDateChanged(view);
                
                // Sayfa yüklendiğinde 16:10 oranında görüntüle ve çözünürlüğü büyüt
                // CSS ile sayfanın genişliğini ve yüksekliğini 16:10 oranında ayarla
                String scaleValue = String.format("%.2f", DISPLAY_SCALE);
                
                String resolutionScript = 
                    "(function() {" +
                    "  function applyResolution() {" +
                    "    // Mevcut style'ı kaldır" +
                    "    var oldStyle = document.getElementById('tv-resolution-style');" +
                    "    if (oldStyle) oldStyle.remove();" +
                    "    " +
                    "    // TV ekran boyutunu al" +
                    "    var screenWidth = window.innerWidth || 1920;" +
                    "    var screenHeight = window.innerHeight || 1080;" +
                    "    " +
                    "    // 16:10 oranını hesapla (daha geniş görüntü)" +
                    "    var aspectRatio = 16 / 10; // 1.6" +
                    "    var targetWidth = screenWidth * " + scaleValue + ";" +
                    "    var targetHeight = targetWidth / aspectRatio; // 16:10 oranı" +
                    "    " +
                    "    // Scale hesapla (ekrana sığdırmak için)" +
                    "    var scaleX = screenWidth / targetWidth;" +
                    "    var scaleY = screenHeight / targetHeight;" +
                    "    var finalScale = Math.min(scaleX, scaleY) * " + scaleValue + ";" +
                    "    " +
                    "    // Yeni style oluştur - !important ile zorla uygula" +
                    "    var style = document.createElement('style');" +
                    "    style.id = 'tv-resolution-style';" +
                    "    style.innerHTML = 'html { " +
                    "      width: ' + targetWidth + 'px !important; " +
                    "      height: ' + targetHeight + 'px !important; " +
                    "      transform: scale(' + finalScale + ') !important; " +
                    "      transform-origin: top left !important; " +
                    "      margin: 0 !important; " +
                    "      padding: 0 !important; " +
                    "      overflow: hidden !important; " +
                    "      aspect-ratio: 16 / 10 !important; " +
                    "    } " +
                    "    body { " +
                    "      width: ' + targetWidth + 'px !important; " +
                    "      height: ' + targetHeight + 'px !important; " +
                    "      transform: scale(' + finalScale + ') !important; " +
                    "      transform-origin: top left !important; " +
                    "      margin: 0 !important; " +
                    "      padding: 0 !important; " +
                    "      overflow: hidden !important; " +
                    "      aspect-ratio: 16 / 10 !important; " +
                    "      box-sizing: border-box !important; " +
                    "    }';" +
                    "    document.head.appendChild(style);" +
                    "    " +
                    "    // HTML elementine zorla uygula" +
                    "    if (document.documentElement) {" +
                    "      document.documentElement.style.setProperty('width', targetWidth + 'px', 'important');" +
                    "      document.documentElement.style.setProperty('height', targetHeight + 'px', 'important');" +
                    "      document.documentElement.style.setProperty('transform', 'scale(' + finalScale + ')', 'important');" +
                    "      document.documentElement.style.setProperty('transform-origin', 'top left', 'important');" +
                    "      document.documentElement.style.setProperty('aspect-ratio', '16 / 10', 'important');" +
                    "    }" +
                    "    " +
                    "    // Body'ye zorla uygula" +
                    "    if (document.body) {" +
                    "      document.body.style.setProperty('width', targetWidth + 'px', 'important');" +
                    "      document.body.style.setProperty('height', targetHeight + 'px', 'important');" +
                    "      document.body.style.setProperty('transform', 'scale(' + finalScale + ')', 'important');" +
                    "      document.body.style.setProperty('transform-origin', 'top left', 'important');" +
                    "      document.body.style.setProperty('aspect-ratio', '16 / 10', 'important');" +
                    "      document.body.style.setProperty('margin', '0', 'important');" +
                    "      document.body.style.setProperty('padding', '0', 'important');" +
                    "    }" +
                    "  }" +
                    "  " +
                    "  // Hemen uygula" +
                    "  applyResolution();" +
                    "  " +
                    "  // Sayfa tam yüklendikten sonra tekrar uygula" +
                    "  if (document.readyState === 'complete') {" +
                    "    setTimeout(applyResolution, 100);" +
                    "    setTimeout(applyResolution, 500);" +
                    "    setTimeout(applyResolution, 1000);" +
                    "  } else {" +
                    "    window.addEventListener('load', function() {" +
                    "      setTimeout(applyResolution, 100);" +
                    "      setTimeout(applyResolution, 500);" +
                    "      setTimeout(applyResolution, 1000);" +
                    "    });" +
                    "  }" +
                    "})();";
                
                // Önce sayfanın tam yüklenmesini bekle ve birkaç kez dene
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.evaluateJavascript(resolutionScript, null);
                    }
                }, 300);
                
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.evaluateJavascript(resolutionScript, null);
                    }
                }, 1000);
                
                // Viewport meta tag ekle veya güncelle (16:10 oranı için)
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
            // Uygulama her açıldığında sayfayı yenile (güncel içerik için)
            refreshPageIfNeeded();
        }
    }
    
    private void refreshPageIfNeeded() {
        // Uygulama her açıldığında sayfayı yenile
        // Cache'i bypass etmek için timestamp ekle
        if (webView != null && isNetworkAvailable()) {
            String urlWithTimestamp = WEB_URL + "?t=" + System.currentTimeMillis();
            webView.loadUrl(urlWithTimestamp);
        }
    }
    
    private void checkAndRefreshIfDateChanged(WebView view) {
        // JavaScript ile sayfadaki tarihi kontrol et
        String dateCheckScript = 
            "(function() {" +
            "  var currentDate = new Date().toLocaleDateString('tr-TR');" +
            "  return currentDate;" +
            "})();";
        
        view.evaluateJavascript(dateCheckScript, new android.webkit.ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // JavaScript'ten gelen tarih string'i (tırnak işaretleri ile)
                String currentDate = value != null ? value.replace("\"", "") : "";
                
                // Tarih değiştiyse sayfayı yenile
                if (!lastLoadedDate.isEmpty() && !lastLoadedDate.equals(currentDate)) {
                    if (webView != null && isNetworkAvailable()) {
                        // Cache'i bypass et
                        String urlWithTimestamp = WEB_URL + "?refresh=" + System.currentTimeMillis();
                        webView.loadUrl(urlWithTimestamp);
                    }
                }
                
                // Son yüklenen tarihi güncelle
                lastLoadedDate = currentDate;
            }
        });
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

