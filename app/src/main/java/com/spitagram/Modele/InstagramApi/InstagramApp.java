package com.spitagram.Modele.InstagramApi;

import android.app.Activity;

import com.spitagram.Modele.InstagramApi.Browser.Browser;
import com.spitagram.Modele.InstagramApi.Users.InstagramUser;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public InstagramUser setUserInfo(String userName){
        InstagramUser instagramUser = new InstagramUser(userName);
        try{
            JSONObject reader = getJsonUser(userName);
            instagramUser.setId(reader.getJSONObject("graphql")
                    .getJSONObject("user")
                    .getString("id"));
            instagramUser.setFollow(reader.getJSONObject("graphql")
                    .getJSONObject("user").getJSONObject("edge_follow")
                    .getString("count"));
            instagramUser.setFollowers(reader.getJSONObject("graphql")
                    .getJSONObject("user")
                    .getJSONObject("edge_followed_by")
                    .getString("count"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return instagramUser;
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

    public int getFollowers(final InstagramUser user){
        String nextPage = "false";
        String endCursor = "";
        String url = "";
        do {
            if(nextPage.equals("false")){
                url = Config.QUERY_REQUEST + "?query_id=" + Config.QUERY_ID_FOLLOWERS + "&id="
                        + user.getId() + "&include_reel=false&fetch_mutual=false&first=" + user.getFollowers();
            }else{
                url = Config.QUERY_REQUEST + "?query_id=" + Config.QUERY_ID_FOLLOWERS + "&id="
                        + user.getId() + "&include_reel=true&fetch_mutual=false&first=" + user.getFollowers() + "&after=" + endCursor;
            }
            try {
                JSONObject reader = browser.getJsonObject(url);
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
                break;
            }
        }while (nextPage.equals("true"));
        return user.getFollowersListSize();
    }
}