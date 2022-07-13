package com.example.contacts;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;
import android.database.sqlite.SQLiteDatabase;

import com.example.contacts.Models.ContactModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    final static String DBNAME = "mydatabase.db";
    final static int DBVERSION = 8;


    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table contacts" +
                        "(id INTEGER primary key autoincrement," +
                        "name TEXT," +
                        "phone TEXT )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP table if exists orders");
        onCreate(sqLiteDatabase);
    }

    public boolean insertIntoContacts(String name, String phone) {
        SQLiteDatabase database = getReadableDatabase();

        ContentValues values = new ContentValues();


        if(name.length() >= 1 && phone.length() >= 1){

            values.put("name",name);
            values.put("phone",phone);
            long id = database.insert("contacts",null,values);
            return id > 0;

        }
        else if(name.length() == 0 && phone.length() >= 1){
            values.put("name","--no name--");
            values.put("phone",phone);
            long id = database.insert("contacts",null,values);
            return id > 0;
        }
        else {
            return false;
        }

    }

    public ArrayList<ContactModel> getContactsList() {
        ArrayList<ContactModel> contacts = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();

        String[] columnNames = {"id", "name", "phone"};

        Cursor cursor = database.query("contacts", columnNames, null,null, null,null,"name" );

        System.out.println("---------------------------");
        System.out.println(cursor.getCount());
        System.out.println("---------------------------");

        if(cursor.moveToFirst()) {
            do {
                System.out.println(cursor.getString(2));
                ContactModel model = new ContactModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                contacts.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return contacts;
    }

    public int deleteContact(ContactModel contactModel){
        SQLiteDatabase database = this.getWritableDatabase();

        int a = database.delete("contacts", "id=?", new String[] {String.valueOf(contactModel.getContactId())} );

        System.out.println(a);

        database.close();
        return 0;
    }
}