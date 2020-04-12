package com.demo.webviewdemo;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.demo.demolib.controller.ApiController;

public class WebviewActivity extends AppCompatActivity {

    private final static String URL="https://www.baidu.com";
    String url="http://192.168.9.2:82/PayWithCreditCard/$s/$s";
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // 1、获取webview控件
        webView = findViewById(R.id.web);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);
        webView.loadUrl(ApiController.getPayWithCreditCardUrl("F2QW1910019"));

        // 2、获得设置
        WebSettings webSettings=webView.getSettings();
        // 3、设置JavaScript打开
        webSettings.setJavaScriptEnabled(true);

        // 4、设置webviewclient 重写相应方法修改设置自己想要的功能
        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        // 5、设置webchromeclient 更简易的控制web
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("title:"+title);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 6、移除webview 要先将webview从父布局里移除再destroy
        ViewGroup parent=findViewById(R.id.parent);
        parent.removeView(webView);
        webView.destroy();
    }
}
