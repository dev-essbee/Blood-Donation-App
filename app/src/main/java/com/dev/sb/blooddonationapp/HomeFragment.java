package com.dev.sb.blooddonationapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DonorAdapter mAdapter;
    private List<Donors> mDonorsArrayList = new ArrayList<>();
    private LinearLayout mContactLayout;
    private FloatingActionButton mfilterButton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String recBldGrp;
    private String recCity;
    private String recId;
  /*  private ConstraintLayout mEligibility;*/
    private Button mEligibleFillPositive, mEligibleLaterPositive;
    private Button mEligibleFillNegative, mEligibleLaterNegative;
    private String eligibilityValue = "1";
    private final int ELIGIBILITY_REQUEST_CODE = 101;
    private final int FILTER_LOC_BLD_CODE = 342;
    private String newLoc = "", newBldGrp = "";
    private ShimmerFrameLayout mShimmerFrameLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private ConstraintLayout mNegativeLayout;
    private Button mRetryButton;
    private Context mContext;
    private ImageView mNegativeImageView;
    private TextView mNegativeTextView;
    private View mPositiveEligibleLayout, mNegativeEligibleLayout;
    private ConstraintLayout mEligibilityPositive,mEligibilityNegative;


    //todo taking name as input first character should be albhabet
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mInflate = inflater.inflate(R.layout.fragment_home, container, false);
        return mInflate;
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

    //todo intent to email app and verification email click verification link and open app signed in
    @Override
    public void onResume() {
        super.onResume();
        mShimmerFrameLayout.startShimmer();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      /*  mEligibility = view.findViewById(R.id.eligible_ask);*/


        mPositiveEligibleLayout=view.findViewById(R.id.positive_layout_eligible);
        mNegativeEligibleLayout=view.findViewById(R.id.negative_layout_eligible);

        mEligibleFillPositive = mPositiveEligibleLayout.findViewById(R.id.eligible_fill);
        mEligibleLaterPositive = mPositiveEligibleLayout.findViewById(R.id.eligible_later);

        mEligibleFillNegative = mNegativeEligibleLayout.findViewById(R.id.eligible_fill);
        mEligibleLaterNegative = mNegativeEligibleLayout.findViewById(R.id.eligible_later);

        mEligibilityPositive=mPositiveEligibleLayout.findViewById(R.id.eligible_ask);
        mEligibilityNegative=mNegativeEligibleLayout.findViewById(R.id.eligible_ask);

        mShimmerFrameLayout = view.findViewById(R.id.home_shimmer);
        mNegativeLayout = view.findViewById(R.id.negative_layout_home);
        mRetryButton = view.findViewById(R.id.negative_button_home);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("users");
        mContext = getContext();
        mAuth = FirebaseAuth.getInstance();
        mNegativeImageView = view.findViewById(R.id.negative_image_home);
        mNegativeTextView = view.findViewById(R.id.negative_txt_home);

//mShimmerFrameLayout.startShimmer();
        mRecyclerView = view.findViewById(R.id.recycler_View);
        mAdapter = new DonorAdapter(mDonorsArrayList);
        mRecyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getActivity()
                .getApplicationContext());

        mRecyclerView.setLayoutManager(LayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()
                .getApplicationContext(), LinearLayoutManager.VERTICAL));


        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity()
                .getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity().getApplicationContext(), position + "selected", Toast
                        .LENGTH_SHORT).show();
                showButtomSheetDialogFragment(position);
            }
        }));
        mRecyclerView.setAdapter(mAdapter);
        checkAndDisplay();
       /* if (checkConnection()) {
            prepareData();
        } else {
            mShimmerFrameLayout.stopShimmer();
            mShimmerFrameLayout.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please check your Internet Connection.", Toast
                    .LENGTH_LONG).show();
        }*/
        //TODO: Implement floating action button on click listener


        mEligibleFillPositive.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        EligibilityActivity.class);
                intent.putExtra("fragment", "HomeFragment");
                startActivityForResult(intent, ELIGIBILITY_REQUEST_CODE);
            }
        });
        mEligibleFillNegative.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        EligibilityActivity.class);
                intent.putExtra("fragment", "HomeFragment");
                startActivityForResult(intent, ELIGIBILITY_REQUEST_CODE);
            }
        });
        mEligibleLaterPositive.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                mPositiveEligibleLayout.setVisibility(View.GONE);
                mNegativeEligibleLayout.setVisibility(View.GONE);
            }
        });
        mEligibleLaterNegative.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                mPositiveEligibleLayout.setVisibility(View.GONE);
                mNegativeEligibleLayout.setVisibility(View.GONE);
            }
        });
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndDisplay();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ELIGIBILITY_REQUEST_CODE && resultCode == RESULT_OK) {
            eligibilityValue = data.getStringExtra("result");
            Log.d("eligi :", eligibilityValue + "");
            mDatabaseReference.child(mAuth.getUid()).child("eligible").setValue(eligibilityValue);
            visibility(eligibilityValue);

        }
        if (requestCode == FILTER_LOC_BLD_CODE) {
            if (resultCode == RESULT_OK) {
                newLoc = data.getStringExtra("loc");
                newBldGrp = data.getStringExtra("bldGrp");
                //   prepareData();
                checkAndDisplay();
            }
        }
    }

    public void showButtomSheetDialogFragment(int position) {
        Bundle args = new Bundle();
        Donors details = mDonorsArrayList.get(position);
        args.putString("phNo", mDonorsArrayList.get(position).getPhNo());
        args.putString("name", details.getName());
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.setArguments(args);
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

    }//todo data updated toast message on updating data everywhere like in recycler view

    //todo create on stop to clear all global variables in all activities
    private void prepareData() {
        mDonorsArrayList.clear();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mAuth.getCurrentUser() != null) {
                    if (!dataSnapshot.child(mAuth.getUid()).exists()) {
                        Log.d("user", "data doesn't exists");
                        try {
                            startActivity(new Intent
                                    (mContext, UserProfileActivity.class));
                            getActivity().finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("user", "data exist");
                        mDonorsArrayList.clear();
                        recId = mAuth.getUid();
                        if (recId != null) {
                            if (!newLoc.equals("")) {
                                recCity = newLoc;
                            } else {
                                recCity = dataSnapshot.child(recId).child("city").getValue(String.class);
                            }
                            if (!newBldGrp.equals("")) {
                                recBldGrp = newBldGrp;
                            } else {
                                recBldGrp = dataSnapshot.child(recId).child("bloodGrp").getValue(String.class);
                            }
                            eligibilityValue = dataSnapshot.child(recId).child("eligible").getValue(String.class);

                            if (eligibilityValue != null) {
                                visibility(eligibilityValue);
                                Log.d("data rec", recCity + recBldGrp + "" + eligibilityValue);
                                //eligibility=1:eligible, 0: not filled, -1: ineligible
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String uid = dataSnapshot1.getKey();
                                    //todo: multiple times same data if data updated in real time
                                    //todo: test everythink without internet connectivity
                                    UserDetails user = dataSnapshot1.getValue(UserDetails.class);
                                    if (user.getCity().equals(recCity) && user.getBloodGrp().equals
                                            (recBldGrp) && user.getEligible().equals("1") && !uid.equals(recId)) {
                                        Log.d("data uid", uid);
                                        Donors donors = dataSnapshot1.getValue(Donors.class);
                                        mDonorsArrayList.add(donors);

                                    }
                                }
                                mShimmerFrameLayout.stopShimmer();
                                mShimmerFrameLayout.setVisibility(View.GONE);
                                setHasOptionsMenu(true);
                                mAdapter.notifyDataSetChanged();
                                if (mDonorsArrayList.size() == 0) {
                                    noResult();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    private void visibility(String eligibilityValue) {
        if (eligibilityValue.equals("0")) {

            mPositiveEligibleLayout.setVisibility(View.VISIBLE);
            mNegativeEligibleLayout.setVisibility(View.VISIBLE);
        } else {

            mPositiveEligibleLayout.setVisibility(View.GONE);
            mNegativeEligibleLayout.setVisibility(View.GONE);
        }

    }

    private void showDialog() {
        DialogFragment dialogFragment = new FilterDialogfragment();
        dialogFragment.setTargetFragment(HomeFragment.this, FILTER_LOC_BLD_CODE);
        dialogFragment.show(getFragmentManager(), "filter by loc and bldGrp");
    }

    private boolean checkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
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
        mPositiveEligibleLayout.setVisibility(View.GONE);
        mNegativeEligibleLayout.setVisibility(View.GONE);

    }

    private void displayResult() {
        mNegativeLayout.setVisibility(View.GONE);
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.startShimmer();
        prepareData();

    }

    private void noResult() {
        mNegativeLayout.setVisibility(View.VISIBLE);
        mNegativeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_data));
        mRetryButton.setVisibility(View.GONE);
        mNegativeTextView.setText(getResources().getString(R.string.no_donor));
    }

    private void checkAndDisplay() {
        if (checkConnection()) {
            displayResult();
        } else {
            displayInternetError();
        }
    }

}
