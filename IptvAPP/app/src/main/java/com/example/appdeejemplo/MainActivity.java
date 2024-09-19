// MainActivity.java
package com.example.appdeejemplo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    private WebView webView;
    private String currentUrl;
    private boolean isInWebView = false; // Bandera para saber si estamos en el reproductor

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restaurar la URL actual si se rota la pantalla
        if (savedInstanceState != null) {
            currentUrl = savedInstanceState.getString("currentUrl");
        }

        showMenu(); // Mostrar el menú al iniciar

        // Si hay una URL guardada, cargarla automáticamente
        if (currentUrl != null) {
            loadChannel(currentUrl);
        }
    }

    private void showMenu() {
        setContentView(R.layout.activity_main); // Cambia al layout del menú
        ImageButton canal1Button = findViewById(R.id.canal1Button);
        ImageButton canal2Button = findViewById(R.id.canal2Button);
        ImageButton canal3Button = findViewById(R.id.canal3Button);

        // Configurar los botones para cargar los canales
        canal1Button.setOnClickListener(v -> loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWw0X1VSVQ=="));
        canal2Button.setOnClickListener(v -> loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWwxMlVSVQ=="));
        canal3Button.setOnClickListener(v -> loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWwxMF9VUlU="));
    }

    private void loadChannel(String url) {
        currentUrl = url; // Guardar la URL actual
        setContentView(R.layout.activity_player); // Cambia al layout del reproductor
        webView = findViewById(R.id.webView);

        // Configuraciones del WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        String newUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
        webView.getSettings().setUserAgentString(newUserAgent);

        // WebViewClient para manejar navegación dentro del WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Inyectar CSS para ajustar el tamaño del contenido y evitar que se desborde
                String css = "iframe, .jwplayer, .jwplayer video { " +
                        "height: 100vh !important; " +
                        "width: 100vw !important; " +
                        "max-height: 100vh !important; " +
                        "overflow: hidden !important; " +
                        "position: absolute !important; " +
                        "top: 0 !important; " +
                        "left: 0 !important; }";
                String javascriptCSS = "var style = document.createElement('style'); " +
                        "style.innerHTML = '" + css + "'; " +
                        "document.head.appendChild(style);";
                webView.evaluateJavascript(javascriptCSS, null);
            }
        });

        // WebChromeClient para manejar pantalla completa
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(url);
        isInWebView = true; // Marcar que estamos en el reproductor
    }

    @Override
    public void onBackPressed() {
        if (isInWebView) {
            showMenu(); // Si estamos en el reproductor, volver al menú
            isInWebView = false;
        } else {
            super.onBackPressed(); // Si estamos en el menú, cerrar la app
        }
    }

    // Guardar el estado actual de la actividad
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentUrl", currentUrl);
    }
}
