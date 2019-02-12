package com.andrew.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebPageView extends AppCompatActivity {

    WebView webPageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page_view);
        webPageView = findViewById(R.id.web_view);

        // get the url and begin showing to the user
        String link = getIntent().getStringExtra("url");
        setWebClient(webPageView, link);
    }

    private void setWebClient(WebView webPageView, String link)
    {
        // allows the user change page in the web view
        webPageView.setWebViewClient(new BrowserSettings());
        loadPage(link);
    }

    private void loadPage(String link)
    {
        // allows the pages run javascript and show images, normal scrolling and loads thepage
        webPageView.getSettings().setLoadsImagesAutomatically(true);
        webPageView.getSettings().setJavaScriptEnabled(true);
        webPageView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webPageView.loadUrl(link);
    }

    private class BrowserSettings extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }
    }
}
