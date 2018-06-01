package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignInActivity extends AppCompatActivity
    {
        private TextView mEmailInput, mPasswordInput;
        private Button mSignInButton, mForgotButton, mSignupButton;
        private ProgressBar mSignInProgress;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_in);

                mEmailInput = findViewById(R.id.input_signin_email);
                mPasswordInput = findViewById(R.id.input_signin_password);
                mSignInButton = findViewById(R.id.button_signin_login);
                mForgotButton = findViewById(R.id.button_signin_reset_password);
                mSignupButton = findViewById(R.id.button_signin_signup);
                mSignInProgress = findViewById(R.id.sign_in_progress);
                mAuth = FirebaseAuth.getInstance();


//TODO: Launcher activity:Main activity
                mSignInButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                String emailId = mEmailInput.getText().toString().trim();
                                final String password = mPasswordInput.getText().toString().trim();

                                if (TextUtils.isEmpty(emailId))
                                    {
                                        Toast.makeText(SignInActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                if (TextUtils.isEmpty(password))
                                    {
                                        Toast.makeText(SignInActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                mSignInProgress.setVisibility(View.VISIBLE);
                                mAuth.signInWithEmailAndPassword(emailId,password)
                                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task)
                                                {
                                                    if(!task.isSuccessful())
                                                        {
                                                            if(password.length()<6)
                                                                {
                                                                    mPasswordInput.setError(getString(R.string.minimum_password));
                                                                }
                                                            else
                                                                {
                                                                    Toast.makeText(SignInActivity.this,getString(R.string.auth_failed),Toast.LENGTH_SHORT).show();
                                                                }
                                                        }else
                                                        {
                                                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                                                            finish();
                                                        }
                                                }
                                        });

                            }
                    });
                mForgotButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {

                            }
                    });
                mSignupButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                            }
                    });
            }
//TODO:inactive sign in or up button before entering details
        //TODO:
        //TODO:
    }
