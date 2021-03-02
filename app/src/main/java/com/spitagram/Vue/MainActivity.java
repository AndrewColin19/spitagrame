package com.spitagram.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.spitagram.Modele.InstagramApi.Users.InstagramUser;
import com.spitagram.R;
import com.spitagram.Modele.InstagramApi.InstagramApp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;
        //user test {wes.off__, pepperlapinebelier, marieamelie.wen, annickd06}
        WebView web = findViewById(R.id.web);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web.loadUrl("https://www.instagram.com/");

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                InstagramApp instagramApp = new InstagramApp(activity);
                user = instagramApp.setUserInfo("wes.off__");
                int nb = instagramApp.getFollowers(user);
                System.out.println("nb followers : " + nb);
            }
        }).start();*/
    }
}