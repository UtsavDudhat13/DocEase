package com.example.miniproject.activities.doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.miniproject.R;
import com.example.miniproject.activities.AppointmentActivity;
import com.example.miniproject.activities.ProfileActivity;
import com.example.miniproject.manager.CustomProgressDialog;
import com.example.miniproject.manager.Methods;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AppointmentDetailsActivity extends AppCompatActivity {

    String appointmentId,date,time,patientImage,patientMobile,patientName,reason,doctorUid,patientUid,status;
    Button btnConfirm;
    ImageView img_patient;
    TextView tv_patient_name,tv_reason,tv_date,tv_time,tv_patient_mobile,tv_confirmed;
    Map<String, Object> updatedData;
    CustomProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Methods.internet(AppointmentDetailsActivity.this);

        Methods.toolbar(AppointmentDetailsActivity.this, "Appointment Details");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("doctor");

        dialog = new CustomProgressDialog(AppointmentDetailsActivity.this);

        updatedData = new HashMap<>();

        btnConfirm = findViewById(R.id.btnConfirm);
        img_patient = findViewById(R.id.img_patient);
        tv_patient_name = findViewById(R.id.tv_patient_name);
        tv_date = findViewById(R.id.tv_date);
        tv_reason = findViewById(R.id.tv_reason);
        tv_time = findViewById(R.id.tv_time);
        tv_patient_mobile = findViewById(R.id.tv_patient_mobile);
        tv_confirmed = findViewById(R.id.tv_confirmed);

        appointmentId = getIntent().getStringExtra("appointmentId");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        patientImage = getIntent().getStringExtra("patientImage");
        patientMobile = getIntent().getStringExtra("patientMobile");
        patientName = getIntent().getStringExtra("patientName");
        reason = getIntent().getStringExtra("reason");
        status = getIntent().getStringExtra("status");
        doctorUid = getIntent().getStringExtra("doctorUid");
        patientUid = getIntent().getStringExtra("patientUid");

        tv_patient_name.setText(patientName);
        tv_date.setText(date);
        tv_reason.setText(reason);
        tv_time.setText(time);
        tv_patient_mobile.setText(patientMobile);
        Glide.with(this).load(patientImage).into(img_patient);

        if (status.equals("Confirmed")){
            btnConfirm.setVisibility(View.GONE);
            tv_confirmed.setVisibility(View.VISIBLE);
        }

        reference = reference.child("info").child(doctorUid).child("appointments").child(appointmentId);

        setListeners();
    }

    private void setListeners() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                updateData();
            }
        });
    }

    private void updateData() {
        updatedData.put("status", "Confirmed");

        reference.updateChildren(updatedData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    // Handle the error
                    Toast.makeText(AppointmentDetailsActivity.this, "Error To Update the data", Toast.LENGTH_SHORT).show();
                } else {
                    reference = FirebaseDatabase.getInstance().getReference();
                    reference = reference.child("patient").child(patientUid).child("appointments").child(appointmentId);
                    reference.updateChildren(updatedData, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null) {
                                // Handle the error
                                Toast.makeText(AppointmentDetailsActivity.this, "Error To Update the data", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                btnConfirm.setVisibility(View.GONE);
                                tv_confirmed.setVisibility(View.VISIBLE);
                                Toast.makeText(AppointmentDetailsActivity.this, "Appointment was confirmed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}