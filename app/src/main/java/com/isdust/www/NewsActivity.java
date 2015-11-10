package com.isdust.www;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.isdust.www.baseactivity.BaseMainActivity_new;

/**
 * Created by Administrator on 2015/10/17.
 */
public class NewsActivity extends BaseMainActivity_new {
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.helper_news, "校园资讯",6);
        mWebView=(WebView)findViewById(R.id.webView_news);
        WebSettings mwebSettings = mWebView.getSettings();
        mwebSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        mwebSettings.setAllowFileAccess(true);
        //设置支持缩放
        mwebSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页

        mWebView.loadUrl("http://bbs.isdust.com");
        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });    }


}