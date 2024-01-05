package com.example.miniproject.activities;

import static com.example.miniproject.manager.AppControl.context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.miniproject.R;
import com.example.miniproject.adapters.DoctorListAdapter;
import com.example.miniproject.manager.CustomProgressDialog;
import com.example.miniproject.manager.Methods;
import com.example.miniproject.models.DoctorModel;
import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DoctorListAdapter doctorListAdapter;
    ArrayList<DoctorModel> arrayList;
    CustomProgressDialog dialog;
    Boolean isInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        Methods.toolbar(DoctorListActivity.this, "Find Doctor");

        dialog = new CustomProgressDialog(DoctorListActivity.this);
        internet();
        if (isInternet) {
            dialog.show();
        }

        arrayList = new ArrayList<DoctorModel>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(DoctorListActivity.this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<DoctorModel> options =
                new FirebaseRecyclerOptions.Builder<DoctorModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("doctor").child("info"), DoctorModel.class)
                        .build();


        doctorListAdapter = new DoctorListAdapter(options, DoctorListActivity.this);
        recyclerView.setAdapter(doctorListAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1200);
        setListeners();
    }

    private void setListeners() {

    }

    private void internet() {
        if (!Methods.isNetworkAvailable(DoctorListActivity.this)) {
            isInternet = false;
            showNoInternetDialog();

            return;
        }
        isInternet = true;

    }

    private void showNoInternetDialog() {

        Methods.alertDialog(DoctorListActivity.this, R.layout.alert_dialog_no_internet, false, new Methods.DialogListener() {
            @Override
            public void onCreated(AlertDialog var1) {
                ((Button) var1.findViewById(R.id.btnTryAgain)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        var1.dismiss();
                        isInternet = true;
                        internet();
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        doctorListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doctorListAdapter.stopListening();
    }
}

class WrapContentLinearLayoutManager extends LinearLayoutManager {


    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    //... constructor
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("TAG", "meet a IOOBE in RecyclerView");
        }
    }
}