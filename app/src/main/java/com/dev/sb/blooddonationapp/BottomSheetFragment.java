package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private RecyclerView mRecyclerView;
    private DetailsAdapter mDetailsAdapter;
    private List<Details> mDetailsArrayList = new ArrayList<>();

    public BottomSheetFragment() {

    }

    private void prepareDetails() {
        Details details = new Details("Call", R.drawable.ic_action_call);
        mDetailsArrayList.add(details);
        details = new Details("Whatsapp Message", R.drawable.ic_action_whtsp);
        mDetailsArrayList.add(details);
        details = new Details("Share", R.drawable.ic_action_share);
        mDetailsArrayList.add(details);
        mDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bottom_sheet_actions, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = getView().findViewById(R.id.bottom_sheet_recycler);
        mDetailsAdapter = new DetailsAdapter(mDetailsArrayList);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity()
                .getApplicationContext());
        mRecyclerView.setLayoutManager(manager);


        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()
                .getApplicationContext(), LinearLayoutManager.VERTICAL));

        Bundle mArgs = getArguments();
        final String phNo = mArgs.getString("phNo");
        final String name=mArgs.getString("name");
        mRecyclerView.setAdapter(mDetailsAdapter);
        prepareDetails();
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity()
                .getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity().getApplicationContext(), position + "pressed",
                        Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        call(phNo);
                        break;
                    case 1:
                        msg(phNo,name);
                        break;
                    case 2:
                        share(phNo,name);
                        break;
                }
            }
        }));
    }

    private void call(String phNo) {
        Intent callIntent = new Intent();
        callIntent.setAction(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phNo));
        if (callIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(callIntent);
        }
    }

    private void msg(String phNo,String name) {
        String url = "", msgText = "";
        try {
            msgText = "Hi, "+name;
            url = "https://api.whatsapp.com/send?phone=" + "+91" + phNo + "&text=" +
                    URLEncoder.encode(msgText, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
        msgIntent.setPackage("com.whatsapp");
        msgIntent.setData(Uri.parse(url));
        if (msgIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(msgIntent);
        }
    }

    private void share(String phNo,String name) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Donor name: "+ name+", Mobile Number: "+phNo);
        shareIntent.setType("text/plain");
        String title = getResources().getString(R.string.share_chooser_title);
        Intent chooser = Intent.createChooser(shareIntent, title);
        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
