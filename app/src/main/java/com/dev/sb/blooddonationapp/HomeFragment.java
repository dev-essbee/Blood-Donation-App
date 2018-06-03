package com.dev.sb.blooddonationapp;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


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
    private LinearLayout mEligibility;
    private Button mEligibleFill, mEligibleLater;
    private int eligibilityValue = 1;
    private final int ELIGIBILITY_REQUEST_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mInflate = inflater.inflate(R.layout.fragment_home, container, false);
        return mInflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mEligibility = view.findViewById(R.id.eligible_ask);
        mEligibleFill = view.findViewById(R.id.eligible_fill);
        mEligibleLater = view.findViewById(R.id.eligible_later);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        mfilterButton = view.findViewById(R.id.filter_fab);

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
        prepareData();
        //TODO: Implement floating action button on click listener
        mfilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEligibleFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        EligibilityActivity.class);
                startActivityForResult(intent, ELIGIBILITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ELIGIBILITY_REQUEST_CODE) {
            eligibilityValue=data.getIntExtra("result",0);
            Log.d("eligi :",eligibilityValue +"");
            mDatabaseReference.child(mAuth.getUid()).child("eligible").setValue(eligibilityValue);
            visibility(eligibilityValue);

        }
    }

    public void showButtomSheetDialogFragment(int position) {
        Bundle args = new Bundle();
        args.putString("phNo", mDonorsArrayList.get(position).getPhNo());
        args.putString("email", mDonorsArrayList.get(position).getEmail());
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.setArguments(args);
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }

    private void prepareData() {

        recId = mAuth.getUid();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recCity = dataSnapshot.child(recId).child("city").getValue(String.class);
                recBldGrp = dataSnapshot.child(recId).child("bloodGrp").getValue(String.class);
                eligibilityValue = dataSnapshot.child(recId).child("eligible").getValue(Integer.class);


                visibility(eligibilityValue);
                Log.d("data rec", recCity + recBldGrp + "" + eligibilityValue);
                //eligibility=1:eligible, 0: not filled, -1: ineligible
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String uid = dataSnapshot1.getKey();
                    //todo: multiple times same data if data updated in real time
                    //todo: test everythink without internet connectivity
                    UserDetails user = dataSnapshot1.getValue(UserDetails.class);
                    if (user.getCity().equals(recCity) && user.getBloodGrp().equals
                            (recBldGrp) && user.getEligible() == 1 && !uid.equals(recId)) {
                        Log.d("data uid", uid);
                        Donors donors = dataSnapshot1.getValue(Donors.class);
                        mDonorsArrayList.add(donors);
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void visibility(int eligibilityValue)
    {
        if (eligibilityValue == 0) {
            mEligibility.setVisibility(View.VISIBLE);
        }
        else{
            mEligibility.setVisibility(View.GONE);
        }
    }
}
