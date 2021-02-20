package com.example.shuactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.web_view);
        url = getIntent().getStringExtra("url");

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.setWebViewClient(new WebViewClient());

        webSettings = webView.getSettings();
        //让webView支持JavaScript
        webSettings.setJavaScriptEnabled(true);
        //在Android5.0以下，默认采用的MIXED_CONTENT_ALWAYS_ALLOW模式，即总是允许Webview同时加载HTTPS和HTTP:而从Android5.0开始，默认用MIXED_CONTENT_ALWAYS_ALLOW模式，即总是不允许webview同时加载HTTPS和HTTP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置自动适应屏幕,两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); //缩放致屏幕大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true，是下面那个的前提
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件，若为false，则该webview不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中的缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8"); //设置编码格式

        webView.loadUrl(url);

    }
}