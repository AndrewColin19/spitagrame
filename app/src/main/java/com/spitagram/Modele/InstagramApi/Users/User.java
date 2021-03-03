package com.spitagram.Modele.InstagramApi.Users;

public class User {
    public static String FOLLOW = "edge_follow";
    public static String FOLLOWERS = "edge_followed_by";

    private String userName;
    private String id;

    public User(String id, String userName){
        this.id = id;
        this.userName = userName;
    }
    public User(int id, String userName){
        this.id = String.valueOf(id);
        this.userName = userName;
    }
    public User(){
    }
    public User(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
