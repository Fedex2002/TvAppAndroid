package com.example.appdeejemplo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private WebView webView;
    private LinearLayout menuLayout;
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

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        menuLayout = findViewById(R.id.menuLayout);

        ImageButton canal1Button = findViewById(R.id.canal1Button);
        ImageButton canal2Button = findViewById(R.id.canal2Button);
        ImageButton canal3Button = findViewById(R.id.canal3Button);


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
                        "height: 100vh !important; " +  // Forzar el tamaño máximo a 100% de la altura visible
                        "width: 100vw !important; " +
                        "max-height: 100vh !important; " +  // No permitir que se desborde verticalmente
                        "overflow: hidden !important; " +  // Ocultar cualquier desbordamiento
                        "position: absolute !important; " +
                        "top: 0 !important; " +
                        "left: 0 !important; }";
                String javascriptCSS = "var style = document.createElement('style'); " +
                        "style.innerHTML = '" + css + "'; " +
                        "document.head.appendChild(style);";
                webView.evaluateJavascript(javascriptCSS, null);

                // Usar la API de JW Player para ajustar el tamaño si está disponible
                String javascriptJWPlayer = "if (typeof jwplayer !== 'undefined') { " +
                        "jwplayer().resize(window.innerWidth, window.innerHeight); " +
                        "jwplayer().on('ready', function() { " +
                        "jwplayer().resize(window.innerWidth, window.innerHeight); }); " +
                        "}";
                webView.evaluateJavascript(javascriptJWPlayer, null);

                // Ajustar el viewport para evitar zoom fuera de proporción
                String viewportJS = "var meta = document.createElement('meta'); " +
                        "meta.name = 'viewport'; " +
                        "meta.content = 'width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'; " +
                        "document.getElementsByTagName('head')[0].appendChild(meta);";
                webView.evaluateJavascript(viewportJS, null);
            }
        });

        // WebChromeClient para manejar pantalla completa
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // Aquí puedes manejar la pantalla completa si lo deseas
            }

            @Override
            public void onHideCustomView() {
                // Restaurar el menú al salir del modo de pantalla completa
                setContentView(R.layout.activity_main);
                webView = findViewById(R.id.webView);
                if (currentUrl != null) {
                    webView.loadUrl(currentUrl); // Recargar la URL actual
                }
            }
        });

        // Configurar los botones para cargar los canales
        canal1Button.setOnClickListener(v -> loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWw0X1VSVQ=="));
        canal2Button.setOnClickListener(v -> loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWwxMlVSVQ=="));
        canal3Button.setOnClickListener(v -> loadChannel("https://television-libre.online/html/fl/?get=Q2FuYWwxMF9VUlU="));

        // Si no hay URL guardada, cargar el primer canal por defecto
        if (currentUrl == null) {
            showMenu();
        } else {
            loadChannel(currentUrl);
        }
    }

    private void loadChannel(String url) {
        currentUrl = url; // Guardar la URL actual
        webView.loadUrl(url);

        // Ocultar el menú y mostrar solo el reproductor
        menuLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        // Habilitar pantalla completa
        webView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        isInWebView = true; // Marcar que estamos en el reproductor
    }

    private void showMenu() {
        // Mostrar el menú y ocultar el reproductor (WebView)
        menuLayout.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        isInWebView = false; // Marcar que estamos en el menú
    }

    // Manejar el botón de "atrás"
    @Override
    public void onBackPressed() {
        if (isInWebView) {
            showMenu(); // Si estamos en el reproductor, volver al menú
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
