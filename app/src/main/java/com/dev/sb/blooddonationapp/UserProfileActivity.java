package com.dev.sb.blooddonationapp;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class UserProfileActivity extends AppCompatActivity
    {
        private TextInputEditText mInputFirName, mInputLastName, mInputAge, mInputPhn;
        private Spinner mGender, mBloodGrp;
        private Button mSubmit, mClear;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_user_profile);
                mInputFirName = findViewById(R.id.input_fir_name);
                mInputLastName = findViewById(R.id.input_last_name);
                mInputAge = findViewById(R.id.input_age);
                mInputPhn = findViewById(R.id.input_ph_no);
                mGender = findViewById(R.id.spinner_gender);
                mBloodGrp = findViewById(R.id.spinner_blood_grp);
                mSubmit = findViewById(R.id.button_submit);
                mClear = findViewById(R.id.button_clear);

                ArrayAdapter<CharSequence> genderAdapter =
                        ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
                genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mGender.setAdapter(genderAdapter);

                ArrayAdapter<CharSequence> bloodGroupAdapter =
                        ArrayAdapter.createFromResource(this, R.array.blood_groups, android.R.layout.simple_spinner_item);
                bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBloodGrp.setAdapter(bloodGroupAdapter);

                mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            Log.d("Spinner selection",parent.getItemAtPosition(position)+"");
                        }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                });
                mBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            Log.d("Spinner selection blood",parent.getItemAtPosition(position)+"");
                        }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                });
            }

            }

