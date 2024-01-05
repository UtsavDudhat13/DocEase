package com.example.miniproject.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.miniproject.R;
import com.example.miniproject.activities.AboutUsActivity;
import com.example.miniproject.activities.ProfileActivity;
import com.example.miniproject.activities.RoleActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    LinearLayout ll_profile;
    LinearLayout ll_about_us;
    LinearLayout ll_share_app;
    LinearLayout ll_rate_us;
    LinearLayout ll_logout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ll_profile = (LinearLayout) view.findViewById(R.id.ll_profile);
        ll_about_us = (LinearLayout) view.findViewById(R.id.ll_about_us);
        ll_share_app = (LinearLayout) view.findViewById(R.id.ll_share_app);
        ll_rate_us = (LinearLayout) view.findViewById(R.id.ll_rate_us);
        ll_logout = (LinearLayout) view.findViewById(R.id.ll_logout);



        setListeners();

        return view;
    }

    private void setListeners() {
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), RoleActivity.class));
                getActivity().finish();
            }
        });
        ll_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });
        ll_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = getResources().getString(R.string.app_name);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, string);
                intent.putExtra(Intent.EXTRA_TEXT, "Download this app to get Instant Doctor Appointment in 5 minutes :\n\nhttps://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                startActivity(Intent.createChooser(intent, "Share With"));
            }
        });
        ll_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("market://details?id=");
                    stringBuilder.append(getActivity().getPackageName());
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) stringBuilder.toString())));
                } catch (ActivityNotFoundException activityNotFoundException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("https://play.google.com/store/apps/details?id=");
                    stringBuilder.append(getActivity().getPackageName());
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) stringBuilder.toString())));
                }
            }
        });
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ProfileActivity.class);

                startActivity(intent);
            }
        });
    }
}