package com.spitagram.Vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.spitagram.Controller.SplashScreenController;
import com.spitagram.R;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashText = findViewById(R.id.splashText);

        SplashScreenController splashScreenController = new SplashScreenController(this);
        splashScreenController.init(splashText);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}