package com.spitagram.Vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.spitagram.Controller.SplashScreenController;
import com.spitagram.Modele.AppConfig;
import com.spitagram.Modele.DataBase;
import com.spitagram.R;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(AppConfig.DEBUG){
            pass();
        }else{
            splashText = findViewById(R.id.splashText);
            //new DataBase(this).reset();
            SplashScreenController splashScreenController = new SplashScreenController(this);
            splashScreenController.init(splashText);

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void pass(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}