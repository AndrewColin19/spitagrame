package com.spitagram.instagramApi;

public class Config {
    public static String QUERY_REQUEST = "https://www.instagram.com/graphql/query/";
    public static String QUERY_BODY = "?query_id={id_query}&id={user_id}&first={nb}";
    public static String QUERY_BODY_NEXT_PAGE = "after={endPoint}";
    public static String QUERY_ID_FOLLOW = "17874545323001329";
    public static String QUERY_ID_FOLLOWERS = "17851374694183129";
    public static String QUERY_ID_POST = "17888483320059182";
    public static String QUERY_ID_LIKES_ON_POST = "17864450716183058";
}
