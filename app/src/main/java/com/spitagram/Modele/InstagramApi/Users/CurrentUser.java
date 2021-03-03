package com.spitagram.Modele.InstagramApi.Users;
import java.util.ArrayList;

public class CurrentUser extends User{
    private String follow;
    private String followers;
    private int nbfollow;
    private int nbfollowers;
    private ArrayList<User> followersList = new ArrayList<>();
    private ArrayList<User> followList = new ArrayList<>();

    //constructeur
    public CurrentUser(){}

    public CurrentUser(String userName){
        super(userName);
    }
    //methode
    public void addFollowers(String id, String username){
        followersList.add(new User(id, username));
    }
    public void addFollow(String id, String username){
        followList.add(new User(id, username));
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
        if(follow != null || !follow.equals("")){
            this.follow = follow;
        }
    }

    public void setFollowers(String followers) {
        if (followers != null || !followers.equals("")){
            this.followers = followers;
        }
    }
    public ArrayList<User> getFollowersList(){ return followersList; }

    public ArrayList<User> getFollowList() {
        return followList;
    }
    public int getFollowersListSize(){
        return followersList.size();
    }
    public int getFollowListSize(){
        return followList.size();
    }

    public void setFollowersList(ArrayList<User> followersList) {
        this.followersList = followersList;
    }
    public void setFollowList(ArrayList<User> followList) {
        this.followList = followList;
    }

    public int getNbfollow() {
        return nbfollow;
    }

    public void setNbfollow(int nbfollow) {
        this.nbfollow = nbfollow;
    }

    public int getNbfollowers() {
        return nbfollowers;
    }

    public void setNbfollowers(int nbfollowers) {
        this.nbfollowers = nbfollowers;
    }
}
