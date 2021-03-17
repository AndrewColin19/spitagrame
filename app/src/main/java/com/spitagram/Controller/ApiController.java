package com.spitagram.Controller;

import android.app.Activity;

import com.spitagram.Modele.AppConfig;
import com.spitagram.Modele.InstagramApi.InstagramApp;
import com.spitagram.Modele.InstagramApi.Users.CurrentUser;
import com.spitagram.Modele.InstagramApi.Users.User;
import com.spitagram.R;


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
        User user = null;
        if (id == R.id.cardFollow) {
            user = currentUser.getUserNoFollowBack(userId);
            currentUser.getNoFollowBackList().remove(user);
        }
        if (id == R.id.cardLoseFollowers) {
            user = currentUser.getUserLose(userId);
            currentUser.getLoseFollowersList().remove(user);
        }
        instagramApp.actionClick(user.getUserName(), InstagramApp.ACTION_UNFOLLOW);
        instagramApp.removeFollowers(user);
        instagramApp.removeFollow(user);

    }
    public void followUser(long userId){
        User user = currentUser.getUserFollowers(userId);
        instagramApp.actionClick(user.getUserName(), InstagramApp.ACTION_FOLLOW);
        instagramApp.addFollow(user);
        instagramApp.addFollowers(user);
        currentUser.getWinFollowersList().remove(user);
    }
}
