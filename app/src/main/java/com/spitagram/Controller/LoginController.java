package com.spitagram.Controller;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.spitagram.Modele.InstagramApi.Browser.BrowserLogin;
import com.spitagram.Modele.InstagramApi.Utils;
import com.spitagram.Modele.InstagramApi.InstagramLogin;
import com.spitagram.R;

public class LoginController{
    private static String TAG = "LoginController";
    private static InstagramLogin instagramLogin;
    public static boolean connected = false;

    private static InstagramLogin getInstagramLogin(Activity activity){
        if (instagramLogin == null){
            instagramLogin = new InstagramLogin(activity);
        }
        return instagramLogin;
    }

    public static boolean isconnected(Activity activity){
        String statut ;
        statut = getInstagramLogin(activity).connexionStatut();
        if (statut.equals(Utils.STATUT_CONNECTION_CONNECTER)){
            connected = true;
        }else{
            connected = false;
        }
        return connected;
    }
    public static void waitConnection(final Activity activity){
        Log.d(TAG, "waitConnection: methode start");
        getInstagramLogin(activity).changeActivity(activity);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: debut attente");
                while(LoginController.connected != true){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "run: fin attente");
                Log.d(TAG, "run: statut connected : " + LoginController.connected);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppController.startMainActivity(activity);
                        activity.finish();
                    }
                });
            }
        });
        t.start();
    }
    public static void addBrowserInView(Activity activity, BrowserLogin browserLogin){
        RelativeLayout content = activity.findViewById(R.id.content);
        if(content != null){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            browserLogin.setLayoutParams(params);
            content.addView(browserLogin);
            browserLogin.load(Utils.SITE_MAIN);
        }
    }
}

