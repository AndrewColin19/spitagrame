package com.spitagram.Controller;

import android.app.Activity;
import android.content.Intent;

import com.spitagram.Vue.LoginActivity;
import com.spitagram.Vue.MainActivity;

public class AppController {

    public static void startMainActivity(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public static void startLoginActivity(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
