package com.dev.sb.blooddonationapp;

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
    private UserAdapter mAdapter;
    private List<Donors> mDonorsArrayList = new ArrayList<>();
    private LinearLayout mContactLayout;
    private FloatingActionButton mfilterButton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mInflate = inflater.inflate(R.layout.fragment_home, container, false);
        return mInflate;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mfilterButton = view.findViewById(R.id.filter_fab);
        mRecyclerView = view.findViewById(R.id.recycler_View);
        mAdapter = new UserAdapter(mDonorsArrayList);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity()
                .getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()
                .getApplicationContext(), LinearLayoutManager.VERTICAL));


        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity()
                .getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity().getApplicationContext(), position + "selected", Toast
                        .LENGTH_SHORT).show();
                showButtomSheetDialogFragment();
            }
        }));
        mRecyclerView.setAdapter(mAdapter);
        //ToDo receive from user
        String receiverBldGrp, receiverLoc, receiverId;
        receiverBldGrp = "B+";
        receiverLoc = "Jaipur";
        mAuth = FirebaseAuth.getInstance();
        receiverId = mAuth.getUid();

        prepareData(receiverBldGrp, receiverLoc, receiverId);

        //TODO: Implement floating action button on click listener
        mfilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void showButtomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }

    private void prepareData(final String recBldGpr, final String recLoc, final String recId) {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("users");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String uid = dataSnapshot1.getKey();
                    Log.d("data uid", uid);

                    UserDetails user = dataSnapshot1.getValue(UserDetails.class);
                    if (user.getCity().equals(recLoc) && user.getBloodGrp().equals(recBldGpr) &&
                            user.isEligible() && !uid.equals(recId)) {

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

}
