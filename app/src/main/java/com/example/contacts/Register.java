package com.example.contacts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.Adapters.ContactsViewAdapter;
import com.example.contacts.Models.ContactModel;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    Button add, delete, update,view;
    EditText phone,name;
    DataBseHandler DB;
    RecyclerView contactDisplayRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar myToolbar = findViewById(R.id.homeActivityToolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        final DBHelper helper = new DBHelper(this);

        name = findViewById(R.id.name );
        phone= findViewById(R.id.phone );
        add= findViewById(R.id.add);
        delete = findViewById(R.id.delete );
        update = findViewById(R.id.update);
        view = findViewById(R.id.view );
        DB = new DataBseHandler(this );

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneTXT = phone.getText().toString();
                String nameTXT = name.getText().toString();

                boolean isInserted = helper.insertIntoContacts(nameTXT, phoneTXT);

//                Boolean checkinsertdata = DB.insertuserdata(phoneTXT,nameTXT);

                if(isInserted)
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

                ArrayList<ContactModel> contacts = helper.getContactsList();

                try {
                    System.out.println("---------------------------------");
                    System.out.println(contacts.get(0).getPhoneNumber());
                    System.out.println("---------------------------------");
                }catch (Exception e){}


                ContactsViewAdapter adapter = new ContactsViewAdapter( Register.this);

//                binding.contactDisplayRecyclerView.setAdapter(adapter);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(Register.this);
//                binding.contactDisplayRecyclerView.setLayoutManager(layoutManager);


                if(contacts.isEmpty()){
                    Toast.makeText(Register.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder buffer = new StringBuilder();

                for (int i=0; i<contacts.size(); i++){
                    buffer.append("Phone :"+contacts.get(i).getPhoneNumber()+"\n");
                    buffer.append("Name :"+contacts.get(i).getContactName()+"\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setCancelable(true);
                builder.setTitle("Queries Answered");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}