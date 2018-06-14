package com.dev.sb.blooddonationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment //implements DeleteAccountDialogFragment.DeleteAccountDialogListener
{
    private TextView mNameTextView;
    private Button mDeleteButton, mChangeButton, mLogoutButton;
    private TextView mEmailModify;
    private ConstraintLayout mAvailableLayout, mEligibleLayout;
    private SwitchCompat mAvailableSwitch, mEligibleSwitch;
    private TextInputEditText mPhoneEditText, mEmailEditText, mCityEditText;
    private TextView mPhoneVerify;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private boolean logOut = false;
    private DatabaseReference mDatabase;
    private String id;
    private String email = "", phoneNo = "", name = "", eligible = "", verified = "", city = "";
    private final int PASS_FRAG = 585;
    private final int DEL_FRAG = 500;
    private final int CHANGE_EMAIL = 702;
    private boolean passwordChanged = false;
    private boolean phoneVerifyClicked = false;
    private String tempPhoneNo = "", verifiedPhoneNo = "";
    private String phoneVerified = "0";
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_SETTINGS = 111;
    private final int ELIGIBILITY_CHANGE = 121;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private int intEnteredYear, enteredMonth;
    private Boolean result;

    private ConstraintLayout mNegativeLayout;
    private Button mRetryButton;
    private ProgressBar mProgressBar;
    private Activity mActivity;
    private ConstraintLayout mPositiveLayout;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_eligibility_intent:
                Intent intent = new Intent(mActivity, EligibilityActivity.class);
                intent.putExtra("fragment", "menu");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //todo email is verified then show as donor and check verified for =1

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNameTextView = view.findViewById(R.id.text_name);
        mDeleteButton = view.findViewById(R.id.button_delete);
        mChangeButton = view.findViewById(R.id.button_change_password);
        mLogoutButton = view.findViewById(R.id.button_logout);
        mAvailableLayout = view.findViewById(R.id.layout_switch_available);
        mPhoneEditText = view.findViewById(R.id.text_phone);
        mEmailEditText = view.findViewById(R.id.text_email);
        mAvailableSwitch = view.findViewById(R.id.switch_available);
        mEligibleLayout = view.findViewById(R.id.layout_switch_eligible);
        mEligibleSwitch = view.findViewById(R.id.switch_eligible);
        mPhoneVerify = view.findViewById(R.id.phone_no_verify);
        mEmailModify = view.findViewById(R.id.email_id_modify);
        mCityEditText = view.findViewById(R.id.city_modify);
        mProgressBar = view.findViewById(R.id.profile_progress);
        mNegativeLayout = view.findViewById(R.id.negative_layout_profile);
        mRetryButton = view.findViewById(R.id.negative_button_profile);
        mPositiveLayout = view.findViewById(R.id.positive_layout_profile);

        mActivity = getActivity();
        setHasOptionsMenu(true);
        // todo To make text view uneditable
       /* mPhoneEditText.setTag(mPhoneEditText.getKeyListener());
        mPhoneEditText.setKeyListener(null);*/
        mShimmerFrameLayout = view.findViewById(R.id.profile_shimmer);
        mShimmerFrameLayout.startShimmer();
        mPhoneEditText.addTextChangedListener(phoneWatcher);
        mEmailEditText.addTextChangedListener(emailWatcher);
        if (checkConnection()) {

        } else {

        }
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        id = mAuth.getUid();

        passwordChanged = false;
        logOut = false;
        //todo: add i'm now eligible
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndDisplay();
            }
        });
        mEligibleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEligibleSwitch.isChecked() && !eligible.equals("1")) {
                    Intent intent = new Intent(mActivity, EligibilityActivity.class);
                    intent.putExtra("fragment", "ProfileFragment");
                    startActivityForResult(intent, ELIGIBILITY_CHANGE);
                } else {
                    eligible = "-1";
                    pushChange();
                    mAvailableLayout.setVisibility(View.GONE);
                }
            }
        });
        mAvailableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eligible = "1";
                    pushChange();
                } else {
                    eligible = "-2";
                    pushChange();
                }

            }
        });
        mCityEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setCountry("IN")
                        .build();
                try {

                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(mActivity);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_SETTINGS);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e("Autocomplete Location", e.toString());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e("Autocomplete Location", e.toString());
                }
            }
        });
        mDeleteButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mDeleteButton.setTextColor(getResources().getColor(R.color.icons));
                    Toast.makeText(getContext(), "focus", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPhoneVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("text", "phone clicked");

// todo For making text view  editable
/*                mPhoneEditText.setKeyListener((KeyListener) mPhoneEditText.getTag());
                mPhoneEditText.requestFocus();
                mPhoneEditText.selectAll();
                InputMethodManager inputMethodManager=(InputMethodManager)mActivity
                        .getSystemService
                        (Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mPhoneEditText,InputMethodManager.SHOW_IMPLICIT);*/
                if (phoneVerified.equals("1")) {
                    Toast.makeText(mActivity.getApplicationContext(), "Mobile Number already verified", Toast
                            .LENGTH_SHORT).show();
                } else {
                    tempPhoneNo = mPhoneEditText.getText().toString().trim();
                    verifyNo(tempPhoneNo);
                }
            }
        });
        checkAndDisplay();


        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.addAuthStateListener(mAuthStateListener);
                DialogFragment dialogFragment = new DeleteAccountDialogFragment();
                dialogFragment.setTargetFragment(ProfileFragment.this, DEL_FRAG);
                dialogFragment.show(getFragmentManager(), "delete account");

            }
        });

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new ChangePassDialogFragment();
                dialogFragment.setTargetFragment(ProfileFragment.this, PASS_FRAG);
                dialogFragment.show(getFragmentManager(), "change password");
            }
        });
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.addAuthStateListener(mAuthStateListener);
                logOut = true;
                mAuth.signOut();
            }
        });
        mEmailModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new ChangeEmailDialogFragment();
                dialogFragment.setTargetFragment(ProfileFragment.this, CHANGE_EMAIL);
                dialogFragment.show(getFragmentManager(), "change email");
            }
        });
//todo handle excpetion of firebase in login logout and data uses


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PASS_FRAG) {
            if (resultCode == RESULT_OK) {
                final String pass = data.getStringExtra("pass");
                final String oldPass = data.getStringExtra("oldPass");
                Log.d("user", "in activity result above" + oldPass + pass);
                mAuth.removeAuthStateListener(mAuthStateListener);
                if (oldPass.length() >= 6) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    deactivateTouch();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);
                    Log.d("userabo", email + " " + oldPass);
                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("userbel", email + " " + oldPass);
                            if (task.isSuccessful()) {
                                updatePass(pass);

                                //   mConstraintLayout.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(mActivity, "Wrong existing password!", Toast
                                        .LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                                activateTouch();
                            }
                        }
                    });
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    activateTouch();
                    Toast.makeText(mActivity, "New Password must be at least 6 characters long." +
                            "!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CHANGE_EMAIL) {
            final String pass = data.getStringExtra("pass");
            Log.d("user", "in activity result above" + pass);
            if ((pass != null || !pass.equals(""))) {
                mProgressBar.setVisibility(View.VISIBLE);
                deactivateTouch();
                AuthCredential credential = EmailAuthProvider.getCredential(email, pass);
                Log.d("userabo", email + " " + pass);
                mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("userbel", email + " " + pass);
                        if (task.isSuccessful()) {
                            changeEmail();

                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            activateTouch();

                            Toast.makeText(mActivity, "Wrong existing password!", Toast
                                    .LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else if (requestCode == DEL_FRAG) {
            final String pass = data.getStringExtra("pass");
            Log.d("user", "in activity result above" + pass);
            if ((pass != null || !pass.equals(""))) {
                mProgressBar.setVisibility(View.VISIBLE);
                deactivateTouch();

                AuthCredential credential = EmailAuthProvider.getCredential(email, pass);
                Log.d("userabo", email + " " + pass);
                mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("userbel", email + " " + pass);
                        if (task.isSuccessful()) {
                            Log.d("userbelbel", email + " " + pass);
                            delAcc();
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            activateTouch();
                            Toast.makeText(mActivity, "Wrong existing password!", Toast
                                    .LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mActivity, data);
                Log.d("Location Complete", place.getName().toString());
                mCityEditText.setText(place.getName().toString());
                mDatabase.child(id).child("city").setValue(place.getName().toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mActivity, data);

                Log.d("Location Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("Location", "Request cancelled");
            }
        } else if (requestCode == ELIGIBILITY_CHANGE) {
            if (resultCode == RESULT_OK) {

                if (data.getStringExtra("result").equals("1")) {
                    eligible = "1";
                    pushChange();
                    mAvailableSwitch.setChecked(true);
                    mAvailableLayout.setVisibility(View.VISIBLE);
                    Log.d("result", "check");
                } else {
                    Log.d("result", "uncheck");
                    eligible = "-1";
                    pushChange();
                    mEligibleSwitch.setChecked(false);
                    mAvailableLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (mAuth.getCurrentUser() != null) {
                if (!dataSnapshot.child(mAuth.getUid()).exists()) {
                    Log.d("user", "data doesn't exists");
                    try {
                        startActivity(new Intent
                                (mActivity, UserProfileActivity.class));
                        mActivity.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    email = mUser.getEmail();
                    tempPhoneNo = dataSnapshot.child(id).child("phNo").getValue(String.class);
                    name = dataSnapshot.child(id).child("name").getValue(String.class);
                    eligible = dataSnapshot.child(id).child("eligible").getValue(String.class);
                    verified = dataSnapshot.child(id).child("verified").getValue(String.class);
                    city = dataSnapshot.child(id).child("city").getValue(String.class);
                    String temp = dataSnapshot.child(id).child("dob").getValue(String.class);
                    Log.d("temp", temp + " " + id);
                    enteredMonth = Integer.parseInt(temp.substring(0, 2));
                    intEnteredYear = Integer.parseInt(temp.substring(3));
                    Log.d("data change", email + phoneNo);
//todo check old age in donor fragment also when obtaining data of donors
                    mEmailEditText.setText(email);
                    mCityEditText.setText(city);

                    if (verified.equals("1")) {
                        mPhoneVerify.setText(getResources().getString(R.string.phone_verified_txtView));
                        mPhoneVerify.setTextColor(getResources().getColor(R.color.button_boundary));
                        verifiedPhoneNo = tempPhoneNo;
                    } else {
                        mPhoneVerify.setText(getResources().getString(R.string.verify));
                        mPhoneVerify.setTextColor(getResources().getColor(R.color.primary_dark));
                    }
                    mPhoneEditText.setText(verifiedPhoneNo);
                    mNameTextView.setText(name);
                    mShimmerFrameLayout.stopShimmer();
                    mShimmerFrameLayout.setVisibility(View.GONE);
                    String checkAgeResult = checkAge();
                    if (eligible.equals("-1")) {
                        mAvailableLayout.setVisibility(View.GONE);
                        mEligibleSwitch.setChecked(false);
                    } else if (eligible.equals("1") || eligible.equals("-2")) {
                        mEligibleSwitch.setChecked(true);
                        mAvailableLayout.setVisibility(View.VISIBLE);
                        if (eligible.equals("-2")) {
                            mAvailableSwitch.setChecked(false);
                        } else {
                            mAvailableSwitch.setChecked(true);
                        }
                    } else if (eligible.equals("0")) {
                        mEligibleSwitch.setChecked(false);
                        mAvailableLayout.setVisibility(View.GONE);
                    } else if (eligible.equals("-3")) {
                        if (checkAgeResult.equals("-3")) {
                            mEligibleLayout.setVisibility(View.GONE);
                            mAvailableLayout.setVisibility(View.GONE);
                        } else if (checkAgeResult.equals("0")) {
                            mEligibleLayout.setVisibility(View.VISIBLE);
                            mAvailableLayout.setVisibility(View.GONE);
                            mDatabase.child(id).child("eligible").setValue("0");
                        }
                    }
                    if (checkAgeResult.equals("-4")) {
                        mEligibleLayout.setVisibility(View.GONE);
                        mAvailableLayout.setVisibility(View.GONE);
                        mDatabase.child(id).child("eligible").setValue("-4");
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            Log.d("user", "in auth state listener");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                if (logOut) {
                    Toast.makeText(mActivity, "You have been " +
                            "successfully " +
                            "Logged Out.", Toast.LENGTH_SHORT).show();
                    mAuth.removeAuthStateListener(mAuthStateListener);
                    startActivity(new Intent(mActivity, SignInActivity.class));
                    mActivity.finish();
                    //todo use snackbar instead of toast
                }
            }
        }
    };

    private void successVerification() {
        mDatabase.child(id).child("verified").setValue("1");
        mPhoneVerify.setText(getResources().getText(R.string.phone_verified_txtView));
        mPhoneVerify.setTextColor(getResources().getColor(R.color.button_boundary));
        mDatabase.child(id).child("phNo").setValue(verifiedPhoneNo);
    }

    //todo: using birthdate instead of age
    private void postPhoneVerificationSettings(String status) {
        if (status.equals("1")) {
            new ProfileFragment().successVerification();
        }
    }
//todo dialog in profile keep edition or discard

    private TextWatcher phoneWatcher = new TextWatcher() {
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
                Log.d("verify", "aftertextchangedtotal");
            } else

            {
                mPhoneVerify.setText(getResources().getText(R.string.verify));
                mPhoneVerify.setTextColor(getResources().getColor(R.color.primary_dark));
                phoneVerified = "0";
                Log.d("verify", "aftertextchangedpartial");
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
            if (!s.toString().equals(email)) {
                mEmailModify.setVisibility(View.VISIBLE);
            } else {
                mEmailModify.setVisibility(View.GONE);
            }

        }
    };

    private void verifyNo(String inputPhoneNo) {
        inputPhoneNo = "+91" + inputPhoneNo;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(inputPhoneNo, 60, TimeUnit.SECONDS,
                mActivity,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    Log.d("verify", "phone no verified");
                    Toast.makeText(getContext(), "Phone Number Verified!", Toast
                            .LENGTH_SHORT).show();
                    verifiedPhoneNo = tempPhoneNo;
                    postPhoneVerificationSettings("1");

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {

                    postPhoneVerificationSettings("-1");

                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Log.d("verify", "code sent");
                    Toast.makeText(getContext(), "Verification Code sent", Toast
                            .LENGTH_SHORT).show();
                }
            };

    private void pushChange() {
        if (eligible.equals("1")) {
            mDatabase.child(id).child("eligible").setValue("1");
        } else if (eligible.equals("-1")) {
            mDatabase.child(id).child("eligible").setValue("-1");
        } else if (eligible.equals("-2")) {
            mDatabase.child(id).child("eligible").setValue("-2");
        }
    }

    private String checkAge() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        currentMonth++;
        Log.d("date", currentMonth + " " + currentYear + " " + enteredMonth + " " + intEnteredYear +
                "");
        if (currentYear - intEnteredYear == 18) {
            if (currentMonth <= enteredMonth) {
                Log.d("date", "true");
                return "-3";
            }
        } else if (currentYear - intEnteredYear < 18) {
            return "-3";
        } else if (currentYear - intEnteredYear == 60) {
            if (currentMonth >= enteredMonth) {

                return "-4";
            }
        } else if (currentYear - intEnteredYear > 60) {
            return "-4";
        }

        return "0";

    }

    private void updatePass(final String pass) {
        mUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("user", "in activity result down");


                if (task.isSuccessful()) {
                    Toast.makeText(mActivity, "Password Changed Successfully.",
                            Toast.LENGTH_SHORT).show();
                    Log.d("user", "in activity result down2");
                    mAuth.signOut();
                    mProgressBar.setVisibility(View.GONE);
                    activateTouch();
                    startActivity(new Intent(mActivity, SignInActivity.class));
                    passwordChanged = false;
                    mActivity.finish();
                } else {
                    Toast.makeText(mActivity, "Check your old Password and try again",
                            Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void delAcc() {
        mDatabase.child(id).removeValue();
        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(mActivity, "Your account has been " +
                        "successfully deleted.", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                mProgressBar.setVisibility(View.GONE);
                activateTouch();
                startActivity(new Intent(mActivity, SignInActivity.class));
                mActivity.finish();
            }
        });
    }

    private void changeEmail() {
        final String tempEmail = mEmailEditText.getText().toString().trim();
        Log.d("newemailclicked", "" + tempEmail + email);
        mUser.updateEmail(tempEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("newemail", "" + tempEmail + email);
                            email = tempEmail;
                            mEmailEditText.setText(email);
                            mProgressBar.setVisibility(View.GONE);
                            activateTouch();
                            Toast.makeText(mActivity, "Your email id has been updated " +
                                    "successfully.", Toast.LENGTH_SHORT).show();
                        }else{
                            mProgressBar.setVisibility(View.GONE);
                            mEmailEditText.setText(email);
                            if(task.getException().toString().contains("email address is")){
                                Toast.makeText(mActivity,"The email address is already in use by" +
                                        " another account.",Toast.LENGTH_LONG).show();
                            }
                            Log.d("email",task.getException().toString());
                        }
                    }

                });

    }

    private void deactivateTouch() {
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void activateTouch() {
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private boolean checkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity
                .getSystemService(Context
                        .CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            return false;
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void displayInternetError() {
        mShimmerFrameLayout.stopShimmer();
        mShimmerFrameLayout.setVisibility(View.GONE);
        mNegativeLayout.setVisibility(View.VISIBLE);

    }

    private void displayResult() {
        mNegativeLayout.setVisibility(View.GONE);
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.stopShimmer();


    }

    private void checkAndDisplay() {
        if (checkConnection()) {
            mDatabase.addListenerForSingleValueEvent(valueEventListener);
            displayResult();
        } else {
            displayInternetError();
        }
    }
}
