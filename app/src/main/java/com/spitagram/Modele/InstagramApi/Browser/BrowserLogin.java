package com.spitagram.Modele.InstagramApi.Browser;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.spitagram.Controller.LoginController;
import com.spitagram.Modele.InstagramApi.Config;

import org.json.JSONException;
import org.json.JSONObject;


public class BrowserLogin extends WebView {
    private String content = "";
    private Activity activity;
    private static boolean received = false;
    private static String JAVASCRIPT_FUNCTION_CALL = "getAllText()";
    private static String JAVASCRIPT_SCRIPT_TO_EXTRACT_TEXT = "function getAllText(){ " +
            "return (document.getElementsByTagName('pre')[0].innerHTML); };";

    public BrowserLogin(Activity activity) {
        super(activity);
        this.activity = activity;
        init();
    }

    private void init(){

        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.addJavascriptInterface(new IJavascriptHandler(this.getContext()), "Android");
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:" + JAVASCRIPT_SCRIPT_TO_EXTRACT_TEXT);
                view.loadUrl("javascript:window.Android.processContent(" + JAVASCRIPT_FUNCTION_CALL + ");");
                if(url.equals(Config.LOGIN_PASS)){
                    if(LoginController.connected != true){
                        LoginController.connected = true;
                    }
                }
            }
        });
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
                loadUrl(url);
            }
        });
    }
    private void waitReceive(){
        while (!isReceived()){ }
        received = false;
    }
    public static boolean isReceived() {
        return received;
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
    }
}
