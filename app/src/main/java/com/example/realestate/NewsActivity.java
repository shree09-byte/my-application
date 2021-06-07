package com.example.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsActivity extends AppCompatActivity {
    private WebView webView1;
    private WebView webView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        webView1 = (WebView) findViewById(R.id.webView1);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new WebViewClient());
        webView1.loadUrl("https://housing.com/news/home-loans-guide-claiming-tax-benefits/");
//        webView2 = (WebView) findViewById(R.id.webView2);
//        webView2.getSettings().setJavaScriptEnabled(true);
//        webView2.setWebViewClient(new WebViewClient());
//        webView2.loadUrl("https://housing.com/news/impact-of-coronavirus-on-indian-real-estate/");

    }
}