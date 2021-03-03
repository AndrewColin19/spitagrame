package com.spitagram.Modele;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spitagram.Modele.InstagramApi.Users.User;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "spitagramDataBase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USER = "CREATE TABLE User ("
            + " id int NOT NULL primary key ,"
            + " username text NOT NULL "
            + ")";
    private static final String CREATE_TABLE_FOLLOW = "CREATE TABLE follow ("
            + " id int NOT NULL primary key ,"
            + " username text NOT NULL "
            + ")";
    private static final String CREATE_TABLE_FOLLOWERS = "CREATE TABLE followers ("
            + " id int NOT NULL primary key ,"
            + " username text NOT NULL "
            + ")";

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
    public ArrayList<User> getAllFollow(){
        ArrayList<User> followList = new ArrayList<>();
        String req = "SELECT * FROM follow";
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            followList.add(new User(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username"))));
            cursor.moveToNext();
        }
        return followList;
    }

    public ArrayList<User> getAllFollowers(){
        ArrayList<User> followersList = new ArrayList<>();
        String req = "SELECT * FROM followers";
        Cursor cursor = this.getReadableDatabase().rawQuery(req, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            followersList.add(new User(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username"))));
            cursor.moveToNext();
        }
        return followersList;
    }

    public void insertFollow(User user){
        String req = "INSERT INTO VALEURS follow ("
            + user.getId() + ", "
            + user.getUserName() + ";";
        getWritableDatabase().execSQL(req);
    }
    public void insertFollowers(User user){
        String req = "INSERT INTO VALEURS followers ("
                + user.getId() + ", "
                + user.getUserName() + ";";
        getWritableDatabase().execSQL(req);
    }
    public void deleteFollow(User user){
        String req = "DELETE FROM follow WHERE id="
                + user.getId();
        getReadableDatabase().execSQL(req);
    }
    public void deleteFollowers(User user){
        String req = "DELETE FROM followers WHERE id="
                + user.getId();
        getReadableDatabase().execSQL(req);
    }
}
