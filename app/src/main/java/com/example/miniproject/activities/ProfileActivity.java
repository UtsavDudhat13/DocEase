package com.example.miniproject.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.miniproject.R;
import com.example.miniproject.manager.CustomProgressDialog;
import com.example.miniproject.manager.Methods;
import com.example.miniproject.models.PatientModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private boolean isUploaded = false;

    PatientModel patientModel;
    EditText edtDateOfBirth;
    EditText edtName;
    //    EditText edtEmail;
    EditText edtMobile;
    ImageView img_male;
    ImageView img_female;
    String gender = "";
    RelativeLayout rl_add_user_photo;
    ImageView img_User_photo;
    Uri uri;
    String patientName, patientEmail, mobileNo, dateOfBirth, dbGender, password, patientUid;
    String uid;
    CustomProgressDialog dialog;

    FirebaseDatabase database;
    DatabaseReference reference;

    Button btnUpdateImage;
    Button btnUpdate;
    String imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    Map<String, Object> updatedData;
    Boolean isInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        dialog = new CustomProgressDialog(ProfileActivity.this);
        internet();
        if (isInternet) {
            dialog.show();
        }

        Methods.toolbar(ProfileActivity.this, "Edit Profile");

        updatedData = new HashMap<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("patient");
        storage = FirebaseStorage.getInstance();

        img_male = (ImageView) findViewById(R.id.img_male);
        img_female = (ImageView) findViewById(R.id.img_female);
        rl_add_user_photo = (RelativeLayout) findViewById(R.id.rl_add_user_photo);
        img_User_photo = (ImageView) findViewById(R.id.img_User_photo);
        edtDateOfBirth = (EditText) findViewById(R.id.edtDateOfBirth);
        edtName = (EditText) findViewById(R.id.edtName);
//        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        btnUpdateImage = (Button) findViewById(R.id.btnUpdateImage);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        img_male.setAlpha(0.5f);
        img_female.setAlpha(0.5f);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            reference = reference.child(uid);
            Log.d("id===", uid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dialog.dismiss();
                    internet();

                    if (snapshot.exists()) {

                        patientName = snapshot.child("patientName").getValue(String.class);
                        patientUid = snapshot.child("uid").getValue(String.class);
                        patientEmail = snapshot.child("patientEmail").getValue(String.class);
                        mobileNo = snapshot.child("mobileNo").getValue(String.class);
                        dateOfBirth = snapshot.child("dateOfBirth").getValue(String.class);
                        dbGender = snapshot.child("gender").getValue(String.class);
                        password = snapshot.child("password").getValue(String.class);
                        imageUri = snapshot.child("userImage").getValue(String.class);

                        edtName.setText(patientName);
//                        edtEmail.setText(patientEmail);
                        edtMobile.setText(mobileNo);
                        edtDateOfBirth.setText(dateOfBirth);

                        if (dbGender.equals("Male")) {
                            gender = "Male";
                            img_male.setAlpha(1f);
                            img_female.setAlpha(0.5f);
                            img_female.setBackgroundResource(0);
                        } else if (dbGender.equals("Female")) {
                            gender = "Female";
                            img_male.setAlpha(0.5f);
                            img_female.setAlpha(1f);
                            img_male.setBackgroundResource(0);
                        }

                        (Glide.with(getApplicationContext()).load(imageUri)).into(img_User_photo);

                    } else {
                        // Data does not exist for this UID
                        Log.d("error===", "Data Not found in profile activity.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        setListeners();

    }


    private void setListeners() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUserName = edtName.getText().toString().trim();
                String strMobile = edtMobile.getText().toString().trim();
                String strDateOfBirth = edtDateOfBirth.getText().toString().trim();

                if (strUserName.isEmpty()) {
                    edtName.setError("Please enter User Name");
                    edtName.requestFocus();
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
                if (gender.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateDataToFirebase(strUserName, strMobile, strDateOfBirth);

            }
        });
        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ImagePicker.Companion.with(ProfileActivity.this)
                        .crop(1, 1)
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
    }

    private void updateDataToFirebase(String strUserName, String strMobile, String strDateOfBirth) {

        updatedData.put("patientName", strUserName);
        updatedData.put("mobileNo", strMobile);
        updatedData.put("dateOfBirth", strDateOfBirth);
        updatedData.put("gender", gender);

        dialog.show();

        reference = database.getReference().child("patient");
        reference.child(uid).updateChildren(updatedData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    // Handle the error
                    Toast.makeText(ProfileActivity.this, "Error To Update the data", Toast.LENGTH_SHORT).show();
                } else {
                    // Data updated successfully

                    if (isUploaded && uri != null) {
                        dialog.show();
                        storageReference = storage.getReference().child("patient").child(System.currentTimeMillis() + "");

                        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri imguri) {
                                        updatedData.put("userImage", imguri.toString());
                                        reference = database.getReference().child("patient");

                                        reference.child(uid).updateChildren(updatedData, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                if (error != null) {
                                                    // Handle the error
                                                    Toast.makeText(ProfileActivity.this, "Error To Update the data", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Data updated successfully
                                                    dialog.dismiss();
                                                    isUploaded = false;
                                                    Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri);
                                        storageReference1.delete();
                                    }
                                });
                            }
                        });
                    } else {

                        dialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        uri = data.getData();
        if (resultCode == RESULT_OK && requestCode == Methods.RC_PIC) {
            ContentResolver resolver = getContentResolver();
            try {
                InputStream inputStream = resolver.openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Glide.with(getApplicationContext()).load(bitmap).circleCrop().into(this.img_User_photo);
                isUploaded = true;
                inputStream.close();
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

    private static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private void internet() {
        if (!Methods.isNetworkAvailable(ProfileActivity.this)) {
            isInternet = false;

            showNoInternetDialog();
            return;
        }
isInternet= true;
    }

    private void showNoInternetDialog() {

        Methods.alertDialog(ProfileActivity.this, R.layout.alert_dialog_no_internet, false, new Methods.DialogListener() {
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
}