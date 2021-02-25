package com.spitagram.instagramApi.BrowerRequest;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.spitagram.instagramApi.InstagramApp;

public class Browser extends WebView {
    private String content = "";
    private static String JAVASCRIPT_FUNCTION_CALL = "getAllTextInColumn()";
    private static String JAVASCRIPT_SCRIPT_TO_EXTRACT_TEXT = "function getAllTextInColumn(){ " +
            "return (document.getElementsByTagName('pre')[0].innerHTML); };";
    public Browser(Context context) {
        super(context);
        init();
    }
    private void init(){
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.addJavascriptInterface(new IJavascriptHandler(this.getContext()), "Android");
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:" + JAVASCRIPT_SCRIPT_TO_EXTRACT_TEXT);
                view.loadUrl("javascript:window.Android.processContent(" + JAVASCRIPT_FUNCTION_CALL + ");");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public String getContent() {
        return content;
    }

    private class IJavascriptHandler {
        Context mContext;

        public IJavascriptHandler(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void processContent(String aContent) {
                content = aContent;
                InstagramApp.lock = false;
                //content.notify();
        }
    }
}
