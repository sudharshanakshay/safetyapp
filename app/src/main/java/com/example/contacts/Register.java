package com.example.contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    Button add, delete, update,view;
    EditText phone,name;
    DataBseHandler DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name );
        phone= findViewById(R.id.phone );
        add= findViewById(R.id.add);
        delete = findViewById(R.id.delete );update = findViewById(R.id.update );
        view = findViewById(R.id.view );
        DB = new DataBseHandler(this );
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneTXT = phone.getText().toString();
                String nameTXT = name.getText().toString();



                Boolean checkinsertdata = DB.insertuserdata(phoneTXT,nameTXT);
                if(checkinsertdata==true)
                    Toast.makeText(Register.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneTXT = phone.getText().toString();
                String nameTXT = name.getText().toString();
                Boolean checkupdatedata = DB.updateuserdata(phoneTXT,nameTXT);
                if(checkupdatedata==true)
                    Toast.makeText(Register.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register.this, "New Entry Not Updated", Toast.LENGTH_SHORT).show();
            }        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneTXT = phone.getText().toString();
                Boolean checkudeletedata = DB.deletedata(phoneTXT);
                if(checkudeletedata==true)
                    Toast.makeText(Register.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
            }        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userTXT = phone.getText().toString();
                Cursor res = DB.getdata(userTXT);
                if(res.getCount()==0){
                    Toast.makeText(Register.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Phone :"+res.getString(0)+"\n");
                    buffer.append("Name :"+res.getString(1)+"\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setCancelable(true);
                builder.setTitle("Queries Answered");
                builder.setMessage(buffer.toString());
                builder.show();
            }        });
    }
}