package com.example.miniproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.miniproject.R;
import com.example.miniproject.manager.Methods;

public class AboutUsActivity extends AppCompatActivity {

    LinearLayout ll_shareApp;
    LinearLayout ll_RateUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Methods.toolbar(AboutUsActivity.this,"About Us");

        ll_shareApp = findViewById(R.id.ll_shareApp);
        ll_RateUs = findViewById(R.id.ll_RateUs);

        setListeners();

    }

    private void setListeners() {
        ll_shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = getResources().getString(R.string.app_name);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, string);
                intent.putExtra(Intent.EXTRA_TEXT, "Download this app to get Instant Doctor Appointment in 5 minutes :\n\nhttps://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, "Share With"));
            }
        });
        ll_RateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("market://details?id=");
                    stringBuilder.append(getApplicationContext().getPackageName());
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) stringBuilder.toString())));
                } catch (ActivityNotFoundException activityNotFoundException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("https://play.google.com/store/apps/details?id=");
                    stringBuilder.append(getApplicationContext().getPackageName());
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) stringBuilder.toString())));
                }
            }
        });
    }
}