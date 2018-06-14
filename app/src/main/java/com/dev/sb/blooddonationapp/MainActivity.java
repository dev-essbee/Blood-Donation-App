package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private FirebaseAuth mAuth;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Log.i("Usersignedin", "null" + "");
            isUserDataExists();
        } else if (mAuth.getCurrentUser() == null) {
            Log.i("user", "no user mainactivity");
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }


//todo above function properly android splash screen or store in local storage


        actionBar = getSupportActionBar();
        setTitle(R.string.navigation_title_home);
        BottomNavigationView navigation = findViewById(R.id.navigationBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation
                .getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        loadFragment(new HomeFragment());

    }
    //TODO: Login activity
    //TODO:eligibility activity
    //TODO:user details activity
    //TODO: Logout, delete
    //TODO: Password Reset
    //TODO: finish previous fragment


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.navigation_donor:
                            actionBar.setTitle(R.string.navigation_title_home);
                            fragment = new HomeFragment();
                            loadFragment(fragment);

                            return true;

                        case R.id.navigation_settings:
                            actionBar.setTitle(R.string.navigation_title_settings);
                            fragment = new ProfileFragment();
                            loadFragment(fragment);

                            return true;

                        case R.id.navigation_dummy:
                            actionBar.setTitle(R.string.navigation_title_bld_bnk);
                            fragment = new BloodBanksFragment();
                            loadFragment(fragment);

                            return true;
                    }
                    return false;
                }
            };


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        /*transaction.addToBackStack(null);*/
        transaction.commit();

    }

    private void isUserDataExists() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mAuth.getUid()).exists()) {
                    Log.d("usersignedin", "data exists" + mAuth.getUid());
                    city = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("city").getValue(String
                            .class);
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.city_key), city);
                    editor.apply();
                    Log.d("city1",city);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
