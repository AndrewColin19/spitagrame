package com.spitagram.Modele;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.spitagram.Modele.InstagramApi.Users.CurrentUser;
import com.spitagram.Modele.InstagramApi.Users.User;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    private static final String TAG = "dataBase";
    private static final String DATABASE_NAME = "spitagramDataBase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USER = "CREATE TABLE user ("
            + " id INTEGER NOT NULL primary key ,"
            + " username text NOT NULL ,"
            + " multicompte int"
            + ")";
    private static final String CREATE_TABLE_FOLLOW = "CREATE TABLE follow ("
            + " id INTEGER NOT NULL primary key ,"
            + " username text NOT NULL ,"
            + " usermain INTEGER NOT NULL"
            + ")";
    private static final String CREATE_TABLE_FOLLOWERS = "CREATE TABLE followers ("
            + " id INTEGER NOT NULL primary key ,"
            + " username text NOT NULL ,"
            + " usermain INTEGER NOT NULL"
            + ")";
    private static final String DROP_TABLE_USER = "DROP TABLE User";
    private static final String DROP_TABLE_FOLLOW = "DROP TABLE follow";
    private static final String DROP_TABLE_FOLLOWERS = "DROP TABLE FOLLOWERS";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        creatTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
    public void creatTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_FOLLOW);
        db.execSQL(CREATE_TABLE_FOLLOWERS);
    }
    public void dropAll(SQLiteDatabase db){
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_FOLLOW);
        db.execSQL(DROP_TABLE_FOLLOWERS);
    }
    public User getFollow(CurrentUser currentUser, User u){
        User user = null;
        String req = "SELECT * FROM follow WHERE id =" + u.getId()
                + " AND usermain = " + currentUser.getId();
        Log.d(TAG, "getFollow: " + req);
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                user = new User(cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getLong(cursor.getColumnIndex("usermain")));
                cursor.moveToNext();
            }
        }
        cursor.close();
        Log.d(TAG, "getFollow: " + user);
        return user;
    }
    public User getFollowByName(String username){
        User user = null;
        String req = "SELECT * FROM follow WHERE username = '" + username + "'";
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                user = new User(cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getLong(cursor.getColumnIndex("usermain")));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return user;
    }

    public User getFollowers(CurrentUser currentUser, User u){
        User user = null;
        String req = "SELECT * FROM followers WHERE followers.id =" + u.getId() + " AND followers.usermain =" + currentUser.getId();
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                user = new User(cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getLong(cursor.getColumnIndex("usermain")));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return user;
    }

    public User getFollowersByName(String username){
        User user = null;
        String req = "SELECT * FROM followers WHERE username = '" + username + "'";
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                user = new User(cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getLong(cursor.getColumnIndex("usermain")));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return user;
    }
    public ArrayList<User> getAllFollow(CurrentUser user){
        ArrayList<User> followList = new ArrayList<>();
        String req = "SELECT * FROM follow WHERE usermain = " + user.getId();
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            followList.add(new User(cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getLong(cursor.getColumnIndex("usermain"))));
            cursor.moveToNext();
        }
        cursor.close();
        return followList;
    }

    public ArrayList<User> getAllFollowers(CurrentUser user){
        ArrayList<User> followersList = new ArrayList<>();
        String req = "SELECT * FROM followers WHERE usermain = " + user.getId();
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            followersList.add(new User(cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getLong(cursor.getColumnIndex("usermain"))));
            cursor.moveToNext();
        }
        cursor.close();
        return followersList;
    }
    public int getNbFollowers(){
        String req = "SELECT * FROM followers";
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        return cursor.getCount();
    }

    public int getNbFollow(){
        String req = "SELECT * FROM follow";
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        return cursor.getCount();
    }
    public User getUser(CurrentUser u) {
        CurrentUser user = null;
        String req = "SELECT * FROM user WHERE id =" + u.getId();
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                user = new CurrentUser(cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return user;
    }

    public void insertFollow(User user){
        String req = "INSERT INTO follow VALUES("
            + user.getId() + ", '"
            + user.getUserName() + "'," + user.getUserMain() + ");";
        getWritableDatabase().execSQL(req);
    }
    public void insertFollowers(User user){
        String req = "INSERT INTO followers VALUES ("
                + user.getId() + ", '"
                + user.getUserName() + "',"
                + user.getUserMain() + ");";
        getWritableDatabase().execSQL(req);
    }
    public void insertUser(CurrentUser user){
        String req = "INSERT INTO user VALUES ("
                + user.getId() + ", '"
                + user.getUserName()
                + "',"
                + user.getUserMain() + ");";
        getWritableDatabase().execSQL(req);
    }
    public void deleteFollow(User user){
        String req = "DELETE FROM follow WHERE id="
                + user.getId() + " AND usermain=" + user.getUserMain();
        getReadableDatabase().execSQL(req);
    }
    public void deleteFollowers(User user){
        String req = "DELETE FROM followers WHERE id="
                + user.getId();
        getReadableDatabase().execSQL(req);
    }
    public void reset(){
        this.dropAll(getReadableDatabase());
        this.creatTable(getReadableDatabase());
    }
}
