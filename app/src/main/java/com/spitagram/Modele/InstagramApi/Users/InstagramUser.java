package com.spitagram.Modele.InstagramApi.Users;
import java.io.File;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

public class InstagramUser extends User{
    private String follow;
    private String followers;
    private ArrayList<User> followersList = new ArrayList<>();

    //constructeur
    public InstagramUser(){}

    public InstagramUser(String userName){
        super(userName);

    }
    //methode
    public void addFollowers(String id, String username){
        if (!ifContain(id)){
            followersList.add(new User(id, username));
        }
    }
    private boolean ifContain(String id){
        boolean test = false;
        for(User u : followersList){
            if (u.getId().equals(id)){
                test = true;
            }
        }
        return test;
    }
    public int getFollowersListSize(){
        return followersList.size();
    }

    @Override
    public String toString() {
        return "InstagramUser{" +
                "userName='" + getUserName() + '\'' +
                ", id='" + getId() + '\'' +
                ", follow='" + follow + '\'' +
                ", followers='" + followers + '\'' +
                '}';
    }

    //getter and setter
    public String getFollow() {
        return follow;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public ArrayList<User> getFollowersList(){ return followersList; }
}
