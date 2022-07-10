package com.example.contacts;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.Models.ContactModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_FINE_LOCATION_PERMISSION_CODE = 100;

    TextView longitudeTextView, latitudeTextView;
    Button addcontact;
    Button emergencyBtn;
    private double longitude ;
    private double latitude;
    private String url = "https://www.google.com/maps/search/?api=1&"+longitude+","+latitude;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
//                    getLocationBtn.setEnabled(true);

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    private void handleNsetLocation (){

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                    Manifest.permission.SEND_SMS);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION);

        }
        else {

//            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                longitudeTextView.setText(String.valueOf(longitude));
                                latitudeTextView.setText(String.valueOf(latitude));

                                System.out.println();
                                Toast.makeText(MainActivity.this, " location ", Toast.LENGTH_SHORT).show();

                                System.out.println("----------------------------");
                                System.out.println(latitude);
                                System.out.println(longitude);
                                System.out.println("----------------------------");
                                Toast.makeText(MainActivity.this, latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                                // Logic to handle location object
                            } else {
                                Toast.makeText(MainActivity.this, "location null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateLocation(){
        handleNsetLocation();
        url = "https://www.google.com/maps/search/?api=1&"+longitude+","+latitude;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(MainActivity.this, DisplayContactsActivity.class));

        Toolbar myToolbar = findViewById(R.id.homeActivityToolbar);
        setSupportActionBar(myToolbar);

        final DBHelper helper = new DBHelper(this);

        handleNsetLocation();


        emergencyBtn = findViewById(R.id.emergencyBtn);
//        addcontact=(Button) findViewById(R.id.addcontact);

        longitudeTextView = findViewById(R.id.longitudeTextView);
        latitudeTextView = findViewById(R.id.latitudeTextView);

        SmsManager smsManager = SmsManager.getDefault();

//        addcontact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(MainActivity.this,Register.class);
//                startActivity(intent);
//            }
//        });

        emergencyBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ArrayList<String> cellNo = new ArrayList<>();

                ArrayList<ContactModel> contacts = helper.getContactsList();

                for (int i=0; i<contacts.size(); i++){
//                    cellNo.add(contacts.get(i).getPhoneNumber());
                    smsManager.sendTextMessage(contacts.get(i).getPhoneNumber(), null,"hello", null, null);
                }

                System.out.println(cellNo);

                updateLocation();
//                smsManager.sendTextMessage("+91 98 804 38 931", null, String.valueOf(longitude)+"-"+String.valueOf(latitude)+"\n"+"hello from safety app !", null, null);
//                for(String s : cellNo){
//                    smsManager.sendTextMessage(s, null,"hello", null, null);
//                }

//                smsManager.sendTextMessage("+91 98 804 38 931", null,url+"\nEmergency\nMy last known location.", null, null);

//                smsManager.sendTextMessage("+91 9845842582", null,url+"\nEmergency\nMy last known location.", null, null);
                return false;
            }
        });

//        emergencyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ArrayList<String> cellNo = new ArrayList<>();
//
//                ArrayList<ContactModel> contacts = helper.getContactsList();
//
//                for (int i=0; i<contacts.size(); i++){
//                   cellNo.add(contacts.get(i).getPhoneNumber());
//                }
//
//                System.out.println(cellNo);
//
//                updateLocation();
////                smsManager.sendTextMessage("+91 98 804 38 931", null, String.valueOf(longitude)+"-"+String.valueOf(latitude)+"\n"+"hello from safety app !", null, null);
////                for(String s : cellNo){
////                    smsManager.sendTextMessage(s, null,"hello", null, null);
////                }
//
////                smsManager.sendTextMessage("+91 98 804 38 931", null,url+"\nEmergency\nMy last known location.", null, null);
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
            {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            }

            case R.id.action_manage_contacts:

            {
                startActivity(new Intent(MainActivity.this, DisplayContactsActivity.class));
                break;
            }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.

        }
        return super.onOptionsItemSelected(item);
    }
}