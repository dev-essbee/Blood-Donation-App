package com.dev.sb.blooddonationapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class FilterDialogfragment extends DialogFragment {
    private TextInputEditText mLocation;
    private Spinner mBldGrp;
    private final int PLACE_REQUEST_CODE = 875;
    private String newLocation = "", newBldGrp = "";
    private LinearLayout mBldGrpLayout;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog_fragment, null);

        mLocation = view.findViewById(R.id.location_text);
        mBldGrp = view.findViewById(R.id.bld_grp_spinner);
        mBldGrpLayout = view.findViewById(R.id.linearLayoutbldGrpDialog);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.filter));
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent().putExtra("loc", newLocation).putExtra("bldGrp", newBldGrp);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Log.d("execution", "onCreateDialog");
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if (getTargetFragment().toString().contains("BloodBanksFragment")) {
            Log.d("dialog", getTargetFragment().toString());
            mBldGrpLayout.setVisibility(View.GONE);

        } else {
            bldGrpAdapter();
        }
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
            }
        });
        return v;
    }

    private void findLocation() {
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .build();
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e("Autocomplete Location", e.toString());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("Autocomplete Location", e.toString());
        }

    }

    private void bldGrpAdapter() {
        ArrayAdapter<CharSequence> bloodGroupAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.blood_groups,
                        android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBldGrp.setAdapter(bloodGroupAdapter);

        mBldGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newBldGrp = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Place place = PlaceAutocomplete.getPlace(this, data);
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                newLocation = place.getName().toString();
                mLocation.setText(newLocation);

                Log.d("Location Complete", newLocation);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.d("Location Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("Location", "Request cancelled");
            }
        }
    }
}
