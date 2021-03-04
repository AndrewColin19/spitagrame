package com.spitagram.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class SplashScreenController {

    private Activity activity;

    public SplashScreenController(Activity activity){
        this.activity = activity;
    }
    public void init(final TextView textView){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!isConnectedInternet()){
                    while(!isConnectedInternet()){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    try {
                        sleep(500);
                        setTextSplashText(textView,"Vérification connection à internet ok");
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setTextSplashText(textView,"Vérification Session...");
                    if(LoginController.isconnected(activity)){
                        AppController.startMainActivity(activity);
                    }else{
                        AppController.startLoginActivity(activity);
                    }
                }
            }
        });
        t1.start();
    }

    public boolean isConnectedInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            NetworkInfo.State networkState = networkInfo.getState();
            if (networkState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                return true;
            }
            else return false;
        }
        else return false;
    }
    public void setTextSplashText(final TextView textview, final String text){
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textview.setText(text);
            }
        });
    }
}
