package com.example.miniproject.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miniproject.R;
import com.example.miniproject.activities.DoctorListActivity;
import com.example.miniproject.activities.RoleActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    CardView card_logout;
    Button btnFindDoctor;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);




        card_logout = view.findViewById(R.id.card_logout);
        btnFindDoctor = view.findViewById(R.id.btnFindDoctor);



        setListeners();

        return view;
    }

    private void setListeners() {
        card_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), RoleActivity.class));
                getActivity().finish();
            }
        });
        btnFindDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DoctorListActivity.class));
            }
        });
    }

}