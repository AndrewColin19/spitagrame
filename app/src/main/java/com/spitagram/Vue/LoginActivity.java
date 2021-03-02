package com.spitagram.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.spitagram.Controller.LoginController;
import com.spitagram.R;

public class LoginActivity extends AppCompatActivity {
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.activity = this;


        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginController.waitConnection(activity);
            }
        }).start();
    }
}