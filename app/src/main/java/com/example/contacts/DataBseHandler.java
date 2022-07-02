package com.example.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBseHandler extends SQLiteOpenHelper {
    public DataBseHandler(Context context) {
        super(context, "project1.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table mylist_data(phone TEXT primary key,name TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists mylist_data");
    }

    public Boolean insertuserdata(String phone, String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone",phone);
        contentValues.put("name", name);
        long result = DB.insert("mylist_data", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateuserdata(String phone, String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        contentValues.put("name", name);
        Cursor cursor = DB.rawQuery("Select * from mylist_data where phone = ?", new String[]{phone});
        if (cursor.getCount() > 0) {
            long result = DB.update("mylist_data", contentValues, "phone=?", new String[]{phone});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deletedata (String phone)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from mylist_data where phone = ?", new String[]{phone});
        if (cursor.getCount() > 0) {
            long result = DB.delete("mylist_data", "phone= ?", new String[]{phone});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Cursor getdata (String phone)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from mylist_data where phone=?", new String[]{phone});
        return cursor;

    }
}

