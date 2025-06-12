package com.example.project;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.model.Content;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    private List<Content> contentList;

    public ReviewAdapter(Context context, List<Content> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    public void updateList(List<Content> newList) {
        this.contentList = newList;
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
        Content content = contentList.get(position);

        holder.textTitle.setText(content.title);
        Glide.with(context).load(content.imageUrl).into(holder.imageCover);

        if (content.rating > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(content.rating);
        } else {
            holder.ratingBar.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_calendar_detail, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

            TextView btnSaveMemo = dialogView.findViewById(R.id.btn_save_memo);

            btnSaveMemo.setOnClickListener(v2 -> {
                View memoView = LayoutInflater.from(context).inflate(R.layout.dialog_add_memo, null);
                AlertDialog memoDialog = new AlertDialog.Builder(context).setView(memoView).create();

                TextView editMemo = memoView.findViewById(R.id.edit_memo);
                TextView btnSave = memoView.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(v3 -> {
                    String memoText = editMemo.getText().toString().trim();
                    if (!memoText.isEmpty()) {
                        new Thread(() -> {
                            com.example.project.model.Review review = new com.example.project.model.Review();
                            review.contentId = content.id;
                            review.memo = memoText;
                            review.createdDate = new java.text.SimpleDateFormat("yyyy.MM.dd", java.util.Locale.getDefault()).format(new java.util.Date());
                            review.rating = 0f;

                            MainActivity.db.appDao().insertReview(review);

                            ((MainActivity) context).runOnUiThread(() -> {
                                memoDialog.dismiss();
                                dialog.dismiss();
                            });
                        }).start();
                    }
                });

                memoDialog.show();
            });

            ImageView imageView = dialogView.findViewById(R.id.dialog_image);
            TextView titleView = dialogView.findViewById(R.id.dialog_title);
            TextView descView = dialogView.findViewById(R.id.dialog_desc1);
            TextView startDateView = dialogView.findViewById(R.id.dialog_start_date);
            TextView endDateView = dialogView.findViewById(R.id.dialog_end_date);
            RatingBar ratingBar = dialogView.findViewById(R.id.dialog_rating);
            LinearLayout memoContainer = dialogView.findViewById(R.id.memo_container);

            Glide.with(context).load(content.imageUrl).into(imageView);
            titleView.setText(content.title);
            descView.setText(content.place);
            startDateView.setText("시작일: " + (content.startDate != null ? content.startDate : "-"));
            endDateView.setText("종료일: " + (content.endDate != null ? content.endDate : "-"));

            if (content.endDate != null && !content.endDate.isEmpty()) {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(content.rating);
            } else {
                ratingBar.setVisibility(View.GONE);
            }

            new Thread(() -> {
                List<com.example.project.model.Review> memoList = MainActivity.db.appDao().getReviewsByContentId(content.id);

                ((MainActivity) context).runOnUiThread(() -> {
                    memoContainer.removeAllViews();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    for (com.example.project.model.Review review : memoList) {
                        View memoItem = inflater.inflate(R.layout.item_memo, memoContainer, false);
                        TextView dateText = memoItem.findViewById(R.id.text_memo_date);
                        TextView contentText = memoItem.findViewById(R.id.text_memo_content);

                        dateText.setText(review.createdDate);
                        contentText.setText(review.memo);

                        memoContainer.addView(memoItem);
                    }
                });
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return contentList != null ? contentList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCover;
        TextView textTitle;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCover = itemView.findViewById(R.id.image_cover);
            textTitle = itemView.findViewById(R.id.text_title);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}
