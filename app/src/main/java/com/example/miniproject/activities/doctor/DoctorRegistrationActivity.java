package com.example.miniproject.activities.doctor;

import static com.example.miniproject.manager.AppControl.mAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.miniproject.R;
import com.example.miniproject.activities.BottomNavActivity;
import com.example.miniproject.activities.LoginActivity;
import com.example.miniproject.activities.RegisterActivity;
import com.example.miniproject.manager.CustomProgressDialog;
import com.example.miniproject.manager.Methods;
import com.example.miniproject.models.DoctorModel;
import com.example.miniproject.models.PatientModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class DoctorRegistrationActivity extends AppCompatActivity {
    Spinner spinner_category;
    TextView tv_login;
    EditText edtName;
    EditText edtEmail;
    EditText edtPassword;
    EditText edtMobile;
    EditText edtExp;
    EditText edtDateOfBirth;
    ImageView img_male;
    ImageView img_female;
    String gender = "";
    ImageView img_User_photo;
    Button btnUploadImage;
    Uri imageUri;
    String uid;
    private boolean isUploaded = false;
    EditText edtConfirmPassword;
    Button btnRegister;
    CustomProgressDialog dialog;

    DoctorModel doctorModel;

    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseStorage storage;
    StorageReference storageReference;

    private ArrayList<String> listDoctorCategory = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        Methods.internet(DoctorRegistrationActivity.this);


        database = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();

        dialog = new CustomProgressDialog(DoctorRegistrationActivity.this);

        spinner_category = findViewById(R.id.spinner_category);
        tv_login = findViewById(R.id.tv_login);
        btnRegister = findViewById(R.id.btnRegister);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        img_male = (ImageView) findViewById(R.id.img_male);
        img_female = (ImageView) findViewById(R.id.img_female);
        img_User_photo = (ImageView) findViewById(R.id.img_User_photo);
        edtName = findViewById(R.id.edtName);
        edtExp = findViewById(R.id.edtExp);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtDateOfBirth = (EditText) findViewById(R.id.edtDateOfBirth);

        img_male.setAlpha(0.5f);
        img_female.setAlpha(0.5f);

        setListeners();
        Arraylist();

setListeners();
    }

    private void setListeners() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorRegistrationActivity.this, DoctorLoginActivity.class));
                finish();
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(DoctorRegistrationActivity.this)
                        .crop(1f,1f)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(Methods.RC_PIC);
            }
        });
        img_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";
                img_male.setAlpha(1f);
                img_female.setAlpha(0.5f);
                img_female.setBackgroundResource(0);
            }
        });
        img_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";
                img_male.setAlpha(0.5f);
                img_female.setAlpha(1f);
                img_male.setBackgroundResource(0);
            }
        });
        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strUserName = edtName.getText().toString().trim();
                String strEmail = edtEmail.getText().toString().trim();
                String strPassword = edtPassword.getText().toString().trim();
                String strConfirmPassword = edtConfirmPassword.getText().toString().trim();
                String strMobile = edtMobile.getText().toString().trim();
                String strDateOfBirth = edtDateOfBirth.getText().toString().trim();
                String strExperience = edtExp.getText().toString().trim();
                String strCategory = spinner_category.getSelectedItem().toString();

                if (strUserName.isEmpty()) {
                    edtName.setError("Please enter User Name");
                    edtName.requestFocus();
                    return;
                }
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
                if (strConfirmPassword.isEmpty()) {
                    edtConfirmPassword.setError("Please enter Confirm Password");
                    edtConfirmPassword.requestFocus();
                    return;
                }
                if (!strPassword.equals(strConfirmPassword)) {
                    edtConfirmPassword.setError("Passwords are mismatched");
                    edtConfirmPassword.requestFocus();
                    return;
                }
                if (strMobile.isEmpty()) {
                    edtMobile.setError("Please enter mobile number");
                    edtMobile.requestFocus();
                    return;
                }
                if (!isValidPhoneNumber(strMobile)) {
                    edtMobile.setError("Please enter valid mobile number");
                    edtMobile.requestFocus();
                    return;
                }
                if (strDateOfBirth.isEmpty()) {
                    edtDateOfBirth.setError("Please enter date of birth");
                    edtDateOfBirth.requestFocus();
                    return;
                }
                if (strExperience.isEmpty()){
                    edtExp.setError("Please enter experience");
                    edtExp.requestFocus();
                    return;
                }
                if (strCategory.equals((Object) "- Select Category -")) {
                    Toast.makeText(DoctorRegistrationActivity.this, (CharSequence) "Please select category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gender.isEmpty()) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isUploaded){
                    Toast.makeText(DoctorRegistrationActivity.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();
                mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(DoctorRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    edtName.setText("");
                                    edtEmail.setText("");
                                    edtPassword.setText("");
                                    edtConfirmPassword.setText("");
                                    edtMobile.setText("");
                                    edtDateOfBirth.setText("");

                                    uid = mAuth.getCurrentUser().getUid();

                                    addDataToFirebase(uid, strUserName, strEmail, strPassword, strMobile, strDateOfBirth, gender, strExperience, strCategory);

                                    Toast.makeText(DoctorRegistrationActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(DoctorRegistrationActivity.this, DoctorBottomNavActivity.class);

                                    startActivity(intent);
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    edtName.setText("");
                                    edtEmail.setText("");
                                    edtPassword.setText("");
                                    edtConfirmPassword.setText("");
                                    edtMobile.setText("");
                                    edtDateOfBirth.setText("");
                                    Toast.makeText(DoctorRegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    private void addDataToFirebase(String uid, String strUserName, String strEmail, String strPassword, String strMobile, String strDateOfBirth, String gender, String strExperience, String strCategory) {
        doctorModel = new DoctorModel();

        doctorModel.setUid(uid);
        doctorModel.setDoctorName(strUserName);
        doctorModel.setDoctorEmail(strEmail);
        doctorModel.setPassword(strPassword);
        doctorModel.setMobileNo(strMobile);
        doctorModel.setDateOfBirth(strDateOfBirth);
        doctorModel.setCategory(strCategory);
        doctorModel.setExperience(strExperience);
        doctorModel.setGender(gender);

        storageReference = storage.getReference().child("doctor").child(System.currentTimeMillis() + "");
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        doctorModel.setUserImage(uri.toString());

                        storeData(doctorModel);
                    }
                });
            }
        });

    }

    private void storeData(DoctorModel doctorModel) {
        reference = database.getReference().child("doctor").child("info");
        reference.child(uid).setValue(doctorModel);
    }

    private void Arraylist() {
        this.listDoctorCategory.add("- Select Category -");
        this.listDoctorCategory.add("General practitioner");
        this.listDoctorCategory.add("Surgeon");
        this.listDoctorCategory.add("Cardiologist");
        this.listDoctorCategory.add("Dermatologist");
        this.listDoctorCategory.add("Dentist");
        this.listDoctorCategory.add("Psychiatrist");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.listDoctorCategory);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        this.spinner_category.setAdapter(arrayAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        imageUri = data.getData();
        if (resultCode == RESULT_OK && requestCode == Methods.RC_PIC) {
            ContentResolver resolver = getContentResolver();
            try {
                InputStream inputStream = resolver.openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                isUploaded = true;
                (Glide.with(getApplicationContext()).load(bitmap)).into(this.img_User_photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 1);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(1, year);
                calendar.set(2, monthOfYear);
                calendar.set(5, dayOfMonth);
                edtDateOfBirth.setText(String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year));
            }
        }, calendar.get(1), calendar.get(2), calendar.get(5));

        datePickerDialog.show();
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }
}