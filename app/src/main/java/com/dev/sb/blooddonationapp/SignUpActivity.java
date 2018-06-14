package com.dev.sb.blooddonationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText mInputEmail, mInputPassword;
    private Button mSignUpButton, mSignInButton, mResetPassButton;
    private ProgressBar mSignUpProgress;
    private FirebaseAuth mAuth;
    private Context mContext = SignUpActivity.this;
    private Boolean signUpSuccessfull = false;
    private String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mInputEmail = findViewById(R.id.input_email);
        mInputPassword = findViewById(R.id.input_pass);
        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignInButton = findViewById(R.id.sign_in_button);
        mSignUpProgress = findViewById(R.id.sign_up_progress);

        mInputPassword.addTextChangedListener(passWatcher);
        mInputEmail.addTextChangedListener(emailWatcher);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mInputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(mContext, R.string.enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(mContext, R.string.enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(mContext, R.string.minimum_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                mSignUpProgress.setVisibility(View.VISIBLE);


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "On Complete" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                mSignUpProgress.setVisibility(View.GONE);
                                mInputPassword.setText("");
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Sign Up Failed" + task.getException(), Toast.LENGTH_SHORT).show();

                                    showErrorMessage(task.getException().toString());
                                } else {
                                    Toast.makeText(SignUpActivity.this, "done" + task.getException(), Toast.LENGTH_SHORT).show();
                                    signUpSuccessfull = true;
                                    mInputEmail.setText("");
                                    verifyEmail();
                                }
                            }
                        });
            }
        });

    }

    private void verifyEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class)
                            .putExtra("act", "new");
                    mAuth.signOut();

                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSignUpProgress.setVisibility(View.GONE);
    }

    private TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().length() >= 6) {
                email = mInputEmail.getText().toString().trim();

                if (checkEmail()) {
                    mSignUpButton.setBackground(getResources().getDrawable(R.drawable.pri_button));

                    mSignUpButton.setEnabled(true);
                } else {
                    mSignUpButton.setBackground(getResources().getDrawable(R.drawable
                            .pri_button_inactive));
                    mSignUpButton.setEnabled(false);
                }
            } else {
                mSignUpButton.setBackground(getResources().getDrawable(R.drawable
                        .pri_button_inactive));
                mSignUpButton.setEnabled(false);
            }

        }
    };
    private TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (mInputPassword.getText().toString().length() >= 6) {
                email = s.toString().trim();
                if (checkEmail()) {
                    mSignUpButton.setBackground(getResources().getDrawable(R.drawable.pri_button));
                    mSignUpButton.setEnabled(true);

                } else {
                    mSignUpButton.setBackground(getResources().getDrawable(R.drawable
                            .pri_button_inactive));
                    mSignUpButton.setEnabled(false);
                }
            } else {
                mSignUpButton.setBackground(getResources().getDrawable(R.drawable
                        .pri_button_inactive));
                mSignUpButton.setEnabled(false);
            }
        }
    };

    private void showErrorMessage(String msg) {
        if (msg.toLowerCase().contains("network")) {
            Toast.makeText(SignUpActivity.this, "Please check your Internet Connection and try " +
                            "again.",
                    Toast.LENGTH_SHORT)
                    .show();
        } else if (msg.contains("formatted")) {
            Toast.makeText(SignUpActivity.this, "Please enter a valid e-mail address.", Toast
                    .LENGTH_SHORT).show();
        } else if (msg.contains("already")) {
            Toast.makeText(SignUpActivity.this, "Account already exists, Log In to continue.",
                    Toast
                            .LENGTH_SHORT).show();
        }

    }

    private Boolean checkEmail() {
        if (!TextUtils.isEmpty(email) && email.contains("@") && email.contains(".")) {

            return true;
        } else {

            return false;
        }
    }


}
