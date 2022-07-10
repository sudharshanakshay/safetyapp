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


//        Cursor cursor = database.rawQuery("Select id, name, phone from contacts ORDER BY name", null);

        if(cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                ContactModel model = new ContactModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
//                model.setContactId(cursor.getInt(0));
//                model.setContactName(cursor.getString(1));
//                model.setPhoneNumber(cursor.getString(2));
                contacts.add(model);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }

    public int deleteAll(){
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery("DELETE FROM contacts", null);
        cursor.close();
        database.close();
        return 1;
    }

    public int deleteContact(ContactModel contactModel){
        SQLiteDatabase database = this.getWritableDatabase();


//        System.out.println("---------------------------------");
//        System.out.println(array[0]);
//        System.out.println("---------------------------------");

        int a = database.delete("contacts", "id=?", new String[] {String.valueOf(contactModel.getContactId())} );

        System.out.println(a);


//        Cursor cursor = database.rawQuery("DELETE FROM contacts WHERE phone='"+id+"'", null);

//        cursor.close();
        database.close();
        return 0;
    }

    public Cursor getOrderById(int id) {
//        ArrayList<OrdersModel> orders = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from orders where id =="+id, null);
//        if(cursor.moveToFirst()) {
//            while(cursor.moveToNext()) {
//                OrdersModel model = new OrdersModel();
//                model.setOrderNumber(cursor.getInt(0)+ "");
//                model.setSoldItemName(cursor.getString(1));
//                model.setOrderImage(cursor.getInt(2));
//                model.setPrice(cursor.getInt(3) + "");
//                orders.add(model);
//            }
//        }
        if(cursor !=null)
            cursor.moveToFirst();
//        cursor.close();
//        database.close();
        return cursor;
    }


    public boolean updateOrder(String name,String phone,int price,int image,String desc,String foodName,int quantity,int id) {
        SQLiteDatabase database = getReadableDatabase();
        ContentValues values =new ContentValues();

        /*
        id = 0
        name = 1
        phone =2
        price =3
        image = 4
        desc = 5
        foodname = 6
        quantity = 7
         */
        values.put("name",name);
        values.put("phone",phone);
        values.put("price",price);
        values.put("image",image);

        values.put("description",desc);
        values.put("foodname",foodName);
        values.put("quantity",quantity);

        long row = database.update("orders",values,"id="+id,null);
        if(row <=0){
            return false;
        }
        else {
            return true;
        }

    }

    public int deleteOrder(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete("orders","id="+id,null);

    }
}