package com.spitagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import android.webkit.WebView;

import com.spitagram.instagramApi.BrowerRequest.Browser;
import com.spitagram.instagramApi.InstagramApp;
import com.spitagram.instagramApi.Users.InstagramUser;

public class MainActivity extends AppCompatActivity {

    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;
        final WebView webView = findViewById(R.id.web);

        //user test {wes.off__, pepperlapinebelier}
        /*String url = "https://www.instagram.com/graphql/query/?query_id=17851374694183129&id=1158667799&first=424";
        webView.loadUrl(url);*/
        final Browser browser = new Browser(this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                InstagramApp instagramApp = new InstagramApp(activity, browser);
                InstagramUser user = instagramApp.setUserInfo("marieamelie.wen");
                instagramApp.getFollowers(user);
            }
        });
        t.start();
    }
}