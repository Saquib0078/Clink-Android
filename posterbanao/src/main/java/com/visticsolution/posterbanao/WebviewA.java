package com.visticsolution.posterbanao;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.visticsolution.posterbanao.classes.Functions;

public class WebviewA extends AppCompatActivity implements View.OnClickListener{



    ProgressBar progressBar;
    WebView webView;
    String url = "";
    String title;
    TextView titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        findViewById(R.id.goBack).setOnClickListener(this::onClick);

        titleTxt = findViewById(R.id.title_txt);
        titleTxt.setText(title);

        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress_bar);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 80) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                if (url.equalsIgnoreCase("closePopup")) {
                    WebviewA.super.onBackPressed();
                }
                return false;
            }
        });

        if (title.equals(getString(R.string.privacy_policy))){
            webView.loadData(Functions.getSharedPreference(this).getString("privacypolicy", getString(R.string.privacy_policy)), "text/html", "UTF-8");
        }else if (title.equals(getString(R.string.terms_service))){
            webView.loadData(Functions.getSharedPreference(this).getString("terms_and_condition", getString(R.string.terms_service)), "text/html", "UTF-8");
        }else{
            webView.loadUrl(url);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goBack) {
            WebviewA.super.onBackPressed();
        }
    }
}
