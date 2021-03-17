package com.spitagram.Modele.InstagramApi.Users;

import android.graphics.Bitmap;

public class User {
    private String userName;
    private long id;
    private long userMain;
    private Bitmap profilImg;

    public User(long id, String userName){
        this.id = id;
        this.userName = userName;
    }

    public User(long id, String userName, long userMain){
        this.id = id;
        this.userMain = userMain;
        this.userName = userName;
    }

    public User(long id, String userName, Bitmap bitmap){
        this.id = id;
        this.userName = userName;
        this.profilImg = bitmap;
    }
    public User(long id, String userName, long userMain, Bitmap bitmap){
        this.id = id;
        this.userName = userName;
        this.profilImg = bitmap;
        this.userMain = userMain;
    }

    public User(){
    }
    public User(String userName){
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", id=" + id +
                ", userMain=" + userMain +
                ", profilImg=" + profilImg +
                '}';
    }

    //getter and setter
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

    public long getUserMain() {
        return userMain;
    }

    public void setUserMain(long userMain) {
        this.userMain = userMain;
    }
}
