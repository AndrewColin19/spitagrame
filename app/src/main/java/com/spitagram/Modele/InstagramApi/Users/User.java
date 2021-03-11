package com.spitagram.Modele.InstagramApi.Users;

import android.graphics.Bitmap;

public class User {
    private String userName;
    private long id;
    private Bitmap profilImg;

    public User(long id, String userName){
        this.id = id;

        this.userName = userName;
    }
    public User(long id, String userName, Bitmap bitmap){
        this.id = id;
        this.userName = userName;
        this.profilImg = bitmap;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getProfilImg() {
        return profilImg;
    }

    public void setProfilImg(Bitmap profilImg) {
        this.profilImg = profilImg;
    }
}
