package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EligibilityActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Button mEligibleButton, mNotEligibleButton;
    private EligibilityAdapter mAdapter;
    private List<String> mEligibilityArrayList = new ArrayList<>();
    private String callingIntent = "";
    //private final int ELIGIBILITY_REQUEST_CODE = 101;
    private ConstraintLayout mButtonLayout;
    private TextView mIndText;
    private ActionBar mActionBar;


    @Override
    public boolean onSupportNavigateUp() {
        if(mActionBar!=null) {
            finish();
            return true;
        }
        else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility);
//todo add back button to all the activities
        mActionBar = getSupportActionBar();

        mIndText = findViewById(R.id.hint_Text);
        mButtonLayout = findViewById(R.id.button_layout);
        callingIntent = getIntent().getStringExtra("fragment");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mEligibleButton = findViewById(R.id.button_eligible);
        mNotEligibleButton = findViewById(R.id.button_not_eligible);
        mEligibleButton.setEnabled(false);
        mNotEligibleButton.setEnabled(false);
        mRecyclerView = findViewById(R.id.eligibility_recycler);
        mAdapter = new EligibilityAdapter(mEligibilityArrayList);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager
                .VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        if(callingIntent.equals("menu")){
         mButtonLayout.setVisibility(View.GONE);
        }
        Log.d("call",callingIntent.toString());
        String[] temp = getResources().getStringArray(R.array.eligibility_criteria);
        for (int i = 0; i < 14; i++) {
            mEligibilityArrayList.add(temp[i]);
        }
        mAdapter.notifyDataSetChanged();
        mEligibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingIntent.equals("HomeFragment")) {
                    Log.d("result", getCallingActivity().getClassName());
                    Intent intent = new Intent(EligibilityActivity.this, HomeFragment.class);
                    intent.putExtra("result", "1");
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (callingIntent.equals("ProfileFragment")) {
                    Intent intent = new Intent(EligibilityActivity.this, ProfileFragment.class);
                    intent.putExtra("result", "1");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        mNotEligibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingIntent.equals("HomeFragment")) {
                    Intent intent = new Intent(EligibilityActivity.this, HomeFragment.class);
                    intent.putExtra("result", "-1");
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (callingIntent.equals("ProfileFragment")) {
                    Intent intent = new Intent(EligibilityActivity.this, ProfileFragment.class);
                    intent.putExtra("result", "-1");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//Log.d("scroll","drag");
                mButtonLayout.setElevation(4 * getApplicationContext().getResources()
                        .getDisplayMetrics().density);
                mIndText.setElevation(4 * getApplicationContext().getResources()
                        .getDisplayMetrics().density);
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {//Log.d("scroll","idle");

            }
            if (newState == RecyclerView.SCROLL_STATE_SETTLING) {//Log.d("scroll","settle");

            }
            mEligibleButton.setEnabled(false);
            mNotEligibleButton.setEnabled(false);
            mNotEligibleButton.setBackground(getResources().getDrawable(R.drawable.ter_button_inactive));
            mNotEligibleButton.setTextColor(getResources().getColor(R.color.icons));
            mEligibleButton.setBackground(getResources().getDrawable(R.drawable.pri_button_inactive));
            mEligibleButton.setTextColor(getResources().getColor(R.color.icons));
            if (!recyclerView.canScrollVertically(1)) {
                Log.d("scroll", "end");
                mEligibleButton.setEnabled(true);
                mNotEligibleButton.setEnabled(true);
                mNotEligibleButton.setBackground(getResources().getDrawable(R.drawable.ter_button_active));
                mNotEligibleButton.setTextColor(getResources().getColor(R.color.icons));
                mEligibleButton.setBackground(getResources().getDrawable(R.drawable.pri_button));
                mEligibleButton.setTextColor(getResources().getColor(R.color.icons));
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //Log.d("scroll",dx+" "+dy);
        }
    };


}
