package com.socialconnect.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    
    private WebView webView;
    private LinearLayout noInternetLayout;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private Button retryButton;
    
    private static final String WEBSITE_URL = "https://socialconnect.wuaze.com";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        webView = findViewById(R.id.webView);
        noInternetLayout = findViewById(R.id.noInternetLayout);
        progressBar = findViewById(R.id.progressBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        retryButton = findViewById(R.id.retryButton);
        
        setupWebView();
        setupSwipeRefresh();
        setupRetryButton();
        loadWebsite();
    }
    
    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showNoInternet();
            }
        });
        
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) progressBar.setVisibility(View.GONE);
            }
        });
    }
    
    private void setupSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.primary);
        swipeRefresh.setOnRefreshListener(this::loadWebsite);
    }
    
    private void setupRetryButton() {
        retryButton.setOnClickListener(v -> loadWebsite());
    }
    
    private void loadWebsite() {
        if (isNetworkAvailable()) {
            showWebView();
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl(WEBSITE_URL);
        } else {
            showNoInternet();
        }
    }
    
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    
    private void showNoInternet() {
        webView.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
    
    private void showWebView() {
        noInternetLayout.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}