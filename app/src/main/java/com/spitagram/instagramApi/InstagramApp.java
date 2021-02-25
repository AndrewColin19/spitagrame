package com.spitagram.instagramApi;

import android.app.Activity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spitagram.instagramApi.BrowerRequest.Browser;
import com.spitagram.instagramApi.Users.InstagramUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstagramApp{

    private Activity activity;
    private Browser browser;
    public static boolean lock = true;

    public InstagramApp(Activity activity, Browser browser) {
        this.activity = activity;
        this.browser = browser;
    }

    public InstagramUser setUserInfo(String userName){
        InstagramUser user = new InstagramUser(userName);
        JSONObject jsonUser = getUserFile(getJsonUser(userName));
        user.setId(getIdUser(jsonUser));
        user.setFollow(getFollowCount(jsonUser));
        user.setFollowers(getfollowersCount(jsonUser));
        return user;
    }

    /**
     * @param userName
     * recupere le fichier json de l'utilisateur en paramètre
     * @return
     */
    private String getJsonUser(String userName) {
        String url = "https://www.instagram.com/"+ userName +"/?__a=1";
        RequestQueue queue = Volley.newRequestQueue(this.activity);
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.GET, url, future, future);
        queue.add(request);
        String rep = null;
        try {
            rep = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rep;
    }
    /**
     * @param jsonFile
     * @return
     */
    private JSONObject getUserFile(String jsonFile){
        JSONObject user = null;
        try {
            JSONObject reader = new JSONObject(jsonFile);
            JSONObject graphql = reader.getJSONObject("graphql");
            user = graphql.getJSONObject("user");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * @param user
     * @return
     */
    private String getfollowersCount(JSONObject user){
        String followers = "";
        try {
            JSONObject count = user.getJSONObject("edge_followed_by");
            followers = count.getString("count");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return followers;
    }

    /**
     * @param user
     * @return
     */
    private String getFollowCount(JSONObject user){
        String follow = "";
        try {
            JSONObject count = user.getJSONObject("edge_follow");
            follow = count.getString("count");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return follow;
    }

    /**
     * @param user
     * @return
     */
    private String getIdUser(JSONObject user){
        String id = "";
        try {
            id = user.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void getFollowers(final InstagramUser user){
        String nextPage = "false";
        String endCursor = "";
        do {
            if(nextPage.equals("false")){
                final String url = Config.QUERY_REQUEST + "?query_id=" + Config.QUERY_ID_FOLLOWERS + "&id="
                        + user.getId() + "&first=" + user.getFollowers();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        browser.loadUrl(url);
                    }
                });
            }else{
                final String url = Config.QUERY_REQUEST + "?query_id=" + Config.QUERY_ID_FOLLOWERS + "&id="
                        + user.getId() + "&first=" + user.getFollowers() + "&after=" + endCursor;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        browser.loadUrl(url);
                    }
                });
            }
            try {
                while (lock == true) {
                }
                lock = true;
                JSONObject reader = new JSONObject(browser.getContent());
                //recupere la page info
                JSONObject pageInfo = reader.getJSONObject("data")
                        .getJSONObject("user")
                        .getJSONObject("edge_followed_by")
                        .getJSONObject("page_info");
                //
                nextPage = pageInfo.getString("has_next_page");
                if (nextPage.equals("true")){
                    endCursor = pageInfo.getString("end_cursor");
                }
                //recupe le tableau des abonnées
                JSONArray followerTab = reader.getJSONObject("data")
                        .getJSONObject("user")
                        .getJSONObject("edge_followed_by")
                        .getJSONArray("edges");
                for (int i = 0; i < followerTab.length(); i++) {
                    String id = followerTab.getJSONObject(i).getJSONObject("node").getString("id");
                    String username = followerTab.getJSONObject(i).getJSONObject("node").getString("username");
                    user.addFollowers(id, username);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }while (nextPage.equals("true"));
        user.afficheFollowers();
    }
}
