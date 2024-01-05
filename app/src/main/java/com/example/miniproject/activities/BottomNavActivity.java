package com.example.miniproject.activities;

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
import com.example.miniproject.fragments.HomeFragment;
import com.example.miniproject.fragments.ProfileFragment;
import com.example.miniproject.fragments.SchedulesFragment;
import com.example.miniproject.manager.Methods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class BottomNavActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    ChipNavigationBar bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(this);


        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        if (fragment!= null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container,fragment)
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
        Fragment fragment= new HomeFragment();
        if (i == R.id.home_menu){
            fragment = new HomeFragment();
        } else if (i == R.id.schedule) {
            fragment = new SchedulesFragment();
        } else if (i == R.id.profile) {
            fragment = new ProfileFragment();
        }
//        switch (i){
//            case R.id.home_menu:
//                fragment = new HomeFragment();
//                break;
//            case R.id.schedule:
//                fragment = new SchedulesFragment();
//                break;
//            case R.id.remainder:
//                fragment = new RemainderFragment();
//                break;
//            case R.id.profile:
//                fragment = new ProfileFragment();
//                break;
//        }
        loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        Methods.alertDialog(BottomNavActivity.this, R.layout.alert_dialog_exit, false, new Methods.DialogListener() {
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