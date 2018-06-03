package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class EligibilityActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Button mEligibleButton, mNotEligibleButton;
    private EligibilityAdapter mAdapter;
    private List<String> mEligibilityArrayList = new ArrayList<>();
    private final int ELIGIBILITY_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility);


        mEligibleButton = findViewById(R.id.button_eligible);
        mNotEligibleButton = findViewById(R.id.button_not_eligible);

        mRecyclerView = findViewById(R.id.eligibility_recycler);
        mAdapter = new EligibilityAdapter(mEligibilityArrayList);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();
        mEligibilityArrayList.add("hi");
        mAdapter.notifyDataSetChanged();

        mEligibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("result", 1);
                setResult(ELIGIBILITY_REQUEST_CODE, intent);
                finish();
            }
        });
        mNotEligibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("result", -1);
                setResult(ELIGIBILITY_REQUEST_CODE, intent);
                finish();
            }
        });
    }
}
