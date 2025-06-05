package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.model.ContentWithReview;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    private List<ContentWithReview> reviewList;

    public ReviewAdapter(Context context, List<ContentWithReview> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    public void updateList(List<ContentWithReview> newList) {
        this.reviewList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentWithReview item = reviewList.get(position);

        holder.title.setText(item.title);
        holder.memo.setText(item.memo);
        holder.ratingBar.setRating(item.rating);

        Glide.with(context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView memo;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_cover);
            title = itemView.findViewById(R.id.text_title);
            memo = itemView.findViewById(R.id.text_memo);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}
