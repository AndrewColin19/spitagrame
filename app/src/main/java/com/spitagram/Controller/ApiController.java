package com.spitagram.Controller;

import android.app.Activity;

import com.spitagram.Modele.AppConfig;
import com.spitagram.Modele.InstagramApi.InstagramApp;
import com.spitagram.Modele.InstagramApi.Users.CurrentUser;
import com.spitagram.Modele.InstagramApi.Users.User;
import com.spitagram.R;

import java.net.UnknownServiceException;

public class ApiController {

    private static final String TAG = "ApiController";
    public InstagramApp instagramApp;
    private Activity activity;
    public static CurrentUser currentUser;
    public static boolean stats = false;

    public ApiController(Activity activity){
        this.activity = activity;
        getInstenceInstagramApi();
        init();
    }
    public InstagramApp getInstenceInstagramApi(){
        if (instagramApp == null){
            instagramApp = new InstagramApp(activity);
        }
        return instagramApp;
    }

    private void init(){
        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                instagramApp.scrapDataUser(currentUser);
                stats = true;
            }
        });
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                currentUser = getInstenceInstagramApi().setUser();
                if (AppConfig.SCRAP_DATA){
                    t2.start();
                }
            }
        });
        t1.start();
    }
    public int[] getStats(){
        return instagramApp.getStats();
    }

    public void unfollowUser(long userId, int id) {
        User user = currentUser.getUserFollow(userId);
        instagramApp.actionClick(user.getUserName(), InstagramApp.ACTION_UNFOLLOW);

        if (user != null) {
            instagramApp.removeFollow(user);
        }
        if (id == R.id.cardLoseFollowers) {
            currentUser.getLoseFollowersList().remove(user);
        }
    }
    public void followUser(long userId){
        User user = currentUser.getUserFollowers(userId);
        instagramApp.actionClick(user.getUserName(), InstagramApp.ACTION_FOLLOW);
        instagramApp.addFollow(user);
        currentUser.getWinFollowersList().remove(user);
    }
}
