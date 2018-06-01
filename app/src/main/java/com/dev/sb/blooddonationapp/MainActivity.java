package com.dev.sb.blooddonationapp;

import android.content.Intent;
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
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else {
            Log.i("User Signed in", mAuth.getCurrentUser().getEmail());
        }
        actionBar = getSupportActionBar();
        setTitle(R.string.navigation_title_home);
        BottomNavigationView navigation = findViewById(R.id.navigationBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams=(CoordinatorLayout.LayoutParams)navigation
                .getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehaviour());
        loadFragment(new HomeFragment());
    }
    //TODO: Login activity
    //TODO:eligibility activity
    //TODO:user details activity
    //TODO: Logout, delete
    //TODO: Password Reset
    //TODO:


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
                            fragment = new SettingsFragment();
                            loadFragment(fragment);
                            return true;

                        case R.id.navigation_dummy:
                            actionBar.setTitle(R.string.navigation_title_dummy);
                            fragment = new DummyFragment();
                            loadFragment(fragment);
                            return true;
                    }
                    return false;
                }
            };


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
