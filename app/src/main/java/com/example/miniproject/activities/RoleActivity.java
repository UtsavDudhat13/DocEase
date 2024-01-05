package com.example.miniproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.miniproject.R;
import com.example.miniproject.activities.doctor.DoctorLoginActivity;

public class RoleActivity extends AppCompatActivity {

    AppCompatButton btnDoctor;
    AppCompatButton btnPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        btnDoctor = findViewById(R.id.btnDoctor);
        btnPatient = findViewById(R.id.btnPatient);

        setListeners();
    }

    private void setListeners() {
        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RoleActivity.this, DoctorLoginActivity.class));
            }
        });
        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RoleActivity.this,LoginActivity.class));
            }
        });
    }
}