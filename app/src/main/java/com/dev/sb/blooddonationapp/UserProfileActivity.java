package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {
    private TextInputEditText mInputName, mInputAge, mInputPhn, mInputLocation;
    private Spinner mGender, mBloodGrp;
    private Button mSubmit, mClear;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 001;
    private UserDetails user = new UserDetails();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.d("Location Complete", place.getName().toString());
                mInputLocation.setText(place.getName().toString());
                user.setCity(place.getName().toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Log.d("Location Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("Location", "Request cancelled");
            }

        }
    }

    //TODO: Add whatsapp button along with call, messagae, email and share

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mInputName = findViewById(R.id.input_name);
        mInputAge = findViewById(R.id.input_age);
        mInputPhn = findViewById(R.id.input_ph_no);
        mGender = findViewById(R.id.spinner_gender);
        mBloodGrp = findViewById(R.id.spinner_blood_grp);
        mSubmit = findViewById(R.id.button_submit);
        mClear = findViewById(R.id.button_clear);
        mInputLocation = findViewById(R.id.input_location);

        String email = getIntent().getStringExtra("email");
        if (email != null) {
            user.setEmail(email);
        }

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
                    Log.e("Autocomplete Location", e.toString());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e("Autocomplete Location", e.toString());
                }

            }
        });


        mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner selection", parent.getItemAtPosition(position) + "");
                user.setGender(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner selection blood", parent.getItemAtPosition(position) + "");
                user.setBloodGrp(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //TODO:Verify phone number and email id.
    private void getData() {
        user.setName(mInputName.getText().toString().trim());
        user.setAge(mInputAge.getText().toString().trim());
        user.setPhNo(mInputPhn.getText().toString().trim());
        user.setEligible(false);
    }

    private String setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        final String ID;
        try {
            ID = mAuth.getCurrentUser().getUid();
        } catch (NullPointerException e) {
            Log.e("Firebase user", "" + e);
            return null;
        }
        return ID;
    }
//TODO: Seperate data seperately as private meta and public.
    private void saveData(final String ID) {
        if (ID != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("users");

            mDatabase.child(ID).setValue(user);
        }
    }

}

