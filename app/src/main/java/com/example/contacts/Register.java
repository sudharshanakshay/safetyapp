package com.example.contacts;

import static android.provider.ContactsContract.Contacts.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class Register extends AppCompatActivity {
    Button add, delete, view;
    EditText phone, name;
    DataBseHandler DB;
//    RecyclerView recyclerView;
//    ArrayList<ContactModel>arrayList = new ArrayList<ContactModel>();
//    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //recyclerView = findViewById(R.id.RVlist);

        checkPermission();


        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
//        update = findViewById(R.id.update);
        view = findViewById(R.id.view);
        DB = new DataBseHandler(this);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneTXT = phone.getText().toString();
                String nameTXT = name.getText().toString();

                Boolean checkinsertdata = DB.insertuserdata(phoneTXT, nameTXT);
                if (checkinsertdata == true)

                    Toast.makeText(Register.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }

        });


//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String phoneTXT = phone.getText().toString();
//                String nameTXT = name.getText().toString();
//                Boolean checkupdatedata = DB.updateuserdata(phoneTXT, nameTXT);
//                if (checkupdatedata == true)
//                    Toast.makeText(Register.this, "Entry Updated", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(Register.this, "New Entry Not Updated", Toast.LENGTH_SHORT).show();
//            }
//        });
//

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneTXT = phone.getText().toString();
                Boolean checkudeletedata = DB.deletedata(phoneTXT);
                if (checkudeletedata == true)
                    Toast.makeText(Register.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userTXT = phone.getText().toString();
                Cursor res = DB.getdata(userTXT);
                if (res.getCount() == 0) {
                    Toast.makeText(Register.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Phone :" + res.getString(0) + "\n");
                    buffer.append("Name :" + res.getString(1) + "\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setCancelable(true);
                builder.setTitle("Queries Answered");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);

        } else {
            getContactList();
        }
    }

    private void getContactList() {
        Uri uri = CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "ASC";
        Cursor cursor = getContentResolver().query(uri, null, null, null, sort);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(
                        _ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
                Cursor phoneCursor = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);
                if (phoneCursor.moveToNext()) {
                    @SuppressLint("Range") String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));
                }
            }
        }
    }

}

//ContactModel model= new ContactModel();
//model.setName(name);
//model.setPhone(phone);
//arrayList.add(model);
//phoneCursor.close();
//
//       }
//        }
//        cursor.close();
//    }
//    recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    adapter = new MainAdapter(this,arrayList);
//    recyclerView.setAdapter(adapter);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    if(requestCode == 100 && grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//getContactList();
//    }else {
//        Toast.makeText(Register.this,"Permission denied",Toast.LENGTH_SHORT).show();
//   checkPermission();
//
//    }
//
//
//
//    }
//}
