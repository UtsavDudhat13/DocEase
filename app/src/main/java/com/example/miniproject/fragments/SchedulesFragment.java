package com.example.miniproject.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.miniproject.R;
import com.example.miniproject.adapters.AppointmentAdapter;
import com.example.miniproject.adapters.AppointmentListAdapter;
import com.example.miniproject.manager.CustomProgressDialog;
import com.example.miniproject.manager.Methods;
import com.example.miniproject.models.AppointmentModel;
import com.example.miniproject.models.DoctorModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SchedulesFragment extends Fragment {

    RecyclerView recyclerView;
    TextView textView;
    ArrayList<AppointmentModel> arrayList;
    AppointmentAdapter appointmentAdapter;
    CustomProgressDialog dialog;
    Boolean isInternet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_schedules, container, false);

        dialog = new CustomProgressDialog(getContext());
        internet();
        if (isInternet) {
            dialog.show();
        }
        arrayList = new ArrayList<AppointmentModel>();

        textView = view.findViewById(R.id.textview);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new DoctorHomeFragment.WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<AppointmentModel> options =
                new FirebaseRecyclerOptions.Builder<AppointmentModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patient").child(FirebaseAuth.getInstance().getUid()).child("appointments"), AppointmentModel.class)
                        .build();

        appointmentAdapter = new AppointmentAdapter(options, getActivity(),textView);
        recyclerView.setAdapter(appointmentAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        appointmentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        appointmentAdapter.stopListening();
    }

    private void internet() {
        if (!Methods.isNetworkAvailable(getContext())) {
            isInternet = false;
            showNoInternetDialog();

            return;
        }
        isInternet = true;

    }

    private void showNoInternetDialog() {

        Methods.alertDialog(getActivity(), R.layout.alert_dialog_no_internet, false, new Methods.DialogListener() {
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


    static class WrapContentLinearLayoutManager extends LinearLayoutManager {


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
}