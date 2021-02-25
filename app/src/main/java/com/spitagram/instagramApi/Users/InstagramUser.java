package com.spitagram.instagramApi.Users;

import java.util.ArrayList;

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
        followersList.add(new User(id, username));
    }
    public void afficheFollowers(){
        System.out.println("nb followers : " + followersList.size());
        /*for (User u: followersList){
            System.out.println(u.getUserName());
        }*/
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
}
