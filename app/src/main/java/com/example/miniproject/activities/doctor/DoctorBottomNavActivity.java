package com.example.miniproject.activities.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.miniproject.R;
import com.example.miniproject.activities.BottomNavActivity;
import com.example.miniproject.fragments.DoctorHomeFragment;
import com.example.miniproject.fragments.DoctorProfileFragment;
import com.example.miniproject.fragments.HomeFragment;
import com.example.miniproject.fragments.ProfileFragment;
import com.example.miniproject.fragments.SchedulesFragment;
import com.example.miniproject.manager.Methods;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DoctorBottomNavActivity extends AppCompatActivity  implements ChipNavigationBar.OnItemSelectedListener{
    ChipNavigationBar bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_bottom_nav);

        bottomNav = findViewById(R.id.doctor_bottomNav);
        bottomNav.setOnItemSelectedListener(DoctorBottomNavActivity.this);


        loadFragment(new DoctorHomeFragment());
    }
    private void loadFragment(Fragment fragment) {
        if (fragment!= null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.doctor_frame_container,fragment)
                    .commit();
        } else {
            Log.d("err","Fragment Error");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu,menu);
        return true;
    }

    @Override
    public void onItemSelected(int i) {
        Fragment fragment= new DoctorHomeFragment();
        if (i == R.id.doctorhome_menu){
            fragment = new DoctorHomeFragment();
        } else if (i == R.id.doctorprofile) {
            fragment = new DoctorProfileFragment();
        }
        loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        Methods.alertDialog(DoctorBottomNavActivity.this, R.layout.alert_dialog_exit, false, new Methods.DialogListener() {
            @Override
            public void onCreated(AlertDialog alertDialog) {
                ((ImageView) alertDialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                ((ImageView) alertDialog.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        finishAffinity();
                    }
                });
            }
        });
    }
}