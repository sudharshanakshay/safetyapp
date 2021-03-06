package com.example.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.safetyapp.Models.ContactModel;

public class SaveContactActivity extends AppCompatActivity {
    private DBHelper helper;
    EditText name, phone;
    String contactID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_contact);

        Toolbar myToolbar = findViewById(R.id.saveContactActivityToolBar);
        setSupportActionBar(myToolbar);

        helper = new DBHelper(this);

        name = findViewById(R.id.name );
        phone= findViewById(R.id.phone );

        if(getIntent().hasExtra("contactID")){
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    contactID= null;
                } else {
                    contactID= extras.getString("contactID");
                }
            } else {
                contactID= (String) savedInstanceState.getSerializable("contactID");
            }

            ContactModel contact = helper.getXcontact(contactID);

            name.setText(contact.getContactName());
            phone.setText(contact.getPhoneNumber());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_contact) {// User chose the "Favorite" action, mark the current item
            // as a favorite...
            if(name.getText().toString().equals("")){
                Toast.makeText(this, "setting name as --no-name--", Toast.LENGTH_SHORT).show();
            }
            try {
                if(!phone.getText().toString().equals("")) {

//                    -------------- save contact --------------
                    if(contactID == null) helper.insertIntoContacts(name.getText().toString(), phone.getText().toString());

//                    -------------- update contact --------------
                    else helper.updateXcontact(contactID, name.getText().toString(), phone.getText().toString());
                    Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();

                    onBackPressed();
                }else{
                    Toast.makeText(this, "Please add Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}