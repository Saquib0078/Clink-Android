package com.nirmiteepublic.clink.ui.activity.pages.broadcast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;

public class WebViewUrls extends AppCompatActivity {
    private WebView webView;
    private String broadcastUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_urls);

        webView = findViewById(R.id.webview);

        Intent intent = getIntent();
        if (intent != null) {
            broadcastUrl = intent.getStringExtra("broadcastUrl");
        }

        if (broadcastUrl != null && !broadcastUrl.isEmpty()) {
            webViewSetup();
            PegaProgress.showProgressBar(this);
        } else {
            finish();  // Finish activity if no valid URL
        }
    }

    private void webViewSetup() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);

//        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(broadcastUrl);
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            PegaProgress.hideProgressBar();
            finish();  // Finish the activity after the page is loaded
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri url = request.getUrl();
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
                return true;
            }
            return false;
        }
    }
}