package com.dev.sb.blooddonationapp;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputEditText mResetEmail;
    private Button mResetButton;
    private FirebaseAuth mAuth;
    private android.support.v7.app.ActionBar mActionBar;
    private String intentActivity;


    @Override
    public boolean onSupportNavigateUp() {
        if (mActionBar != null) {
            if (!intentActivity.isEmpty() && intentActivity.equals("MainActivity")) {
                startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
                finish();
            } else {
                finish();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mResetEmail = findViewById(R.id.reset_email);
        mResetButton = findViewById(R.id.button_submit_reset);
        mAuth = FirebaseAuth.getInstance();
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        intentActivity = getIntent().getStringExtra("activity");
        mResetEmail.addTextChangedListener(textWatcher);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mResetEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter your E-mail id, to " +
                            "reset" +
                            " the password", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetPasswordActivity.this, "Check your " +
                                                "E-mail inbox for instructions to change the " +
                                                "password.", Toast
                                                .LENGTH_LONG).show();
                                        if (mAuth.getCurrentUser() != null) {
                                            mAuth.signOut();
                                        }
                                        startActivity(new Intent(ResetPasswordActivity
                                                .this, SignInActivity.class));
                                        finish();
                                    } else {

                                        showErrorMessage(task.getException().toString());

                                    }
                                }
                            });
                }
            }
        });
    }

    private void showErrorMessage(String msg) {
        if (msg.toLowerCase().contains("network")) {
            Toast.makeText(ResetPasswordActivity.this, "Please check your Internet Connection and try " +
                            "again.",
                    Toast.LENGTH_SHORT)
                    .show();
        } else if (msg.contains("formatted")) {
            Toast.makeText(ResetPasswordActivity.this, "Please enter a valid e-mail address.", Toast
                    .LENGTH_SHORT).show();
        } else if (msg.contains("no user")) {
            Toast.makeText(ResetPasswordActivity.this, "Account Doesn't exists. Please check your " +
                    "credentials.", Toast.LENGTH_SHORT).show();
        }

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                if (checkEmail(s.toString().trim())) {
                    mResetButton.setBackground(getResources().getDrawable(R.drawable.pri_button));
                    mResetButton.setEnabled(true);


                } else {
                    mResetButton.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                    mResetButton.setEnabled(false);
                }
            }
        }
    };

    private Boolean checkEmail(String email) {
        if (!TextUtils.isEmpty(email) && email.contains("@") && email.contains(".")) {

            return true;
        } else {

            return false;
        }
    }
}
