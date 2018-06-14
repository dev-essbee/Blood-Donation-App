package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    private TextView mEmailInput, mPasswordInput;
    private Button mSignInButton, mForgotButton, mSignupButton;
    private ProgressBar mSignInProgress;
    private FirebaseAuth mAuth;
    private String email;
    private CoordinatorLayout mCoordinatorLayout;
    private String incIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.d("user", "already signed in");
        } else {
            Log.d("user", " signed out");
        }
        mEmailInput = findViewById(R.id.input_signin_email);
        mPasswordInput = findViewById(R.id.input_signin_password);
        mSignInButton = findViewById(R.id.button_signin_login);
        mForgotButton = findViewById(R.id.button_signin_reset_password);
        mSignupButton = findViewById(R.id.button_signin_signup);
        mCoordinatorLayout = findViewById(R.id.sign_in_layout);
        mSignInProgress = findViewById(R.id.sign_in_progress);
        mAuth = FirebaseAuth.getInstance();

        mEmailInput.addTextChangedListener(emailWatcher);
        mPasswordInput.addTextChangedListener(passWatcher);
        incIntent = getIntent().getStringExtra("act");
        if (!TextUtils.isEmpty(incIntent)) {
            if (incIntent.equals("new")) {
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Verify Your Email Id and " +
                                "Sign In to continue",
                        Snackbar
                                .LENGTH_INDEFINITE);
                snackbar.show();
            }
        }
//TODO: Launcher activity:Main activity
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = mEmailInput.getText().toString().trim();
                final String password = mPasswordInput.getText().toString().trim();

                if (TextUtils.isEmpty(emailId)) {
                    Toast.makeText(SignInActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSignInProgress.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(emailId, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        mPasswordInput.setError(getString(R.string.minimum_password));
                                        mSignInProgress.setVisibility(View.INVISIBLE);
                                    } else {
                                        Log.d("error", task.getException().toString());
                                        showErrorMessage(task.getException().toString());
                                        mPasswordInput.setText("");
                                        mSignInProgress.setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    mEmailInput.setText("");
                                    mPasswordInput.setText("");
                                    mSignInProgress.setVisibility(View.INVISIBLE);
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        mAuth.signOut();
                                        Snackbar snackbar = Snackbar
                                                .make(mCoordinatorLayout, "Verify your Email id " +
                                                        "to login", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }
                            }
                        });

            }
        });
        mForgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailInput.setText("");
                mPasswordInput.setText("");
                startActivity(new Intent(SignInActivity.this,
                        ResetPasswordActivity.class));

            }
        });
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailInput.setText("");
                mPasswordInput.setText("");
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
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
            Log.d("text", "text changed");
            if (s.toString().length() >= 6) {
                email = mEmailInput.getText().toString().trim();
                Log.d("text", "text changed" + email);
                if (checkEmail()) {
                    mSignInButton.setBackground(getResources().getDrawable(R.drawable.pri_button));
                    mSignInButton.setEnabled(true);
                    Log.d("text", "color");
                } else {
                    mSignInButton.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                    mSignInButton.setEnabled(false);
                }
            } else {
                mSignInButton.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                mSignInButton.setEnabled(false);
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
            Log.d("text", "email changed");
            if (mPasswordInput.getText().toString().length() >= 6) {
                email = s.toString().trim();
                if (checkEmail()) {
                    mSignInButton.setBackground(getResources().getDrawable(R.drawable.pri_button));
                    mSignInButton.setEnabled(true);

                    Log.d("text", "email color");
                } else {
                    mSignInButton.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                    mSignInButton.setEnabled(false);
                }
            } else {
                mSignInButton.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                mSignInButton.setEnabled(false);
            }
        }
    };

    private Boolean checkEmail() {
        if (!TextUtils.isEmpty(email) && email.contains("@") && email.contains(".")) {
            Log.d("text", "email id verified");
            return true;
        } else {
            Log.d("text", "email id wrong");
            return false;
        }
    }

    private void showErrorMessage(String msg) {
        if (msg.toLowerCase().contains("network")) {
            Toast.makeText(SignInActivity.this, "Please check your Internet Connection and try " +
                            "again.",
                    Toast.LENGTH_SHORT)
                    .show();
        } else if (msg.contains("formatted")) {
            Toast.makeText(SignInActivity.this, "Please enter a valid e-mail address.", Toast
                    .LENGTH_SHORT).show();
        } else if (msg.contains("no user")) {
            Toast.makeText(SignInActivity.this, "Account Doesn't exists. Please check your " +
                    "credentials", Toast.LENGTH_SHORT).show();
        } else if (msg.contains("password is invalid")) {
            Toast.makeText(SignInActivity.this, "Please check your email id or password and try " +
                    "again.", Toast.LENGTH_SHORT).show();
        }

    }
}
