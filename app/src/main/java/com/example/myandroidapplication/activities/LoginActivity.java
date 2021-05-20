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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myandroidapplication.R;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    private EditText inputEmail, inputPasswd;
    private Button btn_login, btn_lnk_to_reg;
    private TextView txt_forgot_password;


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
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        init();
        setListeners();
    }

    private void init () {

        getRunTimePermission();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // initializing shared preferences
        sharedPreferenceMaster = new SharedPreferenceMaster(LoginActivity.this);

        inputEmail=findViewById(R.id.email_id);
        inputPasswd=findViewById(R.id.lgn_password);
        btn_login=findViewById(R.id.btn_login);
        btn_lnk_to_reg=findViewById(R.id.btn_lnk_to_reg);
        txt_forgot_password=findViewById(R.id.txt_forgot_password);
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
                                        LoginActivity.this, REQUEST_LOCATION);
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
                                        LoginActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    private void setListeners() {

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validate();
            }
        });

        btn_lnk_to_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    private void openSecondActivity(){

        //sharedPreferenceMaster.setUserName(R_name);
        //sharedPreferenceMaster.setUserSurname(R_surname);
        //sharedPreferenceMaster.setUserCell(R_cellNo);
        //sharedPreferenceMaster.setUserAddress(R_address);

        Intent intentSecondActivity = new Intent(LoginActivity.this, SecondActivity.class);
        // Using Shared Preference Master to pass the values instead
//        intentSecondActivity.putExtra("Name", String.valueOf(R_name));
//        intentSecondActivity.putExtra("Surname", String.valueOf(R_surname));
//        intentSecondActivity.putExtra("Cell_Number", String.valueOf(R_cellNo));
//        intentSecondActivity.putExtra("Address", String.valueOf(R_address));

        startActivity(intentSecondActivity);
    }

    private void validate() {
        if (inputEmail.getText().toString().trim().equals("")) {
            inputEmail.setError("Please enter your email address..");
        } else if (inputPasswd.getText().toString().trim().equals("")) {
            inputPasswd.setError("Please enter your password...");
        }
        else {
            doLogin(inputEmail.getText().toString().trim(), inputPasswd.getText().toString().trim());
        }
    }

    private void doLogin(String email, String password) {

        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Logging in the user");
        pd.show();

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        pd.dismiss();
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPasswd.setError("Password too short");
                            } else {
                                Toast.makeText(LoginActivity.this, "Auth Failed!!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                            openSecondActivity();
                        }
                    }
                });
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}