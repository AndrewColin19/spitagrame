package com.spitagram.Modele.InstagramApi;

import android.app.Activity;
import android.util.Log;

import com.spitagram.Controller.ApiController;
import com.spitagram.Modele.DataBase;
import com.spitagram.Modele.InstagramApi.Browser.Browser;
import com.spitagram.Modele.InstagramApi.Users.CurrentUser;
import com.spitagram.Modele.InstagramApi.Users.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstagramApp{

    private Activity activity;
    private Browser browser;
    private DataBase dataBase;
    public static final String TAG = "InstagrameApp";

    public InstagramApp(final Activity activity){
        this.activity = activity;
        this.dataBase = new DataBase(this.activity);
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browser = new Browser(activity);
            }
        });
        while(browser == null) {}
    }

    /**
     *
     * @return
     */
    public CurrentUser setUser(){
        CurrentUser currentUser = new CurrentUser(getUserName());
        try {
            JSONObject reader = getJsonUser(currentUser.getUserName());
            currentUser.setId(reader.getJSONObject("graphql")
                    .getJSONObject("user")
                    .getString("id"));
            currentUser.setFollow(reader.getJSONObject("graphql")
                    .getJSONObject("user").getJSONObject("edge_follow")
                    .getString("count"));
            currentUser.setFollowers(reader.getJSONObject("graphql")
                    .getJSONObject("user")
                    .getJSONObject("edge_followed_by")
                    .getString("count"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentUser;
    }
    private String getUserName(){
        String username = "";
        JSONObject jsonObject = getData();
        try {
            username = jsonObject.getJSONObject("config").getJSONObject("viewer").getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return username;

    }
    /**
     *
     * @return
     */
    private JSONObject getData(){
        JSONObject jsonObject = browser.getJsonObject(Utils.DATA_REQUEST);
        return jsonObject;
    }
    /**
     * @param userName
     * recupere le fichier json de l'utilisateur en paramètre
     * @return
     */
    private JSONObject getJsonUser(String userName) {
        // a modifier
        String url = "https://www.instagram.com/"+ Utils.NAME_COMPTE +"/?__a=1";
        JSONObject jsonObject = browser.getJsonObject(url);
        return jsonObject;
    }

    public int getFollowers(CurrentUser user){
        user.setFollowersList(getUserList(User.FOLLOWERS));
        user.setNbfollowers(user.getFollowersListSize());
        return user.getNbfollowers();
    }

    public int getFollow(CurrentUser user){
        user.setFollowList(getUserList(User.FOLLOW));
        user.setNbfollow(user.getFollowListSize());
        return user.getNbfollow();
    }

    private ArrayList<User> getUserList(String type){
        String nextPage = "false";
        String endCursor = "";
        String url = "";
        String query = Utils.QUERY_ID_FOLLOW;
        if(type.equals(User.FOLLOWERS)){
            query = Utils.QUERY_ID_FOLLOWERS;
        }
        CurrentUser user = ApiController.currentUser;
        ArrayList<User> userList = new ArrayList<>();
        do {
            if(nextPage.equals("false")){
                url = Utils.QUERY_REQUEST + "?query_id=" + query + "&id="
                        + user.getId() +"&first=" + Utils.NB_USER_TAKEN;
            }else{
                url = Utils.QUERY_REQUEST + "?query_id=" + query + "&id="
                        + user.getId() + "&first=" + Utils.NB_USER_TAKEN + "&after=" + endCursor;
            }
            try {
                JSONObject reader = browser.getJsonObject(url);
                //recupere la page info
                JSONObject pageInfo = reader.getJSONObject("data")
                        .getJSONObject("user")
                        .getJSONObject(type)
                        .getJSONObject("page_info");
                //
                nextPage = pageInfo.getString("has_next_page");
                if (nextPage.equals("true")){
                    endCursor = pageInfo.getString("end_cursor");
                }
                //recupe le tableau des abonnées
                JSONArray followerTab = reader.getJSONObject("data")
                        .getJSONObject("user")
                        .getJSONObject(type)
                        .getJSONArray("edges");
                for (int i = 0; i < followerTab.length(); i++) {
                    String id = followerTab.getJSONObject(i).getJSONObject("node").getString("id");
                    String username = followerTab.getJSONObject(i).getJSONObject("node").getString("username");
                    userList.add(new User(id, username));
                }
            } catch(Exception e){
                e.printStackTrace();
                break;
            }
        }while (nextPage.equals("true"));
        return userList;
    }

    /**
     * ajoute les données a la base seulement si il n'y sont pas déja
     * @param user
     */
    public void writeDataBase(CurrentUser user){
        for (User u : user.getFollowersList()){
            if (dataBase.getFollowers(u) == null){
                dataBase.insertFollowers(u);
            }
        }
        for (User u: user.getFollowList()){
            if (dataBase.getFollow(u) == null){
                dataBase.insertFollow(u);
            }
        }
    }

    private int getMutual(){
        int compt = 0;
        ArrayList<User> followersList = ApiController.currentUser.getFollowersList();
        ArrayList<User> followList = ApiController.currentUser.getFollowList();
        for (User s: followersList){
            for(User user: followList){
                if (s.getId().equals(user.getId())){
                    compt++;
                    Log.d(TAG, "getMutual: username : " + user.getUserName());
                }
            }
        }
        return compt;
    }
    private int getUnfollow(){
        return ApiController.currentUser.getNbfollowers() - dataBase.getNbFollowers();
    }
    private int getNewFollow(){
        return ApiController.currentUser.getNbfollow() - dataBase.getNbFollow();
    }

    public int[] getStats(){
        int unfollow = getUnfollow();
        int newfollow = getNewFollow();
        int newfollwers = 0;
        int mutual = getMutual();
        if (unfollow > 0){
            newfollwers = unfollow;
            unfollow = 0;
        }
        if (newfollow < 0){
            newfollow = 0;
        }
        int[] stats = {newfollwers, unfollow, newfollow, mutual};
        return stats;
    }
}