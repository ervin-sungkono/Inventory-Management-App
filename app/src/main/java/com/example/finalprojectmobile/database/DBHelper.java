package com.example.finalprojectmobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    // Database Configuration
    private static final String DB_NAME = "inventoryDB";
    private static final int DB_VERSION = 3;

    // Table Names
    public static final String TABLE_USER = "users";
    public static final String TABLE_ITEM = "items";

    // User Table Columns
    public static final String FIELD_USER_ID = "id";
    public static final String FIELD_USER_NAME = "username";
    public static final String FIELD_USER_EMAIL = "email";
    public static final String FIELD_USER_PROFILE_PIC = "profile_pic";

    // Item Table Columns
    public static final String FIELD_ITEM_ID = "id";
    public static final String FIELD_FOREIGN_USER_ID = "user_id";
    public static final String FIELD_ITEM_NAME = "name";
    public static final String FIELD_ITEM_DESC = "description";
    public static final String FIELD_ITEM_QTY = "quantity";
    public static final String FIELD_ITEM_IMAGE = "image";

    // Create Table Query
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
                FIELD_USER_ID + " TEXT PRIMARY KEY," +
                FIELD_USER_NAME + " TEXT UNIQUE," +
                FIELD_USER_EMAIL + " TEXT UNIQUE," +
                FIELD_USER_PROFILE_PIC + " BLOB)";
    private static final String CREATE_TABLE_ITEM =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM + "(" +
                FIELD_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FIELD_FOREIGN_USER_ID + " TEXT REFERENCES " + TABLE_USER + "," +
                FIELD_ITEM_NAME + " TEXT," +
                FIELD_ITEM_DESC + " TEXT," +
                FIELD_ITEM_QTY + " INTEGER," +
                FIELD_ITEM_IMAGE + " BLOB)";

    // Drop Table Query
    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_USER;
    private static final String DROP_TABLE_ITEM = "DROP TABLE IF EXISTS " + TABLE_ITEM;

    private static DBHelper dbInstance;

    public static synchronized DBHelper getInstance(Context context){
        // Singleton Pattern
        if(dbInstance == null){
            dbInstance = new DBHelper(context.getApplicationContext());
        }
        return dbInstance;
    }

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_ITEM);
        onCreate(db);
    }
}
