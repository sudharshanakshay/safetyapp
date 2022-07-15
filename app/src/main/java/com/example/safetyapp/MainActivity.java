package com.example.safetyapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safetyapp.R;
import com.example.safetyapp.Models.ContactModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    TextView longitudeTextView, latitudeTextView, displayTimer;
    Button emergencyBtn, cancelSendSMSBtn;

    private int counter = 0;

    Handler mainHandler = new Handler();

    boolean running = false;
    ArrayList<ContactModel> contacts;

    NewThread sendSMSThread = new NewThread();
    SmsManager smsManager = SmsManager.getDefault();

    class NewThread extends Thread {
        @Override
        public void run() {
            while (running) {
                mainHandler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        displayTimer.setText("Sending SMS in " + String.valueOf(counter));
                        if (!(counter <= 0)) counter--;
                        else {
                            for (int i = 0; i < contacts.size(); i++) {
//                              System.out.println(contacts.get(i).getPhoneNumber());
                                try {
                                    smsManager.sendTextMessage(contacts.get(i).getPhoneNumber(), null, url + "\nEmergency\nMy last known location.", null, null);
                                }
                                catch (Exception e){
                                    System.out.println("------------send SMS error--------------");
                                    System.out.println(e);
                                    System.out.println("------------send SMS error--------------");
                                    initiatePermissionRequest();
                                }
                            }
                            running = false;
                        }
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            Toast.makeText(MainActivity.this, "SMS sent", Toast.LENGTH_SHORT).show();
        }

    }

    private static final int ACCESS_FINE_LOCATION_PERMISSION_CODE = 100;
    private double longitude;
    private double latitude;
    private String url = "https://www.google.com/maps/search/?api=1&" + longitude + "," + latitude;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app
//                    getLocationBtn.setEnabled(true);

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    private boolean hasPermission(String[] permissions){

             for (String permission : permissions) {
                 if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) return false;
             }

             return true;

    }

    private void initiatePermissionRequest() {

        int permissionsCode = 51;
        String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermission(permissions)) ActivityCompat.requestPermissions(this, permissions, permissionsCode);

//
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        ){
//            System.out.println("from initialpermission, about to request all required permission !");
//            requestPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE);
//            requestPermissionLauncher.launch(
//                    Manifest.permission.ACCESS_COARSE_LOCATION);
//            requestPermissionLauncher.launch(
//                    Manifest.permission.ACCESS_FINE_LOCATION);
//            requestPermissionLauncher.launch(
//                    Manifest.permission.SEND_SMS);
//        }


//        if () {
//            requestPermissionLauncher.launch(
//                    Manifest.permission.SEND_SMS);
//        }
//
//        if ( && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissionLauncher.launch(
//                    Manifest.permission.ACCESS_COARSE_LOCATION);
//            requestPermissionLauncher.launch(
//                    Manifest.permission.ACCESS_FINE_LOCATION);
//        }

    }

    private void updateLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            initiatePermissionRequest();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            url = "https://www.google.com/maps/search/?api=1&"+longitude+","+latitude;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.homeActivityToolbar);
        setSupportActionBar(myToolbar);

        initiatePermissionRequest();

        final DBHelper helper = new DBHelper(this);

        updateLocation();

        emergencyBtn = findViewById(R.id.emergencyBtn);
        cancelSendSMSBtn = findViewById(R.id.cancelSendSMSBtn);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        displayTimer = findViewById(R.id.displayTimer);

        if(counter == 0){
            displayTimer.setVisibility(View.GONE);
            cancelSendSMSBtn.setVisibility(View.GONE);
        }

        emergencyBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                updateLocation();
                contacts = helper.getContactsList();

                displayTimer.setVisibility(View.VISIBLE);
                cancelSendSMSBtn.setVisibility(View.VISIBLE);

                counter = 5;
                running =  true;
                if(sendSMSThread.isAlive());
                else{
                    sendSMSThread = new NewThread();
                    sendSMSThread.start();
                }
                return false;
            }
        });

        cancelSendSMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = false;
                displayTimer.setVisibility(View.GONE);
                cancelSendSMSBtn.setVisibility(View.GONE);
                sendSMSThread.interrupt();
            }
        });
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
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
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