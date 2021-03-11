package com.spitagram.Modele.InstagramApi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.spitagram.Controller.ApiController;
import com.spitagram.Modele.DataBase;
import com.spitagram.Modele.InstagramApi.Browser.Browser;
import com.spitagram.Modele.InstagramApi.Users.CurrentUser;
import com.spitagram.Modele.InstagramApi.Users.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.UnknownServiceException;
import java.util.ArrayList;

public class InstagramApp{

    private Activity activity;
    public Browser browser;
    private DataBase dataBase;
    public static final String ACTION_FOLLOW = "follow_user";
    public static final String ACTION_UNFOLLOW = "unfollow_user";
    public static String JSON_KEY_FOLLOW = "edge_follow";
    public static String JSON_KEY_FOLLOWERS = "edge_followed_by";
    public static final String TAG = "InstagrameApp";
    public static int progressPurcent = 0;

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
     * scrap les données et les stocks dans la base de donnée
     * puit set les stats de l'utilisateur
     * @param currentUser
     */
    public void scrapDataUser(CurrentUser currentUser){
        this.getFollowers(currentUser);
        this.getFollow(currentUser);
        this.setStats();
        this.writeDataBase(currentUser);
    }

    /**
     * scrap les personnes que suit l'utilisateur et ce qui le suivent
     * @return
     */
    public CurrentUser setUser(){
        CurrentUser currentUser = new CurrentUser(getUserName());
        try {
            JSONObject reader = getJsonUser(currentUser.getUserName());
            //recupere le pseudo et l'id
            currentUser.setId(Long.parseLong(reader.getJSONObject("graphql")
                    .getJSONObject("user")
                    .getString("id")));
            //recupere le nombre de follow
            currentUser.setNbFollow(reader.getJSONObject("graphql")
                    .getJSONObject("user").getJSONObject("edge_follow")
                    .getString("count"));
            //recupere le nombre de followers
            currentUser.setNbFollowers(reader.getJSONObject("graphql")
                    .getJSONObject("user")
                    .getJSONObject("edge_followed_by")
                    .getString("count"));
            //recupere l'image de profile
            currentUser.setUserImg(loadBitmap(reader.getJSONObject("graphql")
                    .getJSONObject("user").getString("profile_pic_url")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentUser;
    }

    /**
     * recupere le pseudo de l'utilisateur accutellement connecter
     * @return le pseudo de l'utilisateur
     */
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
     * fait une requete pour recupere le json des données de l'utilisateur
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
        //String url = "https://www.instagram.com/"+ Utils.NAME_COMPTE +"/?__a=1";
        String url = "https://www.instagram.com/"+ userName +"/?__a=1";
        JSONObject jsonObject = browser.getJsonObject(url);
        return jsonObject;
    }

    /**
     * scrap les followers de l'utilisateur
     * et les stock dans l'utilisateur courrent
     * @param user
     */
    public void getFollowers(CurrentUser user){
        user.setFollowersList(getUserList(JSON_KEY_FOLLOWERS));
    }

    /**
     * scrap les follow de l'utilisateur
     * et les stock dans l'utilisateur courrent
     * @param user
     */
    public void getFollow(CurrentUser user){
        user.setFollowList(getUserList(JSON_KEY_FOLLOW));
    }

    /**
     * utiliser pour scraper les données par type
     * @param type
     * @return
     */
    private ArrayList<User> getUserList(String type){
        CurrentUser user = ApiController.currentUser;
        String nextPage = "false";
        String endCursor = "";
        String url = "";
        String query = Utils.QUERY_ID_FOLLOW;
        int max = Integer.parseInt(user.getNbFollow());
        if(type.equals(JSON_KEY_FOLLOWERS)){
            query = Utils.QUERY_ID_FOLLOWERS;
            max = Integer.parseInt(user.getNbFollowers());
        }

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
                    //Bitmap imgProfile = loadBitmap(followerTab.getJSONObject(i).getJSONObject("node").getString("profile_pic_url"));
                    userList.add(new User(Long.parseLong(id), username/*, imgProfile*/));
                }
                progressPurcent += ((followerTab.length()*100)/max)/2;
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
        //Log.d(TAG, "writeDataBase: new size : " + user.getFollowList().size());
        //Log.d(TAG, "writeDataBase: new size followers : " + user.getFollowersList().size());
        /*int i = 0;
        int j = 0;*/
        for (User u : user.getFollowersList()){
            if (dataBase.getFollowers(u) == null){
                dataBase.insertFollowers(u);
                //j++;
            }
        }
        //Log.d(TAG, "writeDataBase: insert followers : " + j);
        for (User u: user.getFollowList()){
            if (dataBase.getFollow(u) == null){
                dataBase.insertFollow(u);
                //i++;
            }
            //Log.d(TAG, "writeDataBase: follow id : " + u.getId() + " name : " + u.getUserName());
        }
        //Log.d(TAG, "writeDataBase: insert : " + i);
    }

    /**
     *
     * @return
     */
    private ArrayList<User> getMutual(){
        ArrayList<User> followersList = ApiController.currentUser.getFollowersList();
        ArrayList<User> followList = ApiController.currentUser.getFollowList();
        ArrayList<User> mutual = new ArrayList<>();
        for (User u: followersList){
            boolean test = false;
            for(User user: followList){
                if (u.getId() == user.getId()){
                    test= true;
                }
            }
            if (test){
                mutual.add(u);
            }
        }
        return mutual;
    }

    private ArrayList<User> getLosefollowers(ArrayList<User> newFollowersList, ArrayList<User> oldFollowersList){
        ArrayList<User> lose = new ArrayList<>();
        for (User u : oldFollowersList){
            boolean test = false;
            for (User user : newFollowersList) {
                if (u.getId() == user.getId()) {
                    test = true;
                }
            }
            if (!test){
                lose.add(u);
            }
        }
        return lose;
    }
    private ArrayList<User> getNewFollowers(ArrayList<User> newFollowersList, ArrayList<User> oldFollowersList){
        ArrayList<User> win = new ArrayList<>();
        for (User u : newFollowersList){
            boolean test = false;
            for (User user : oldFollowersList) {
                if (u.getId() == user.getId()) {
                    test = true;
                }
            }
            if (!test){
                win.add(u);
            }
        }
        return win;
    }
    private int getNewFollow(){
        return ApiController.currentUser.getFollowList().size() - dataBase.getNbFollow();
    }

    private void setStats(){
        ArrayList<User> newFollowersList = ApiController.currentUser.getFollowersList();
        ArrayList<User> oldFollowersList = dataBase.getAllFollowers();
        ApiController.currentUser.setLoseFollowersList(getLosefollowers(newFollowersList, oldFollowersList));
        ApiController.currentUser.setWinFollowersList(getNewFollowers(newFollowersList, oldFollowersList));
        ApiController.currentUser.setMutualList(getMutual());
    }

    public int[] getStats(){
        int newfollow = getNewFollow();
        int newfollwers = ApiController.currentUser.getWinFollowersList().size();
        int Losefollowers = ApiController.currentUser.getLoseFollowersList().size();
        int mutual = ApiController.currentUser.getMutualList().size();
        int[] stats = {newfollwers, Losefollowers, newfollow, mutual};
        return stats;
    }

    /**
     * charge l'image de profile de l'utilisateur
     * @param url
     * @return
     */
    private Bitmap loadBitmap(String url) {
        url = url.replaceAll("amp;", "");
        Bitmap mIcon_val = null;
        try{
            URL req = new URL(url);

            mIcon_val = BitmapFactory.decodeStream(req.openConnection()
                    .getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

        return mIcon_val;
    }

    public void actionClick(String username, String action){
        browser.clickOnUser(username, action);
    }

    public void addFollowers(User newFollowers){
        if (dataBase.getFollowers(newFollowers) == null){
            dataBase.insertFollowers(newFollowers);
        }
    }
    public void addFollow(User newFollow){
        if (dataBase.getFollow(newFollow) == null){
            dataBase.insertFollow(newFollow);
        }
    }
    public void removeFollowers(User followers){
        if (dataBase.getFollowers(followers) != null){
            dataBase.deleteFollowers(followers);
        }
    }

    public void removeFollow(User follow){
        if (dataBase.getFollow(follow) != null){
            dataBase.deleteFollow(follow);
        }
    }
}