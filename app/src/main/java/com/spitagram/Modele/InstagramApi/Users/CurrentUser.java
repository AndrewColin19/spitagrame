package com.spitagram.Modele.InstagramApi.Users;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class CurrentUser extends User{
    private String nbFollow;
    private String nbFollowers;
    private Bitmap userImg;
    private ArrayList<User> followersList = new ArrayList<>();
    private ArrayList<User> followList = new ArrayList<>();
    private ArrayList<User> winFollowersList = new ArrayList<>();
    private ArrayList<User> loseFollowersList = new ArrayList<>();
    private ArrayList<User> newFollowList = new ArrayList<>();
    private ArrayList<User> mutualList = new ArrayList<>();

    //constructeur
    public CurrentUser(){}

    public CurrentUser(String userName){
        super(userName);
    }

    @Override
    public String toString() {
        return "InstagramUser{" +
                "userName='" + getUserName() + '\'' +
                ", id='" + getId() + '\'' +
                ", follow='" + nbFollow + '\'' +
                ", followers='" + nbFollowers + '\'' +
                '}';
    }

    //getter and setter
    public String getNbFollow() {
        return nbFollow;
    }

    public String getNbFollowers() {
        return nbFollowers;
    }

    public void setNbFollow(String nbFollow) {
        if(nbFollow != null || !nbFollow.equals("")){
            this.nbFollow = nbFollow;
        }
    }

    public void setNbFollowers(String nbFollowers) {
        if (nbFollowers != null || !nbFollowers.equals("")){
            this.nbFollowers = nbFollowers;
        }
    }
    public ArrayList<User> getFollowersList(){ return followersList; }

    public ArrayList<User> getFollowList() {
        return followList;
    }

    public void setFollowersList(ArrayList<User> followersList) {
        this.followersList = followersList;
    }
    public void setFollowList(ArrayList<User> followList) {
        this.followList = followList;
    }

    public ArrayList<User> getWinFollowersList() {
        return winFollowersList;
    }

    public void setWinFollowersList(ArrayList<User> winFollowersList) {
        this.winFollowersList = winFollowersList;
    }

    public ArrayList<User> getNewFollowList() {
        return newFollowList;
    }

    public void setNewFollowList(ArrayList<User> newFollowList) {
        this.newFollowList = newFollowList;
    }

    public ArrayList<User> getLoseFollowersList() {
        return loseFollowersList;
    }

    public void setLoseFollowersList(ArrayList<User> loseFollowersList) {
        this.loseFollowersList = loseFollowersList;
    }

    public ArrayList<User> getMutualList() {
        return mutualList;
    }

    public void setMutualList(ArrayList<User> mutualList) {
        this.mutualList = mutualList;
    }

    public Bitmap getUserImg() {
        return userImg;
    }

    public void setUserImg(Bitmap userImg) {
        this.userImg = userImg;
    }

    public User getUserFollow(long userId){
        User userFollow = null;
        for (User u: this.getFollowList()){
            if (u.getId()== userId){
                userFollow = u;
            }
        }
        return userFollow;
    }
    public User getUserFollowers(long userId){
        User userFollowers = null;
        for (User u: this.getFollowersList()){
            if (u.getId() == userId){
                userFollowers = u;
            }
        }
        return userFollowers;
    }
}
