package com.example.finalprojectmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalprojectmobile.models.Item;
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
        cv.put(DBHelper.FIELD_USER_NAME, user.getUsername());
        cv.put(DBHelper.FIELD_USER_EMAIL, user.getEmail());
        cv.put(DBHelper.FIELD_USER_PROFILE_PIC, user.getProfilePic());
        db.insert(DBHelper.TABLE_USER, null, cv);
        db.close();
    }

    //Read
    public ArrayList<User> getAllUsers(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                String.format("SELECT * FROM %s", DBHelper.TABLE_USER)
                , null);
        ArrayList<User> usersArrayList= new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_EMAIL));
                byte[] profilePic = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_PROFILE_PIC));
                User user = new User(userId, username, email, profilePic);

                usersArrayList.add(user);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return usersArrayList;
    }

    // Update
    public void updateUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_USER_NAME, user.getUsername());
        cv.put(DBHelper.FIELD_USER_EMAIL, user.getEmail());
        cv.put(DBHelper.FIELD_USER_PROFILE_PIC, user.getProfilePic());
        db.update(DBHelper.TABLE_ITEM, cv, "id=?", new String[]{String.valueOf(user.getId())});
        db.close();
    }
}
