package com.example.miniproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.miniproject.R;
import com.example.miniproject.manager.CustomProgressDialog;
import com.example.miniproject.manager.Methods;
import com.example.miniproject.models.AppointmentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {
    String name, number, category, email, Docuid, experience, imageUri;
    String patientName, patientImage, time, date, patientMobile;
    TextView tv_email, tv_number, tv_experience, tv_category, tv_doctor_name;
    EditText edtDateOfAppointment;
    EditText edtTimeOfAppointment;
    EditText edtReason;
    Button btnBook;
    ImageView img_doctor;
    CustomProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference reference, doctorReference;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Methods.internet(AppointmentActivity.this);

        Methods.toolbar(AppointmentActivity.this, "Doctor Information");

        dialog = new CustomProgressDialog(AppointmentActivity.this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("patient");

        tv_email = findViewById(R.id.tv_email);
        tv_number = findViewById(R.id.tv_number);
        tv_experience = findViewById(R.id.tv_experience);
        tv_category = findViewById(R.id.tv_category);
        tv_doctor_name = findViewById(R.id.tv_doctor_name);
        edtDateOfAppointment = findViewById(R.id.edtDateOfAppointment);
        edtTimeOfAppointment = findViewById(R.id.edtTimeOfAppointment);
        edtReason = findViewById(R.id.edtReason);
        btnBook = findViewById(R.id.btnBook);
        img_doctor = findViewById(R.id.img_doctor);

        Docuid = getIntent().getStringExtra("doctorUid");
        name = getIntent().getStringExtra("doctorName");
        email = getIntent().getStringExtra("doctorEmail");
        number = getIntent().getStringExtra("doctorNumber");
        category = getIntent().getStringExtra("doctorCategory");
        experience = getIntent().getStringExtra("doctorExperience");
        experience = getIntent().getStringExtra("doctorExperience");
        imageUri = getIntent().getStringExtra("doctorPhoto");

        tv_category.setText(category);
        tv_email.setText(email);
        tv_number.setText(number);
        tv_doctor_name.setText(name);
        tv_experience.setText(experience + " years");
        Glide.with(this).load(imageUri).into(img_doctor);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            reference = reference.child(uid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    patientName = snapshot.child("patientName").getValue(String.class);
                    patientImage = snapshot.child("userImage").getValue(String.class);
                    patientMobile = snapshot.child("mobileNo").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        reference = reference.child("appointments");

        setListeners();
    }

    private void setListeners() {
        edtDateOfAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        edtTimeOfAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = edtDateOfAppointment.getText().toString().trim();
                String time = edtTimeOfAppointment.getText().toString().trim();
                String reason = edtReason.getText().toString().trim();
                if (reason.isEmpty()){
                    edtReason.setError("Please Provide Reason");
                    edtReason.requestFocus();
                    return;
                }
                if (date.isEmpty()) {
                    Toast.makeText(AppointmentActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time.isEmpty()) {
                    Toast.makeText(AppointmentActivity.this, "Please select Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                addDataToFirebase(date, time,reason);

            }
        });
    }

    private void addDataToFirebase(String date, String time,String reason) {
        final String appointmentId;
        AppointmentModel appointmentModel = new AppointmentModel();
        appointmentModel.setDate(date);
        appointmentModel.setTime(time);
        appointmentModel.setPatientUid(uid);
        appointmentModel.setPatientName(patientName);
        appointmentModel.setPatientMobile(patientMobile);
        appointmentModel.setPatientImage(patientImage);
        appointmentModel.setDoctorUid(Docuid);
        appointmentModel.setDoctorName(name);
        appointmentModel.setDoctorMobile(number);
        appointmentModel.setDoctorImage(imageUri);
        appointmentModel.setReason(reason);
        appointmentModel.setStatus("Pending");


        appointmentId = reference.push().getKey();
        appointmentModel.setAppointmentId(appointmentId);

        reference = reference.child(appointmentId);
        reference.setValue(appointmentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseDatabase db  = FirebaseDatabase.getInstance();
                doctorReference = db.getReference().child("doctor").child("info").child(Docuid).child("appointments").child(appointmentId);
                doctorReference.setValue(appointmentModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Methods.alertDialog(AppointmentActivity.this, R.layout.dialog_booked_appointment, false, new Methods.DialogListener() {
                                    @Override
                                    public void onCreated(AlertDialog var1) {

                                        ((Button)var1.findViewById(R.id.btnDone)).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(AppointmentActivity.this,BottomNavActivity.class));
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(AppointmentActivity.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(1, year);
                calendar.set(2, monthOfYear);
                calendar.set(5, dayOfMonth);
                date = String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
                edtDateOfAppointment.setText(date);
            }
        }, calendar.get(1), calendar.get(2), calendar.get(5));

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(1, i);
                calendar.set(2, i1);

                time = String.format("%02d:%02d ", i, i1);
                edtTimeOfAppointment.setText(time);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
}