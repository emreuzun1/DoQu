package com.example.doqu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "DoQu";
    private static final int DB_VERSION = 10;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USERS ("
                + "NAME TEXT,"
                + "USERNAME TEXT PRIMARY KEY,"
                + "PASSWORD TEXT)");

        db.execSQL("CREATE TABLE POSTS("
                + "POSTID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "TITLE TEXT,"
                + "USER TEXT,"
                + "DESCRIPTION TEXT,"
                + "LIKES INTEGER)");

        db.execSQL("CREATE TABLE COMMENTS("
                + "COMMENTID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "POSTID INTEGER,"
                + "USERNAME TEXT,"
                + "COMMENT TEXT)");

        db.execSQL("CREATE TABLE PROFILE("
                + "USERNAME TEXT PRIMARY KEY,"
                + "NAME TEXT)");
    }

    public boolean insertUser(SQLiteDatabase db, String name, String username, String password) {
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("USERNAME", username);
        values.put("PASSWORD", password);
        long result = -1;
        if (!findUser(username, password)) {
            result = db.insert("USERS", null, values);
            if (result == -1) {
                return false;
            }
            ContentValues values1 = new ContentValues();
            values1.put("NAME", name);
            values1.put("USERNAME", username);
            result = db.insert("PROFILE", null, values1);
        }
        return result == -1 ? false : true;
    }

    public boolean findUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from USERS where USERNAME = ? and PASSWORD = ?", new String[]{username, password});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{username};

        long result = db.delete("USERS", "USERNAME = ?",args);
        result = db.delete("POSTS","USER = ?", args);
        result = db.delete("COMMENTS","USERNAME = ?", args);
        result = db.delete("PROFILE","USERNAME = ?", args);
        db.close();
        return result == -1 ? false : true;
    }

    public boolean deleteQuestions(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{username};

        long result = db.delete("COMMENTS","USERNAME = ?", args);
        result = db.delete("POSTS", "USER = ?", args);
        db.close();
        return result == -1 ? false : true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS COMMENTS");
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS POSTS");
        db.execSQL("DROP TABLE IF EXISTS PROFILE");
        onCreate(db);
    }

    public Cursor getPosts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from POSTS", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return cursor;
            }
        }
        return null;
    }

    public Cursor getProfile(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from PROFILE where USERNAME = ?", new String[]{username});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return cursor;
            }
        }
        return null;
    }

    public Cursor getProfilePost(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from POSTS where USER = ?", new String[]{username});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return cursor;
            }
        }
        return null;
    }

    public boolean addPost(SQLiteDatabase db, String user, String title, String description) {
        ContentValues values = new ContentValues();
        values.put("USER", user);
        values.put("DESCRIPTION", description);
        values.put("TITLE", title);
        long result = db.insert("POSTS", null, values);
        if (result != -1) {
            return true;
        }
        return false;
    }

    public boolean insertComment(SQLiteDatabase db, int postId, String username, String comment) {
        ContentValues values = new ContentValues();
        values.put("POSTID", postId);
        values.put("USERNAME", username);
        values.put("COMMENT", comment);
        long result = db.insert("COMMENTS", null, values);
        if (result != -1) {
            return true;
        }
        return false;
    }

    public Cursor getComments(SQLiteDatabase db, int postId) {
        Cursor cursor = db.rawQuery("Select * from COMMENTS where POSTID = ?", new String[]{String.valueOf(postId)});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return cursor;
            }
        }
        return null;
    }

    public void likePost(SQLiteDatabase db, int id, int newCount) {
        System.out.println(id);
        String query = "UPDATE POSTS SET LIKES = " + newCount + " WHERE POSTID = " + id;
        db.execSQL(query);
    }
}
