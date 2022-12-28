package com.example.finalprojectmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.finalprojectmobile.models.Item;

import java.util.ArrayList;

public class ItemDB {
    private DBHelper dbHelper;

    public ItemDB(Context context){
        this.dbHelper = DBHelper.getInstance(context);
    }

    // Create
    public int insertItem(Item item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_ITEM_NAME, item.getName());
        cv.put(DBHelper.FIELD_ITEM_DESC, item.getDescription());
        cv.put(DBHelper.FIELD_ITEM_QTY, item.getQuantity());
        cv.put(DBHelper.FIELD_ITEM_IMAGE, item.getImage());
        cv.put(DBHelper.FIELD_FOREIGN_USER_ID, item.getUserId());
        int insertedId = (int)db.insert(DBHelper.TABLE_ITEM, null, cv);
        db.close();
        return insertedId;
    }

    // Read
    public ArrayList<Item> getAllItems(String uid){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                String.format("SELECT * FROM %s WHERE user_id=?",
                        DBHelper.TABLE_ITEM)
                , new String[]{uid});
        ArrayList<Item> itemsArrayList= new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_FOREIGN_USER_ID));
                // Item
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_DESC));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_QTY));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ITEM_IMAGE));

                itemsArrayList.add(new Item(itemId, userId, name, description, quantity, image));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return itemsArrayList;
    }

    // Update
    public int updateItem(Item item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_ITEM_NAME, item.getName());
        cv.put(DBHelper.FIELD_ITEM_DESC, item.getDescription());
        cv.put(DBHelper.FIELD_ITEM_QTY, item.getQuantity());
        cv.put(DBHelper.FIELD_ITEM_IMAGE, item.getImage());
        int updatedRows = db.update(DBHelper.TABLE_ITEM, cv, DBHelper.FIELD_ITEM_ID + "=?", new String[]{String.valueOf(item.getId())});
        db.close();
        return updatedRows;
    }

    public int deleteItem(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(DBHelper.TABLE_ITEM, DBHelper.FIELD_ITEM_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }
}
