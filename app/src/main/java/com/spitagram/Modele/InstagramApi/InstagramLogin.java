package com.spitagram.Modele.InstagramApi;

import android.app.Activity;
import com.spitagram.Controller.LoginController;
import com.spitagram.Modele.InstagramApi.Browser.BrowserLogin;

import org.json.JSONObject;

public class InstagramLogin {
    private Activity activity;
    public BrowserLogin browserLogin;
    public static final String TAG = "InstagrameLogin";

    public InstagramLogin(final Activity act){
        this.activity = act;
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browserLogin = new BrowserLogin(activity);
                LoginController.addBrowserInView(activity, browserLogin);
            }
        });
        while(browserLogin == null) {}
    }
    public String connexionStatut(){
        JSONObject data = this.browserLogin.getJsonObject(Utils.DATA_REQUEST);
        JSONObject viewer;
        try {
            viewer = data.getJSONObject("config").getJSONObject("viewer");
        }catch (Exception e){
            e.getStackTrace();
            return Utils.STATUT_CONNECTION_FAIL;
        }
        if(viewer != null){
            return Utils.STATUT_CONNECTION_CONNECTER;
        }
        return Utils.STATUT_CONNECTION_DECONNECTER;
    }
    public void changeActivity(Activity newActivity){
        this.activity = newActivity;
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browserLogin = new BrowserLogin(activity);
                LoginController.addBrowserInView(activity, browserLogin);
            }
        });
        while(browserLogin == null) {}
    }
}
