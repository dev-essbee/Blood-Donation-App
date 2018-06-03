package com.dev.sb.blooddonationapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {
    private List<Details> mDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mDetail;
        public ImageView mDetailImage;

        public MyViewHolder(View view) {
            super(view);
            mDetail = view.findViewById(R.id.detail);
            mDetailImage = view.findViewById(R.id.detail_image);

        }
    }
    public DetailsAdapter (List<Details> detailsList){
        this.mDetailsList=detailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View detailView= LayoutInflater.from(parent.getContext()).inflate(R.layout
                .bottom_action_list_layout,parent,false);
        return new MyViewHolder(detailView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    Details details=mDetailsList.get(position);
    String text=details.getText();
    int img=details.getImageSrc();
    holder.mDetail.setText(text);
    holder.mDetailImage.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return mDetailsList.size();
    }
}
