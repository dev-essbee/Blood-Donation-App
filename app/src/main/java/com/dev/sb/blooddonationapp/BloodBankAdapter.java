package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.MyViewHolder> {
    private List<BloodBank> mBankList;

    public BloodBankAdapter(List<BloodBank> bloodBankList) {
        this.mBankList = bloodBankList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bloodbBanksView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .blood_banks_item_layout, parent, false);
        return new MyViewHolder(bloodbBanksView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BloodBank bloodBank = mBankList.get(position);

        final String name=bloodBank.getName();
        final String lat = bloodBank.getLat();
        final String lon = bloodBank.getLon();
        final String phNo = bloodBank.getPhoneNo();
        holder.mName.setText(name);
        holder.mLocation.setText(bloodBank.getLocation());
        if (phNo.equals("NA") || phNo.equals("")) {
            holder.mCall.setVisibility(View.GONE);
        }
        if (lat.equals("0")) {
            holder.mNavigate.setVisibility(View.GONE);
        }
        holder.mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent();
                callIntent.setAction(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phNo));
                if (callIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(callIntent);
                }
            }
        });//todo remove bloodbank from name for maps search
        holder.mNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navIntent = new Intent();
                Uri intentUri = Uri.parse("geo:" + lat + "," + lon+"?q="+name);
                navIntent.setAction(Intent.ACTION_VIEW);
                navIntent.setData(intentUri);
                navIntent.setPackage("com.google.android.apps.maps");
                if (navIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(navIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBankList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mLocation;
        public ImageButton mCall, mNavigate;

        public MyViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.bld_bnk_name);
            mLocation = view.findViewById(R.id.bld_bnk_loc);
            mCall = view.findViewById(R.id.bnk_call_button);
            mNavigate = view.findViewById(R.id.bnk_direc_button);
        }

    }
}
