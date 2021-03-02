package com.spitagram.Controller;

import android.app.Activity;

import com.spitagram.Modele.InstagramApi.InstagramApp;

public class ApiController {

    private Activity activity;
    private InstagramApp instagramApp;
    private Thread mainThread;

    public ApiController(Activity activity){
        this.activity = activity;
        this.instagramApp = new InstagramApp(this.activity);

    }
}
