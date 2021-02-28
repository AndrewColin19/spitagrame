package com.spitagram.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.spitagram.Modele.InstagramApi.Users.InstagramUser;
import com.spitagram.Modele.InstagramApi.Users.User;
import com.spitagram.R;
import com.spitagram.Modele.InstagramApi.InstagramApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

    private Activity activity;
    private InstagramUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;
        //user test {wes.off__, pepperlapinebelier, marieamelie.wen}
        /*WebView web = findViewById(R.id.web);
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
        web.loadUrl("https://www.instagram.com/");*/
        //browser.loadUrl("https://www.instagram.com");

        new Thread(new Runnable() {
            @Override
            public void run() {
                InstagramApp instagramApp = new InstagramApp(activity);
                user = instagramApp.setUserInfo("marieamelie.wen");
                int i = instagramApp.getFollowers(user);
                System.out.println("followers : " + i);
                String data = "";
                /*for(User u :user.getFollowersList()){
                data += u.getUserName() + "\n";
                }*/
                //writeToFile(data);
            }
        }).start();
    }
    private void writeToFile(String data) {
        String fileName = "config.txt";
        String contentToWrite = data;
        File myFile = new File("/data/data/com.spitagram/files/", fileName);

        /*Write to file*/
        ///storage/self/primary/Android/data/com.spitagram/files/config.txt
        try {
            FileWriter fileWriter = new FileWriter(myFile);
            fileWriter.append(contentToWrite);
            fileWriter.flush();
            fileWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}