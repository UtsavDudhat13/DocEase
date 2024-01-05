package com.example.miniproject.activities;

import static com.example.miniproject.manager.AppControl.mAuth;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject.R;
import com.example.miniproject.manager.CustomProgressDialog;
import com.example.miniproject.manager.Methods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    TextView tv_register;
    Button btnLogin;
    CheckBox chkShowPass;
    EditText edtPassword;
    EditText edtEmail;
    CustomProgressDialog dialog;

//    GoogleSignInClient googleSignInClient;
//    int RC_GOOGLE_LOGIN = 101;
//    Button btnGoogleLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new CustomProgressDialog(LoginActivity.this);
        Methods.internet(LoginActivity.this);

        tv_register = findViewById(R.id.tv_register);
        btnLogin = findViewById(R.id.btnLogin);
        chkShowPass = findViewById(R.id.chkShowPass);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

//        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,gso);

        setListeners();

//        if (mAuth.getCurrentUser()!=null){
//            startActivity(new Intent(LoginActivity.this, BottomNavActivity.class));
//            finish();
//        }
    }

    private void setListeners() {
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    edtPassword.setText("");
                                    edtEmail.setText("");

                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, BottomNavActivity.class));
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    edtPassword.setText("");
                                    edtEmail.setText("");
                                    Toast.makeText(LoginActivity.this, "Credentials are Invalid", Toast.LENGTH_SHORT).show();
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
//        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.show();
//                googleLogin();
//            }
//        });
    }

//    private void googleLogin() {
//
//        Intent intent = googleSignInClient.getSignInIntent();
//        startActivityForResult(intent,RC_GOOGLE_LOGIN);
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_GOOGLE_LOGIN){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuth(account.getIdToken());
//            } catch (Exception e){
//                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//        dialog.dismiss();
//    }

//    private void firebaseAuth(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            dialog.dismiss();
//                            startActivity(new Intent(LoginActivity.this, BottomNavActivity.class));
//                            finish();
//                        }else {
//                            dialog.dismiss();
//                            Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}