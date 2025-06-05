package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.model.Content;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private List<Content> itemList;

    public CalendarAdapter(List<Content> itemList) {
        this.itemList = itemList;
    }

    public void updateList(List<Content> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Content item = itemList.get(position);

        holder.titleText.setText(item.title);
        holder.categoryText.setText(item.category);

        Glide.with(holder.itemView.getContext())
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageIcon);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView categoryText;
        ImageView imageIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.text_title);
            categoryText = itemView.findViewById(R.id.text_category);
            imageIcon = itemView.findViewById(R.id.image_icon);
        }
    }
}
