package com.spitagram.Vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.spitagram.Controller.AppController;
import com.spitagram.Controller.LoginController;
import com.spitagram.R;

import static java.lang.Thread.sleep;

public class SplashScreenActivity extends AppCompatActivity {

    private Activity activity;
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.activity = this;
        splashText = findViewById(R.id.splashText);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!AppController.isConnectedInternet(activity)){
                    while(!AppController.isConnectedInternet(activity)){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    try {
                        sleep(500);
                        setTextSplashText("Vérification connection à internet ok");
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setTextSplashText("Vérification Session...");
                    if(LoginController.isconnected(activity)){
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }else{
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void setTextSplashText(final String text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                splashText.setText(text);
            }
        });
    }
}