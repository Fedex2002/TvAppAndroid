// MainActivity.java
package com.example.appdeejemplo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    private WebView webView;
    private String currentUrl;
    private boolean isInWebView = false;
    private ViewPager2 viewPager;
    private ChannelPagerAdapter adapter;
    private int numPages = 2;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentUrl = savedInstanceState.getString("currentUrl");
        }

        initializeViewPager();

        if (currentUrl != null) {
            loadChannel(currentUrl);
        }
    }

    private void initializeViewPager() {
        viewPager = findViewById(R.id.viewPager);
        adapter = new ChannelPagerAdapter(this, numPages);
        viewPager.setAdapter(adapter);

        findViewById(R.id.prevPageButton).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        findViewById(R.id.nextPageButton).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < numPages - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        });
    }

    public void loadChannel(String url) {
        currentUrl = url;
        setContentView(R.layout.activity_player);
        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        String newUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
        webView.getSettings().setUserAgentString(newUserAgent);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
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


                String clickScript = "function simulateClick(x, y) { " +
                        "var event = new MouseEvent('click', { " +
                        "clientX: x, " +
                        "clientY: y, " +
                        "bubbles: true, " +
                        "cancelable: true " +
                        "}); " +
                        "document.elementFromPoint(x, y).dispatchEvent(event); " +
                        "} " +
                        "var x = window.innerWidth / 2; " +
                        "var y = window.innerHeight / 2; " +
                        "for (let i = 0; i < 10; i++) { " +  // Aumentar a 10 clics
                        "setTimeout(() => simulateClick(x, y), i * 200); " +  // Reducir el tiempo a 200ms
                        "}";
                webView.evaluateJavascript(clickScript, null);

            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
        isInWebView = true;
    }

    @Override
    public void onBackPressed() {
        if (isInWebView) {
            showMenu();
            isInWebView = false;
        } else {
            super.onBackPressed();
        }
    }

    private void showMenu() {
        setContentView(R.layout.activity_main);
        initializeViewPager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentUrl", currentUrl);
    }
}
