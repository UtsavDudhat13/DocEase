package com.example.miniproject.manager;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class AppControl extends Application {

    public static AppControl context;
    public static AppControl getContext() {
        return context;
    }
    public static FirebaseAuth mAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        PreferenceManager.init();
        mAuth = FirebaseAuth.getInstance();
    }
}
