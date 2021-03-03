package com.spitagram.Modele.InstagramApi;

import android.app.Activity;

import com.spitagram.Controller.ApiController;
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
    public static final String TAG = "InstagrameApp";

    public InstagramApp(final Activity activity){
        this.activity = activity;
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
        JSONObject jsonObject = browser.getJsonObject(Config.DATA_REQUEST);
        return jsonObject;
    }
    /**
     * @param userName
     * recupere le fichier json de l'utilisateur en paramètre
     * @return
     */
    private JSONObject getJsonUser(String userName) {
        String url = "https://www.instagram.com/"+ userName +"/?__a=1";
        JSONObject jsonObject = browser.getJsonObject(url);
        return jsonObject;
    }
    public ArrayList<User> getUnfollow(){
        ArrayList<User> listUnfollow = new ArrayList<>();
        return listUnfollow;
    }

    public int getFollowers(CurrentUser user){
        user.setFollowersList(getUserList(User.FOLLOWERS));
        return user.getFollowersListSize();
    }
    public int getFollow(CurrentUser user){
        user.setFollowList(getUserList(User.FOLLOW));
        return user.getFollowListSize();
    }

    private ArrayList<User> getUserList(String type){
        String nextPage = "false";
        String endCursor = "";
        String url = "";
        CurrentUser user = ApiController.currentUser;
        ArrayList<User> userList = new ArrayList<>();
        do {
            if(nextPage.equals("false")){
                url = Config.QUERY_REQUEST + "?query_id=" + Config.QUERY_ID_FOLLOW + "&id="
                        + user.getId() + "&include_reel=false&fetch_mutual=false&first=" + Config.NB_USER_TAKEN;
            }else{
                url = Config.QUERY_REQUEST + "?query_id=" + Config.QUERY_ID_FOLLOW + "&id="
                        + user.getId() + "&include_reel=true&fetch_mutual=false&first=" + Config.NB_USER_TAKEN + "&after=" + endCursor;
            }
            try {
                JSONObject reader = browser.getJsonObject(url);
                //recupere la page info
                JSONObject pageInfo = reader.getJSONObject("data")
                        .getJSONObject("user")
                        .getJSONObject("edge_follow")
                        .getJSONObject("page_info");
                //
                nextPage = pageInfo.getString("has_next_page");
                if (nextPage.equals("true")){
                    endCursor = pageInfo.getString("end_cursor");
                }
                //recupe le tableau des abonnées
                JSONArray followerTab = reader.getJSONObject("data")
                        .getJSONObject("user")
                        .getJSONObject("edge_follow")
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
}