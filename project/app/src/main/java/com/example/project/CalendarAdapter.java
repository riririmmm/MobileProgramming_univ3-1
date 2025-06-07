package com.example.project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.model.Content;
import com.example.project.model.Review;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

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

        holder.itemView.setOnClickListener(v -> {
            Content content = itemList.get(holder.getAdapterPosition());

            View dialogView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.dialog_calendar_detail, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();

            // 1. 다이얼로그 뷰 연결
            ImageView imageView = dialogView.findViewById(R.id.dialog_image);
            TextView titleView = dialogView.findViewById(R.id.dialog_title);
            TextView descView = dialogView.findViewById(R.id.dialog_desc1);
            TextView startDateView = dialogView.findViewById(R.id.dialog_start_date);
            TextView endDateView = dialogView.findViewById(R.id.dialog_end_date);
            RatingBar ratingBar = dialogView.findViewById(R.id.dialog_rating);
            Button btnSaveMemo = dialogView.findViewById(R.id.btn_save_memo);

            // 2. 콘텐츠 정보 세팅
            titleView.setText(content.title);
            descView.setText(content.place); // desc1 역할
            startDateView.setText("시작일: " + (content.startDate != null ? content.startDate : "-"));
            endDateView.setText("종료일: " + (content.endDate != null ? content.endDate : "-"));

            Glide.with(holder.itemView.getContext())
                    .load(content.imageUrl)
                    .into(imageView);

            // 3. 종료일 있으면 RatingBar 표시
            if (content.endDate != null && !content.endDate.isEmpty()) {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(content.rating);
            } else {
                ratingBar.setVisibility(View.GONE);
            }

            // 4. 시작일/종료일 TextView 클릭 시 DatePicker
            startDateView.setOnClickListener(v2 -> showDatePickerDialog(holder, startDateView, true, content));
            endDateView.setOnClickListener(v2 -> {
                showEndDateRatingDialog(holder.itemView.getContext(), content, () -> {
                    endDateView.setText("종료일: " + content.endDate);
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setRating(content.rating);
                });
            });

            // 5. 메모 추가 버튼 클릭 시 메모 다이얼로그 띄우기
            btnSaveMemo.setOnClickListener(v2 -> {
                showMemoInputDialog(holder.itemView.getContext(), content.id, () -> {
                    Toast.makeText(holder.itemView.getContext(), "메모가 저장되었습니다", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            });

            // 6. 작성된 메모들 불러와서 추가 표시 (아래에서 추가 구현)
            loadAndAttachMemoList(holder.itemView.getContext(), dialogView, content.id);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void showDatePickerDialog(ViewHolder holder, TextView targetView, boolean isStart, Content content) {
        Context context = holder.itemView.getContext();
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            String newDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            targetView.setText((isStart ? "시작일: " : "종료일: ") + newDate);

            new Thread(() -> {
                if (isStart) content.startDate = newDate;
                else content.endDate = newDate;
                MainActivity.db.appDao().updateContent(content);
            }).start();

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showMemoInputDialog(Context context, int contentId, Runnable onSaved) {
        View memoView = LayoutInflater.from(context).inflate(R.layout.dialog_add_memo, null);
        EditText editMemo = memoView.findViewById(R.id.edit_memo);
        Button btnSave = memoView.findViewById(R.id.btn_save);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(memoView)
                .create();

        btnSave.setOnClickListener(v -> {
            String memoText = editMemo.getText().toString().trim();
            if (!memoText.isEmpty()) {
                new Thread(() -> {
                    SimpleDateFormat sdf;
                    sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                    String todayStr = sdf.format(new Date());
                    Review review = new Review();
                    review.contentId = contentId;
                    review.memo = memoText;
                    review.rating = 0f;
                    review.createdDate = todayStr;
                    MainActivity.db.appDao().insertReview(review);

                }).start();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadAndAttachMemoList(Context context, View dialogView, int contentId) {
        ViewGroup memoContainer = new LinearLayout(context);
        ((LinearLayout) dialogView.findViewById(R.id.dialog_rating).getParent()).addView(memoContainer);  // 버튼 밑에 추가

        new Thread(() -> {
            List<Review> memoList = MainActivity.db.appDao().getReviewsByContentId(contentId);

            new android.os.Handler(Looper.getMainLooper()).post(() -> {
                memoContainer.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(context);
                for (Review review : memoList) {
                    View memoItem = inflater.inflate(R.layout.item_memo, memoContainer, false);
                    TextView dateText = memoItem.findViewById(R.id.text_memo_date);
                    TextView contentText = memoItem.findViewById(R.id.text_memo_content);

                    dateText.setText(review.createdDate);
                    contentText.setText(review.memo);

                    // 클릭 시 수정 다이얼로그
                    memoItem.setOnClickListener(v -> {
                        View editView = inflater.inflate(R.layout.dialog_add_memo, null);
                        EditText editMemo = editView.findViewById(R.id.edit_memo);
                        Button btnSave = editView.findViewById(R.id.btn_save);
                        editMemo.setText(review.memo);

                        AlertDialog editDialog = new AlertDialog.Builder(context).setView(editView).create();

                        btnSave.setOnClickListener(btn -> {
                            String updated = editMemo.getText().toString().trim();
                            if (!updated.isEmpty()) {
                                new Thread(() -> {
                                    review.memo = updated;
                                    MainActivity.db.appDao().updateReview(review);
                                    new android.os.Handler(Looper.getMainLooper()).post(() -> {
                                        Toast.makeText(context, "메모가 수정되었습니다", Toast.LENGTH_SHORT).show();
                                        editDialog.dismiss();
                                        loadAndAttachMemoList(context, dialogView, contentId); // 새로고침
                                    });
                                }).start();
                            }
                        });

                        editDialog.show();
                    });

                    memoContainer.addView(memoItem);
                }
            });
        }).start();
    }

    private void showEndDateRatingDialog(Context context, Content content, Runnable onSaved) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_enddate_rating, null);
        DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();

        btnSave.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            int day = datePicker.getDayOfMonth();
            String dateStr = String.format("%04d-%02d-%02d", year, month, day);
            float rating = ratingBar.getRating();

            content.endDate = dateStr;
            content.rating = rating;

            new Thread(() -> {
                MainActivity.db.appDao().updateContent(content);
            }).start();

            Toast.makeText(context, "종료일 및 별점이 저장되었습니다", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            if (onSaved != null) onSaved.run();
        });

        dialog.show();
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
