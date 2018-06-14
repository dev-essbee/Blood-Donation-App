package com.dev.sb.blooddonationapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class UserProfileActivity extends AppCompatActivity {
    private TextInputEditText mInputName, mInputDOB, mInputPhn, mInputLocation;
    private Spinner mGender, mBloodGrp;
    private Button mSubmit, mClear;
    private TextView mPhoneVerify, mGenderLabel;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 001;
    private UserDetails user = new UserDetails();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String phoneVerified = "0";
    private String verifiedPhoneNo = "", tempPhoneNo = "";
    private Boolean dateCorrect = true;
    private int enteredMonth = 1, intEnteredYear = 1;
    private ActionBar mActionBar;

    //verified 1: phoneNoVerified, -1: wrong no, 0:not sent code. -2:new no
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                mInputLocation.setText(place.getName().toString());
                user.setCity(place.getName().toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);



            } else if (resultCode == RESULT_CANCELED) {

            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mActionBar=getSupportActionBar();
       setTitle(getResources().getString(R.string.create_profile));

        mInputName = findViewById(R.id.input_name);
        mInputDOB = findViewById(R.id.input_dob);
        mInputPhn = findViewById(R.id.input_ph_no);
        mGender = findViewById(R.id.spinner_gender);
        mBloodGrp = findViewById(R.id.spinner_blood_grp);
        mSubmit = findViewById(R.id.button_submit);
        mClear = findViewById(R.id.button_clear);
        mInputLocation = findViewById(R.id.input_location);
        mPhoneVerify = findViewById(R.id.profile_phone_no_verify);

        mInputDOB.addTextChangedListener(textWatcher);
        mInputName.addTextChangedListener(textWatcher);
        mInputPhn.addTextChangedListener(textWatcher);
        mInputLocation.addTextChangedListener(textWatcher);

        mSubmit.setEnabled(false);
        mGenderLabel = findViewById(R.id.gender_label);
        verifiedPhoneNo = "";
        tempPhoneNo = "";
        mInputPhn.addTextChangedListener(mTextWatcher);

        mPhoneVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneVerified.equals("0")) {
                    tempPhoneNo = mInputPhn.getText().toString().trim();
                    if (TextUtils.getTrimmedLength(tempPhoneNo) == 10) {
                        if (checkConnection()) {
                            verifyNo(tempPhoneNo);
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Please check your Internet" +
                                    " connection and try again!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UserProfileActivity.this, "Please enter a valid 10 digit " +
                                "number.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserProfileActivity.this, "Mobile Number already verified.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputName.setText("");
                mInputDOB.setText("");
                mInputPhn.setText("");
                phoneVerified = "0";
                mInputLocation.setText("");
                mGender.setSelection(0);
                mBloodGrp.setSelection(0);
                dateCorrect = true;

            }
        });
        mInputDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mInputDOB.getText().toString().isEmpty()) {

                        mInputDOB.setHint(getResources().getString(R
                                .string
                                .dob_format));
                    }
                } else {
                    mInputDOB.setHint("");
                }
            }
        });
        ArrayAdapter<CharSequence> genderAdapter =
                ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> bloodGroupAdapter =
                ArrayAdapter.createFromResource(this, R.array.blood_groups, android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodGrp.setAdapter(bloodGroupAdapter);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                saveData(setUpFirebase());

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        mInputLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setCountry("IN")
                        .build();
                try {

                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(UserProfileActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                  e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                  e.printStackTrace();
                }

            }
        });

        mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                user.setGender(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                user.setBloodGrp(parent.getItemAtPosition(position).toString());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getData() {

        //eligibility=1:eligible, 0: not filled, -1: ineligible, -2:not available, -3:<18 years old
        user.setName(mInputName.getText().toString().trim());
        user.setDob(mInputDOB.getText().toString().trim());
        user.setPhNo(verifiedPhoneNo);
        user.setVerified(phoneVerified);
        user.setEligible(checkAge());


    }


    private String setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        final String ID;
        try {
            ID = mAuth.getCurrentUser().getUid();
        } catch (NullPointerException e) {

            return null;
        }
        return ID;
    }


    private void saveData(final String ID) {
        if (ID != null && phoneVerified.equals("1")) {
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            mDatabase.child(ID).setValue(user);
        }

    }

    private void postPhoneVerificationUserProfile(String status) {
        if (status.equals("1")) {
            phoneVerified = "1";

            mPhoneVerify.setText(getResources().getText(R.string.phone_verified_txtView));
            mPhoneVerify.setTextColor(getResources().getColor(R.color.button_boundary));
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 10 && s.toString().equals(verifiedPhoneNo)) {
                mPhoneVerify.setText(getResources().getText(R.string.phone_verified_txtView));
                mPhoneVerify.setTextColor(getResources().getColor(R.color.button_boundary));
                phoneVerified = "1";

            } else

            {
                mPhoneVerify.setText(getResources().getText(R.string.verify));
                mPhoneVerify.setTextColor(getResources().getColor(R.color.primary_dark));
                phoneVerified = "0";

            }
        }
    };



    private void verifyNo(String inputPhoneNo) {

        inputPhoneNo = "+91" + inputPhoneNo;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(inputPhoneNo, 60, TimeUnit.SECONDS,
                UserProfileActivity.this,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    Toast.makeText(UserProfileActivity.this, "Phone Number Verified!", Toast
                            .LENGTH_SHORT).show();
                    verifiedPhoneNo = tempPhoneNo;
                    postPhoneVerificationUserProfile("1");

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {

                    postPhoneVerificationUserProfile("-1");


                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    Toast.makeText(UserProfileActivity.this, "Verification Code sent", Toast
                            .LENGTH_SHORT).show();
                }
            };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            if (!mInputName.getText().toString().isEmpty()) {
                if (!mInputDOB.getText().toString().isEmpty()) {
                    if (dateCorrect && mInputDOB.getText().toString().length() == 7) {
                        if (!mInputLocation.getText().toString().isEmpty()) {
                            if (!mInputPhn.getText().toString().isEmpty()) {
                                if (phoneVerified.equals("1")) {
                                    mSubmit.setBackground(getResources().getDrawable(R.drawable
                                            .pri_button));
                                    mSubmit.setEnabled(true);
                                } else {
                                   mSubmit.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                                    mSubmit.setEnabled(true);
                                }
                            } else {
                                mSubmit.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                                mSubmit.setEnabled(false);
                            }
                        } else {
                            mSubmit.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                            mSubmit.setEnabled(false);
                        }
                    } else {
                        mSubmit.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                        mSubmit.setEnabled(false);
                    }
                } else {
                    mSubmit.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                    mSubmit.setEnabled(false);
                }
            } else {
                mSubmit.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
                mSubmit.setEnabled(false);

            }

            if (!mInputDOB.getText().toString().isEmpty()) {
                String dob = mInputDOB.getText().toString();

                dateCorrect = true;
                if (dob.length() == 2 && before == 0) {
                    enteredMonth = Integer.parseInt(dob);
                    if (enteredMonth < 1 || enteredMonth > 12) {
                        dateCorrect = false;
                    } else {
                        dob += "/";
                        mInputDOB.setText(dob);
                        mInputDOB.setSelection(dob.length());
                    }
                } else if (dob.length() == 7 && before == 0) {
                    String enteredYear = dob.substring(3);
                    Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);
                    int currentMonth = calendar.get(Calendar.MONTH);

                    intEnteredYear = Integer.parseInt(enteredYear);

                    if (intEnteredYear > currentYear || intEnteredYear < currentYear - 150
                            || (intEnteredYear == currentYear && enteredMonth > currentMonth)) {

                        dateCorrect = false;
                    }
                }

                if (dob.length() == 2 || dob.length() == 7) {
                    if (!dateCorrect) {
                        mInputDOB.setError("Enter a valid date: MM/YYYY");
                    } else {
                        mInputDOB.setError(null);
                    }
                }
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            return false;
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private String checkAge() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        currentMonth++;
        if (currentYear - intEnteredYear <= 18) {
            if (currentMonth >= enteredMonth) {
                return "-3";
            }
        } else if (currentYear - intEnteredYear == 60) {
            if (currentMonth < enteredMonth) {
                return "-4";
            }
        }
        return "0";
    }
    //eligibility 1 eligible, 0 not checked, -1 not eligible -2 eligible not available -3 minor
    // -4 old age
}

