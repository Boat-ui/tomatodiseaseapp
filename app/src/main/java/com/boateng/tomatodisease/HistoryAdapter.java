package com.boateng.tomatodisease;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<Scan> scanList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Scan scan);
        void onDeleteClick(Scan scan);
    }

    public HistoryAdapter(ArrayList<Scan> scanList, OnItemClickListener listener) {
        this.scanList = scanList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scan scan = scanList.get(position);

        holder.tvDisease.setText(scan.getDisease());
        holder.tvConfidence.setText(scan.getConfidenceString());
        holder.tvTimestamp.setText(scan.getFormattedTimestamp());

        // Set disease image if available
        Bitmap image = scan.getImage();
        if (image != null) {
            holder.ivImage.setImageBitmap(image);
        } else {
            // holder.ivImage.setImageResource(R.drawable.ic_tomato);
        }

        // Set color based on disease
        int colorRes;
        if (scan.getDisease().equals("Healthy")) {
            colorRes = R.color.healthy;
        } else {
            colorRes = R.color.diseased;
        }
        holder.tvDisease.setBackgroundColor(holder.itemView.getResources().getColor(colorRes));

        // Set click listeners
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(scan);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(scan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDisease;
        TextView tvConfidence;
        TextView tvTimestamp;
        ImageView ivImage;
        View btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDisease = itemView.findViewById(R.id.tv_disease);
            tvConfidence = itemView.findViewById(R.id.tv_confidence);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            ivImage = itemView.findViewById(R.id.iv_image);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}