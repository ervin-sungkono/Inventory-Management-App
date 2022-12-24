package com.example.finalprojectmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalprojectmobile.models.Item;
import com.example.finalprojectmobile.models.User;

import java.util.ArrayList;

public class ItemDB {
    private DBHelper dbHelper;

    public ItemDB(Context context){
        this.dbHelper = DBHelper.getInstance(context);
    }

    // Create
    public void insertItem(Item item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_ITEM_NAME, item.getName());
        cv.put(DBHelper.FIELD_ITEM_DESC, item.getDescription());
        cv.put(DBHelper.FIELD_ITEM_QTY, item.getQuantity());
        cv.put(DBHelper.FIELD_ITEM_IMAGE, item.getImage());
        db.insert(DBHelper.TABLE_ITEM, null, cv);
        db.close();
    }

    // Read
    public ArrayList<Item> getAllItems(String id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                String.format("SELECT * FROM %s JOIN %s ON %s = %s WHERE user_id=?",
                        DBHelper.TABLE_ITEM,
                        DBHelper.TABLE_USER,
                        DBHelper.TABLE_USER + "." + DBHelper.FIELD_USER_ID,
                        DBHelper.TABLE_ITEM + "." + DBHelper.FIELD_FOREIGN_USER_ID)
                , new String[]{String.valueOf(id)});
        ArrayList<Item> itemsArrayList= new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // User
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_EMAIL));
                byte[] profilePic = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.FIELD_USER_PROFILE_PIC));
                User user = new User(userId, username, email, profilePic);

                // Item
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_DESC));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_QTY));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_IMAGE));

                itemsArrayList.add(new Item(itemId, user, name, description, quantity, image));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return itemsArrayList;
    }

    // Update
    public void updateItem(Item item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_ITEM_NAME, item.getName());
        cv.put(DBHelper.FIELD_ITEM_DESC, item.getDescription());
        cv.put(DBHelper.FIELD_ITEM_QTY, item.getQuantity());
        cv.put(DBHelper.FIELD_ITEM_IMAGE, item.getImage());
        db.update(DBHelper.TABLE_ITEM, cv, "id=?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public void deleteItem(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_ITEM, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
