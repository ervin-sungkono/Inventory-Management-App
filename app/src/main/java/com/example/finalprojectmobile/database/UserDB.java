package com.example.finalprojectmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalprojectmobile.models.User;

import java.util.ArrayList;

public class UserDB {
    private DBHelper dbHelper;

    public UserDB(Context context){
        this.dbHelper = new DBHelper(context);
    }

    // Create
    public void insertUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_USER_ID, user.getId());
        cv.put(DBHelper.FIELD_USER_NAME, user.getUsername());
        cv.put(DBHelper.FIELD_USER_EMAIL, user.getEmail());
        cv.put(DBHelper.FIELD_USER_PROFILE_PIC, user.getProfilePic());
        int id = (int) db.insertWithOnConflict(DBHelper.TABLE_USER, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        if(id == -1){
            db.update(DBHelper.TABLE_USER, cv, "id=?", new String[]{user.getId()});
        }
        db.close();
    }

    //Read
    public User findUser(String id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                String.format("SELECT * FROM %s WHERE id=?",
                    DBHelper.TABLE_USER),
                new String[]{id});
        User user = null;
        if(cursor.moveToFirst()){
            String userId = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_EMAIL));
            byte[] profilePic = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_PROFILE_PIC));
            user = new User(userId, username, email, profilePic);
        }
        cursor.close();
        db.close();
        return user;
    }

    // Update
    public int updateUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_USER_NAME, user.getUsername());
        cv.put(DBHelper.FIELD_USER_EMAIL, user.getEmail());
        cv.put(DBHelper.FIELD_USER_PROFILE_PIC, user.getProfilePic());
        int updatedRows = db.update(DBHelper.TABLE_USER, cv, "id=?", new String[]{user.getId()});
        db.close();
        return updatedRows;
    }
}
