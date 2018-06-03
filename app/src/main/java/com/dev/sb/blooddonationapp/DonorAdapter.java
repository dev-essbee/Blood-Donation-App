package com.dev.sb.blooddonationapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder> {
    private List<Donors> donorList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView nameImage;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            nameImage = view.findViewById(R.id.image_name);
        }
    }

    public DonorAdapter(List<Donors> donorList) {
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .donor_list_layout, parent, false);
        return new MyViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Donors donor = donorList.get(position);
        String name = donor.getName();
        holder.name.setText(name);
        holder.nameImage.setImageResource(selectImage(name));


    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    private int selectImage(String name) {
        int imgSrc=0;
        switch (name.substring(0, 1)) {
            case "A":
                imgSrc = R.drawable.a;
                break;
            case "B":
                imgSrc = R.drawable.b;
                break;
            case "C":
                imgSrc = R.drawable.c;
                break;
            case "D":
                imgSrc = R.drawable.d;
                break;
            case "E":
                imgSrc = R.drawable.e;
                break;
            case "F":
                imgSrc = R.drawable.f;
                break;
            case "G":
                imgSrc = R.drawable.g;
                break;
            case "H":
                imgSrc = R.drawable.h;
                break;
            case "I":
                imgSrc = R.drawable.i;
                break;
            case "J":
                imgSrc = R.drawable.j;
                break;
            case "K":
                imgSrc = R.drawable.k;
                break;
            case "L":
                imgSrc = R.drawable.l;
                break;
            case "M":
                imgSrc = R.drawable.m;
                break;
            case "N":
                imgSrc = R.drawable.n;
                break;
            case "O":
                imgSrc = R.drawable.o;
                break;
            case "P":
                imgSrc = R.drawable.p;
                break;
            case "Q":
                imgSrc = R.drawable.q;
                break;
            case "R":
                imgSrc = R.drawable.r;
                break;
            case "S":
                imgSrc = R.drawable.s;
                break;
            case "T":
                imgSrc = R.drawable.t;
                break;
            case "U":
                imgSrc = R.drawable.u;
                break;
            case "V":
                imgSrc = R.drawable.v;
                break;
            case "W":
                imgSrc = R.drawable.w;
                break;
            case "X":
                imgSrc = R.drawable.x;
                break;
            case "Y":
                imgSrc = R.drawable.y;
                break;
            case "Z":
                imgSrc = R.drawable.z;
                break;

        }
        return imgSrc;
    }
}
