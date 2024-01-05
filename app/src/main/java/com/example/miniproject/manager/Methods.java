package com.example.miniproject.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.miniproject.R;

public class Methods {

//    public static String uid;


    public static final int RC_PIC = 1234;
    public DialogListener dialogListener;

    public static void toolbar(Activity activity, String string) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(string);
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }
    public static void alertDialog(Activity activity, int n, boolean bl, DialogListener dialogListener) {
        new Methods().dialogListener = dialogListener;
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(n, null));
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(bl);
        alertDialog.getWindow().setBackgroundDrawable((Drawable) new ColorDrawable(0));
        alertDialog.show();
        dialogListener.onCreated(alertDialog);
    }
    public static interface DialogListener {
        public void onCreated(AlertDialog var1);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
    public static void internet(Activity activity) {
        if (!Methods.isNetworkAvailable(activity)) {
            showNoInternetDialog(activity);
        }
    }
    public static void showNoInternetDialog(Activity activity) {

        Methods.alertDialog(activity, R.layout.alert_dialog_no_internet, false, new DialogListener() {
            @Override
            public void onCreated(AlertDialog var1) {
                ((Button)var1.findViewById(R.id.btnTryAgain)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        var1.dismiss();
                        internet(activity);
                    }
                });
            }
        });

    }
}
