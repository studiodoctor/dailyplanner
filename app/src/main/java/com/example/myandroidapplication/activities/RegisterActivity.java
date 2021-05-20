package com.example.myandroidapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myandroidapplication.R;
import com.example.myandroidapplication.activities.ui.CallAPIActivity;
import com.example.myandroidapplication.data.SharedPreferenceMaster;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    private EditText R_name, R_surname, R_cellNo, R_address, R_password;
    private Button B_reg;


    //declaring shared preferences
    private SharedPreferenceMaster sharedPreferenceMaster;

    //google client
    public static GoogleApiClient googleApiClient;

    //code for requesting location
    final static int REQUEST_LOCATION = 199;

    //firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        init();
        setListeners();
    }

    private void init () {

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

      // initializing shared preferences
        sharedPreferenceMaster = new SharedPreferenceMaster(RegisterActivity.this);

      R_name=findViewById(R.id.reg_name);
      R_surname=findViewById(R.id.reg_surname);
      R_cellNo=findViewById(R.id.reg_cellNo);
      R_address=findViewById(R.id.reg_address);
      R_password=findViewById(R.id.reg_password);
      B_reg=findViewById(R.id.btn_reg);

      getRunTimePermission();
    }

    private void validateFields() {
        if (R_name.getText().toString().trim().equals("")) {
            R_name.setError("Please enter your name..");
        } else if (R_surname.getText().toString().trim().equals("")) {
            R_surname.setError("Please enter your surname...");
        }else if (R_cellNo.getText().toString().trim().equals("")) {
            R_cellNo.setError("Please enter your mobile number...");
        } else if (R_cellNo.getText().length() > 10 || R_cellNo.getText().length() < 10) {
            R_cellNo.setError("Mobile number must be 10 characters...");
        } else if (R_address.getText().toString().trim().equals("")) {
            R_address.setError("Please enter your address...");
        } else if (R_password.getText().toString().trim().equals(""))  {
            R_password.setError("Please enter your password...");
        }
        else {
            registerUser(R_address.getText().toString().trim(), R_password.getText().toString().trim());
           
            openSecondActivity();
        }
    }

    private void setListeners() {

        B_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validateFields();
            }
        });
    }

    private void getRunTimePermission () {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report)
            {
                activateTheGPS();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
            {

            }

        }).check();
    }

    private void activateTheGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            //Timber.v("Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        RegisterActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        } else {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        RegisterActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    private void registerUser(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Registering user...");
        pd.show();

        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //startActivity(new Intent(RegisterActivity.this, SecondActivity.class));
                            Toast.makeText(RegisterActivity.this, "Authentication successful!!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void openSecondActivity(){

        sharedPreferenceMaster.setUserName(R_name);
        sharedPreferenceMaster.setUserSurname(R_surname);
        sharedPreferenceMaster.setUserCell(R_cellNo);
        sharedPreferenceMaster.setUserAddress(R_address);

        Intent intentSecondActivity = new Intent(RegisterActivity.this, CallAPIActivity.class);
        // Using Shared Preference Master to pass the values instead
//        intentSecondActivity.putExtra("Name", String.valueOf(R_name));
//        intentSecondActivity.putExtra("Surname", String.valueOf(R_surname));
//        intentSecondActivity.putExtra("Cell_Number", String.valueOf(R_cellNo));
//        intentSecondActivity.putExtra("Address", String.valueOf(R_address));

        startActivity(intentSecondActivity);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}