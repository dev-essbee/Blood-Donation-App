package com.dev.sb.blooddonationapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private List<OtherUsers> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView nameImage;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            nameImage = view.findViewById(R.id.image_name);
        }
    }

    public UserAdapter(List<OtherUsers> userList) {
        this.userList = userList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View userView= LayoutInflater.from(parent.getContext()).inflate(R.layout
                .donor_list_layout,parent,false);
        return new MyViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OtherUsers users=userList.get(position);
        String name=users.getName();
        holder.name.setText(name);
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }


}
