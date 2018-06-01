package com.dev.sb.blooddonationapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity
    {
        private TextInputEditText mInputEmail, mInputPassword;
        private Button mSignUpButton, mSignInButton, mResetPassButton;
        private ProgressBar mSignUpProgress;
        private FirebaseAuth mAuth;
        private Context mContext = SignUpActivity.this;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_up);
                mAuth = FirebaseAuth.getInstance();
                mInputEmail = findViewById(R.id.input_email);
                mInputPassword = findViewById(R.id.input_pass);
                mSignUpButton = findViewById(R.id.sign_up_button);
                mSignInButton = findViewById(R.id.sign_in_button);
                mResetPassButton = findViewById(R.id.reset_password_button);
                mSignUpProgress = findViewById(R.id.sign_up_progress);

                mSignInButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                            }
                    });
                mSignUpButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                final String email = mInputEmail.getText().toString().trim();
                                String password = mInputPassword.getText().toString().trim();
                                if (TextUtils.isEmpty(email))
                                    {
                                        Toast.makeText(mContext, R.string.enter_email, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                if (TextUtils.isEmpty(password))
                                    {
                                        Toast.makeText(mContext, R.string.enter_password, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                if (password.length() < 6)
                                    {
                                        Toast.makeText(mContext, R.string.minimum_password, Toast.LENGTH_SHORT).show();
                                    }
                                mSignUpProgress.setVisibility(View.VISIBLE);

                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task)
                                                    {
                                                        Toast.makeText(SignUpActivity.this, "On Complete" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                        mSignUpProgress.setVisibility(View.GONE);
                                                        if (!task.isSuccessful())
                                                            {
                                                                Toast.makeText(SignUpActivity.this, "Sign Up Failed" + task.getException(), Toast.LENGTH_SHORT).show();

                                                            } else
                                                            {
                                                                Toast.makeText(SignUpActivity.this, "done" + task.getException(), Toast.LENGTH_SHORT).show();
                                                                Intent intent=new Intent(SignUpActivity.this, UserProfileActivity.class)
                                                                        .putExtra("email",email);
                                                                startActivity(intent);
                                                                finish();

                                                            }

                                                    }
                                            });
                            }
                    });
                mResetPassButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {

                            }
                    });
            }

        @Override
        protected void onResume()
            {
                super.onResume();
                mSignUpProgress.setVisibility(View.GONE);
            }
    }
