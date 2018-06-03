package com.dev.sb.blooddonationapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EligibilityAdapter extends RecyclerView.Adapter<EligibilityAdapter.MyViewHolder> {
    private List<String> eligibilityList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eligibilityCon;

        public MyViewHolder(View view) {
            super(view);
            eligibilityCon = view.findViewById(R.id.condition_text);
        }
    }

    public EligibilityAdapter(List<String> eligibilityList) {
        this.eligibilityList = eligibilityList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eligibilityView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .eligibilty_condition_layout, parent, false);
        return new MyViewHolder(eligibilityView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String condition = eligibilityList.get(position);
        holder.eligibilityCon.setText(condition);

    }

    @Override
    public int getItemCount() {
        return eligibilityList.size();
    }
}
