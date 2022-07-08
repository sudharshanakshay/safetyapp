package com.example.contacts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.example.contacts.Adapters.ContactsViewAdapter;
import com.example.contacts.Models.ContactModel;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar myToolbar = findViewById(R.id.contactActivityToolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        final DBHelper helper = new DBHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewContacts);

        ArrayList<ContactModel> contacts = helper.getContactsList();

        ContactsViewAdapter adapter = new ContactsViewAdapter(contacts, ContactsActivity.this);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ContactsActivity.this);
        recyclerView.setLayoutManager(layoutManager);


    }
}