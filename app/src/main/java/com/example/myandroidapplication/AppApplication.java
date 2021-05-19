package com.example.myandroidapplication;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }


}
