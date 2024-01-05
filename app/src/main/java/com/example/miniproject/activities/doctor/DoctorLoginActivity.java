package com.example.miniproject.activities.doctor;

import static com.example.miniproject.manager.AppControl.mAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject.R;
import com.example.miniproject.activities.BottomNavActivity;
import com.example.miniproject.activities.LoginActivity;
import com.example.miniproject.activities.RegisterActivity;
import com.example.miniproject.manager.CustomProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class DoctorLoginActivity extends AppCompatActivity {
    TextView tv_register;
    Button btnLogin;
    CheckBox chkShowPass;
    EditText edtPassword;
    EditText edtEmail;
    CustomProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        dialog = new CustomProgressDialog(DoctorLoginActivity.this);

        tv_register = findViewById(R.id.tv_register);
        btnLogin = findViewById(R.id.btnLogin);
        chkShowPass = findViewById(R.id.chkShowPass);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        setListeners();

//        if (mAuth.getCurrentUser()!=null){
//            startActivity(new Intent(DoctorLoginActivity.this, BottomNavActivity.class));
//            finish();
//        }
    }

    private void setListeners() {
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorLoginActivity.this, DoctorRegistrationActivity.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strEmail = edtEmail.getText().toString().trim();
                String strPassword = edtPassword.getText().toString().trim();

                if (strEmail.isEmpty()) {
                    edtEmail.setError("Please enter Email");
                    edtEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    edtEmail.setError("Please enter Valid Email");
                    edtEmail.requestFocus();
                    return;
                }
                if (strPassword.isEmpty()) {
                    edtPassword.setError("Please enter Password");
                    edtPassword.requestFocus();
                    return;
                }
                if (strPassword.length() < 6) {
                    edtPassword.setError("Password must have minimum 6 characters");
                    edtPassword.requestFocus();
                    return;
                }
                dialog.show();
                mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(DoctorLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    edtPassword.setText("");
                                    edtEmail.setText("");

                                    Toast.makeText(DoctorLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DoctorLoginActivity.this, DoctorBottomNavActivity.class));
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    edtPassword.setText("");
                                    edtEmail.setText("");
                                    Toast.makeText(DoctorLoginActivity.this, "Credentials are Invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        chkShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkShowPass.isChecked()) {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    return;
                }
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }


}