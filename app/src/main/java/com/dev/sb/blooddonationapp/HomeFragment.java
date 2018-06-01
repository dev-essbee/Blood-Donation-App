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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private List<OtherUsers> mOtherUsersList = new ArrayList<>();
    private LinearLayout mContactLayout;
    private FloatingActionButton mfilterButton;

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
        mAdapter = new UserAdapter(mOtherUsersList);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity()
                .getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()
                .getApplicationContext(), LinearLayoutManager.VERTICAL));


        //TODO:Above comment does not work,implement:  When one list name is opened other should
        // get closed.
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity()
                .getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity().getApplicationContext(), position + "selected", Toast
                        .LENGTH_SHORT).show();

            }
        }));
        mRecyclerView.setAdapter(mAdapter);
        prepareData();

        mfilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void prepareData() {
        OtherUsers users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);
        users = new OtherUsers();
        users.setName("Shubham");
        mOtherUsersList.add(users);

        mAdapter.notifyDataSetChanged();
    }

}
