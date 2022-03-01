package com.app.blogapp.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "blogs.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Blogs";
    public static final String ID = "Id";
    public static final String BLOG_TITLE = "Title";
    public static final String BLOG_DESCRIPTION = "Description";
    public static final String BLOG_PUBLISHED_DATE = "PublishedDate";
    public static final String BLOG_IMAGE_PATH = "ImagePath";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_NAME + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BLOG_TITLE + " TEXT, " +
                        BLOG_DESCRIPTION + " TEXT, " +
                        BLOG_PUBLISHED_DATE + " TEXT, " +
                        BLOG_IMAGE_PATH + " TEXT"+
                    ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_NAME
        );
    }

    public Cursor fetchAllData(SQLiteDatabase sqLiteDatabase) {
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME,
                null
        );
    }

    public void addImage(SQLiteDatabase sqLiteDatabase, String title, String description, String path) {
        String publishedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(new Date());
        ContentValues values = new ContentValues();
        values.put(BLOG_TITLE, title);
        values.put(BLOG_DESCRIPTION, description);
        values.put(BLOG_PUBLISHED_DATE, publishedDate);
        values.put(BLOG_IMAGE_PATH, path);
        sqLiteDatabase.insert(TABLE_NAME,null ,values);
    }
    public void updateImage(SQLiteDatabase sqLiteDatabase, int id, String title, String description, String path, String date) {
        ContentValues values = new ContentValues();
        values.put(BLOG_TITLE, title);
        values.put(BLOG_DESCRIPTION, description);
        values.put(BLOG_PUBLISHED_DATE, date);
        values.put(BLOG_IMAGE_PATH, path);
        sqLiteDatabase.update(TABLE_NAME, values, ID +"=?", new String[] {String.valueOf(id)});
    }
}
