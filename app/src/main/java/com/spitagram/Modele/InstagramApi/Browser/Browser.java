package com.spitagram.Modele.InstagramApi.Browser;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.spitagram.Modele.InstagramApi.InstagramApp;
import com.spitagram.Modele.InstagramApi.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class Browser extends WebView {
    private String content = "";
    private Activity activity;
    private static boolean received = false;
    private static final String TAG = "browser";
    private WebViewClient webFollow = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:" + Script.FOLLOW);
            //setWebViewClient(defaultWebView);
        }
    };

    private WebViewClient webUnfollow = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:" + Script.UNFOLLOW);

        }
    };

    private WebViewClient defaultWebView = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:" + Script.EXTRACT_TEXT);
            view.loadUrl("javascript:window.Android.processContent(" + Script.FUNCTION_EXTRACT_TEXT + ");");
            //view.loadUrl("javascript:window.Android.showHtml("+ Script.RECUP_HTML +");");
        }
    };

    public Browser(Activity activity) {
        super(activity);
        this.activity = activity;
        setDefaultWebClient();
    }
    private void setDefaultWebClient(){
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.addJavascriptInterface(new IJavascriptHandler(this.getContext()), "Android");
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.setWebChromeClient(new WebChromeClient());
        this.setWebViewClient(defaultWebView);
    }
    public JSONObject getJsonObject(String url){
        JSONObject jsonObject = null;
        this.load(url);
        this.waitReceive();
        try {
            jsonObject = new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void load(final String url){
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (getWebViewClient() != defaultWebView){
                        setDefaultWebClient();
                    }
                }
                loadUrl(url);
            }
        });
    }
    public void clickOnUser(final String username, final String action){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (action){
                    case InstagramApp.ACTION_FOLLOW:
                        setWebViewClient(webFollow);
                        break;
                    case InstagramApp.ACTION_UNFOLLOW:
                        setWebViewClient(webUnfollow);
                        break;
                }
                loadUrl(Utils.SITE_MAIN + username);
            }
        });
    }
    private void waitReceive(){
        while (!isReceived()){ }
        received = false;
    }

    //getter and setter
    public String getContent() {
        return content;
    }

    public static boolean isReceived() {
        return received;
    }

    public static void setReceived(boolean received) {
        Browser.received = received;
    }

    /**
     * class de gestion du java script
     * sert essencielement a recupere le contenu
     * dans la webView
     */
    private class IJavascriptHandler {
        Context mContext;

        public IJavascriptHandler(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void processContent(String aContent) {
                content = aContent;
                received = true;
        }
        @JavascriptInterface
        public void showHtml(String html){
            System.out.println(html);
        }
    }
}
