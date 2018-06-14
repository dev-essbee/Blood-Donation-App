package com.dev.sb.blooddonationapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class BloodBanksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BloodBankAdapter mAdapter;
    private List<BloodBank> mBloodBankList = new ArrayList<>();
    private BloodBank bank;
    private FirebaseAuth mAuth;

    private final int FILTER_LOC_CODE = 321;
    private String name;
    private String newLoc;
    private String uid;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private String city;
    private ConstraintLayout mNegativeLayout;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bld_bnk, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.filter_menu):
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mContext = getContext();
        uid = mAuth.getCurrentUser().getUid();
        mShimmerFrameLayout = view.findViewById(R.id.bld_bnk_shimmer);
        mShimmerFrameLayout.startShimmer();

        mRecyclerView = view.findViewById(R.id.bld_bnk_recycler);
        mAdapter = new BloodBankAdapter(mBloodBankList);
        mRecyclerView.setHasFixedSize(true);
        mNegativeLayout = view.findViewById(R.id.negative_layout_bld_bnk);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity()
                .getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()
                .getApplicationContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (getContext());
        city = sharedPreferences.getString(getResources().getString(R.string.city_key), "Jaipur");
        prepareData(city);
    }

    private void showDialog() {
        DialogFragment dialogFragment = new FilterDialogfragment();
        dialogFragment.setTargetFragment(BloodBanksFragment.this, FILTER_LOC_CODE);
        dialogFragment.show(getFragmentManager(), "filter by loc");
    }

    private void prepareData(String recCity) {
        Log.d("city", "prepareData");

        new GetData().execute(recCity);

        //todo clear resources on on destroy like arraylist
    }

    private void loadJSONFromAsset(String recCity) {

        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("bloodbanks.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Oops! An error occured, while fetching the data.",
                    Toast.LENGTH_SHORT).show();
            //  return null;
            return;
        }
        try {
            JSONArray array = new JSONArray(json);
            JSONArray inArray;
            BloodBank bloodBank;
            Log.d("city", newLoc + " ");
            for (int i = 1; i <= 2823; i++) {
                inArray = array.getJSONArray(i);

                if (inArray.getString(4).toLowerCase().equals(recCity.toLowerCase())) {
                    String name = inArray.getString(1);
                    String phnNo = inArray.getString(8);
                    if (phnNo.equals("NA") || phnNo.equals("")) {
                        phnNo = inArray.getString(7);
                    }
                    if (phnNo.equals("NA") || phnNo.equals("")) {
                        phnNo = inArray.getString(15);
                    }
                    if (phnNo.equals("NA") || phnNo.equals("")) {
                        phnNo = inArray.getString(14);
                    }
                    if (phnNo.equals("")) {
                        Log.d("not found phone", name);
                    } else {
                        try {
                            phnNo = checkPhoneNo(phnNo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            phnNo = " ";
                        }
                    }
                    Log.d("city", phnNo);
                    String lat = inArray.getString(25);
                    String lon = inArray.getString(26);
                    String city = inArray.getString(4);
                    bloodBank = new BloodBank(name, city, phnNo, lat, lon);
                    mBloodBankList.add(bloodBank);
                    Log.d("city list", mBloodBankList.size() + "");
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_LOC_CODE) {
            if (resultCode == RESULT_OK) {

                if (!data.getStringExtra("loc").equals("")) {
                    newLoc = data.getStringExtra("loc");
                    mBloodBankList.clear();
                    prepareData(newLoc);
                }
            }
        }
    }


    private class GetData extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            loadJSONFromAsset(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setHasOptionsMenu(true);

            mShimmerFrameLayout.stopShimmer();
            mShimmerFrameLayout.setVisibility(View.GONE);
            if (mBloodBankList.size() == 0) {
                mNegativeLayout.setVisibility(View.VISIBLE);
            } else {
                mAdapter.notifyDataSetChanged();
                mNegativeLayout.setVisibility(View.GONE);
            }

        }
    }

    private String checkPhoneNo(String phn) {
       /* if (phn.contains(",") && phn.indexOf(",") >= 10) {
            phn = phn.substring(0, phn.indexOf(",") );
        } else if (phn.contains(" ") && phn.indexOf(" ") >= 10) {
            phn = phn.substring(0, phn.indexOf(" ") );
        } else if (phn.contains("-") && phn.indexOf("-") >= 10) {
            phn = phn.substring(0, phn.indexOf("-") );
        }*/
        Log.d("no", phn);
        if (phn.contains("-")) {
            phn = phn.replaceAll("-", "");
        }
        if (phn.contains(" ")) {
            phn = phn.replaceAll(" ", "");
        }
        if (phn.contains(",")) {
            phn = phn.replaceAll(",", "");
        }
        if (phn.contains("0") && phn.indexOf("0") == 0) {
            phn = phn.replace("0", "");

        }
        if (phn.length() >= 11) {
            phn = phn.substring(0, 10);
        }
        phn = "0" + phn;

        return phn;
    }

}
