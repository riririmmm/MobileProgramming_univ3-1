package com.example.project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.model.Content;
import com.example.project.model.ReadDate;
import com.example.project.model.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        holder.itemView.setOnClickListener(v -> showDetailDialog(holder, content));
    }

    private void showDetailDialog(ViewHolder holder, Content content) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_calendar_detail, null);
        AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
        dialog.show();

        ImageView imageView = dialogView.findViewById(R.id.dialog_image);
        TextView titleView = dialogView.findViewById(R.id.dialog_title);
        TextView descView = dialogView.findViewById(R.id.dialog_desc1);
        TextView startDateView = dialogView.findViewById(R.id.dialog_start_date);
        TextView endDateView = dialogView.findViewById(R.id.dialog_end_date);
        RatingBar ratingBar = dialogView.findViewById(R.id.dialog_rating);
        LinearLayout memoContainer = dialogView.findViewById(R.id.memo_container);
        Button btnSaveMemo = dialogView.findViewById(R.id.btn_save_memo);
        Button btnEdit = dialogView.findViewById(R.id.btn_edit);
        Button btnDelete = dialogView.findViewById(R.id.btn_delete);

        Glide.with(context).load(content.imageUrl).into(imageView);
        titleView.setText(content.title);
        descView.setText(content.place);
        startDateView.setText("시작일: " + (content.startDate != null ? content.startDate : "-"));
        endDateView.setText("종료일: " + (content.endDate != null ? content.endDate : "-"));

        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setRating(content.rating);

        loadAndAttachMemoList(dialogView, content.id);

        btnSaveMemo.setOnClickListener(v -> showMemoInputDialog(content.id, () -> {
            Toast.makeText(context, "메모가 저장되었습니다", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }));

        btnEdit.setOnClickListener(v -> {
            showEditContentDialog(context, content, () -> {
                notifyItemChanged(holder.getAdapterPosition());
                dialog.dismiss();
            });
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("콘텐츠 삭제")
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제", (d, i) -> {
                        new Thread(() -> {
                            MainActivity.db.appDao().deleteContent(content);
                            MainActivity.db.appDao().deleteReadDatesByContentId(content.id);
                            MainActivity.db.appDao().deleteReviewsByContentId(content.id);
                            contentList.remove(holder.getAdapterPosition());
                            new android.os.Handler(Looper.getMainLooper()).post(() -> {
                                notifyItemRemoved(holder.getAdapterPosition());
                                dialog.dismiss();
                            });
                        }).start();
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });
    }

    private void showMemoInputDialog(int contentId, Runnable onSaved) {
        View memoView = LayoutInflater.from(context).inflate(R.layout.dialog_add_memo, null);
        EditText editMemo = memoView.findViewById(R.id.edit_memo);
        Button btnSave = memoView.findViewById(R.id.btn_save);
        AlertDialog dialog = new AlertDialog.Builder(context).setView(memoView).create();

        btnSave.setOnClickListener(v -> {
            String memoText = editMemo.getText().toString().trim();
            if (!memoText.isEmpty()) {
                new Thread(() -> {
                    Review review = new Review();
                    review.contentId = contentId;
                    review.memo = memoText;
                    review.rating = 0f;
                    review.createdDate = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(new Date());
                    MainActivity.db.appDao().insertReview(review);

                    new android.os.Handler(Looper.getMainLooper()).post(() -> {
                        dialog.dismiss();
                        if (onSaved != null) onSaved.run();
                    });
                }).start();
            } else {
                Toast.makeText(context, "메모를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void loadAndAttachMemoList(View dialogView, int contentId) {
        ViewGroup memoContainer = dialogView.findViewById(R.id.memo_container);
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

                    memoItem.setOnClickListener(v -> {
                        View editView = inflater.inflate(R.layout.dialog_add_memo, null);
                        EditText editMemo = editView.findViewById(R.id.edit_memo);
                        Button btnSave = editView.findViewById(R.id.btn_save);
                        editMemo.setText(review.memo);

                        btnSave.setVisibility(View.GONE);
                        LinearLayout buttonRow = new LinearLayout(context);
                        buttonRow.setOrientation(LinearLayout.HORIZONTAL);
                        buttonRow.setGravity(Gravity.END);
                        Button btnDelete = new Button(context);
                        btnDelete.setText("삭제");
                        Button btnSaveNew = new Button(context);
                        btnSaveNew.setText("수정");
                        buttonRow.addView(btnDelete);
                        buttonRow.addView(btnSaveNew);
                        ((LinearLayout) editView).addView(buttonRow);

                        AlertDialog editDialog = new AlertDialog.Builder(context).setView(editView).create();

                        btnDelete.setOnClickListener(v2 -> {
                            new AlertDialog.Builder(context)
                                    .setTitle("메모 삭제")
                                    .setMessage("정말 삭제하시겠습니까?")
                                    .setPositiveButton("삭제", (d, i) -> {
                                        new Thread(() -> {
                                            MainActivity.db.appDao().deleteReview(review);
                                            new android.os.Handler(Looper.getMainLooper()).post(() -> {
                                                Toast.makeText(context, "메모가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                                editDialog.dismiss();
                                                loadAndAttachMemoList(dialogView, contentId);
                                            });
                                        }).start();
                                    })
                                    .setNegativeButton("취소", null)
                                    .show();
                        });

                        btnSaveNew.setOnClickListener(v2 -> {
                            String updated = editMemo.getText().toString().trim();
                            if (!updated.isEmpty()) {
                                new Thread(() -> {
                                    review.memo = updated;
                                    MainActivity.db.appDao().updateReview(review);
                                    new android.os.Handler(Looper.getMainLooper()).post(() -> {
                                        Toast.makeText(context, "메모가 수정되었습니다", Toast.LENGTH_SHORT).show();
                                        editDialog.dismiss();
                                        loadAndAttachMemoList(dialogView, contentId);
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

    private void showEditContentDialog(Context context, Content content, Runnable onUpdated) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_content, null);
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context).setView(dialogView).create();

        TextView titleView = dialogView.findViewById(R.id.text_title);
        TextView startDateView = dialogView.findViewById(R.id.text_start_date);
        TextView endDateView = dialogView.findViewById(R.id.text_end_date);
        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        LinearLayout readDatesContainer = dialogView.findViewById(R.id.read_dates_container);
        Button btnAddReadDate = dialogView.findViewById(R.id.btn_add_read_date);
        Button btnSave = dialogView.findViewById(R.id.btn_save);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        titleView.setText(content.title);
        startDateView.setText(content.startDate != null ? content.startDate : "시작일 선택");
        endDateView.setText(content.endDate != null ? content.endDate : "종료일 선택");
        ratingBar.setRating(content.rating);

        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setRating(content.rating);

        // 날짜 선택
        startDateView.setOnClickListener(v -> showDatePicker(context, startDateView));
        endDateView.setOnClickListener(v -> showDatePicker(context, endDateView));

        // 읽은 날짜 불러오기
        new Thread(() -> {
            List<ReadDate> readDates = MainActivity.db.appDao().getReadDatesByContentId(content.id);
            List<String> existingDates = new ArrayList<>();
            for (ReadDate rd : readDates) {
                existingDates.add(rd.readDate);
            }

            // 시작일/종료일이 이미 들어가 있지 않으면 추가
            if (content.startDate != null && !content.startDate.isEmpty()
                    && !existingDates.contains(content.startDate)) {
                ReadDate start = new ReadDate();
                start.contentId = content.id;
                start.readDate = content.startDate;
                readDates.add(0, start); // 앞에 추가
            }

            if (content.endDate != null && !content.endDate.isEmpty()
                    && !content.endDate.equals(content.startDate)
                    && !existingDates.contains(content.endDate)) {
                ReadDate end = new ReadDate();
                end.contentId = content.id;
                end.readDate = content.endDate;
                readDates.add(end);  // 뒤에 추가
            }

            new android.os.Handler(Looper.getMainLooper()).post(() -> {
                readDatesContainer.removeAllViews();
                for (ReadDate rd : readDates) {
                    TextView tv = new TextView(context);
                    tv.setText(rd.readDate);
                    tv.setPadding(0, 8, 0, 8);
                    readDatesContainer.addView(tv);
                }
            });
        }).start();

        // 읽은 날짜 추가
        btnAddReadDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(context, (view, year, month, day) -> {
                String dateStr = String.format("%04d-%02d-%02d", year, month + 1, day);
                boolean exists = false;
                for (int i = 0; i < readDatesContainer.getChildCount(); i++) {
                    String existing = ((TextView) readDatesContainer.getChildAt(i)).getText().toString();
                    if (existing.equals(dateStr)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    TextView tv = new TextView(context);
                    tv.setText(dateStr);
                    tv.setPadding(0, 8, 0, 8);
                    readDatesContainer.addView(tv);
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(v -> {
            String startDateStr = startDateView.getText().toString();
            String endDateStr = endDateView.getText().toString();

            // 시작일/종료일 선택 안내 문구는 null 처리
            content.startDate = startDateStr.equals("시작일 선택") ? null : startDateStr;
            content.endDate = endDateStr.equals("종료일 선택") ? null : endDateStr;
            content.rating = ratingBar.getRating();

            new Thread(() -> {
                MainActivity.db.appDao().updateContent(content);

                // 기존 읽은 날짜 삭제 후 새로 삽입
                MainActivity.db.appDao().deleteReadDatesByContentId(content.id);
                for (int i = 0; i < readDatesContainer.getChildCount(); i++) {
                    String date = ((TextView) readDatesContainer.getChildAt(i)).getText().toString();

                    // 날짜 포맷이 yyyy-MM-dd인 경우만 저장
                    if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) continue;

                    ReadDate rd = new ReadDate();
                    rd.contentId = content.id;
                    rd.readDate = date;
                    MainActivity.db.appDao().insertReadDate(rd);
                }

                new android.os.Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "수정되었습니다", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    if (onUpdated != null) onUpdated.run();
                });
            }).start();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDatePicker(Context context, TextView targetView) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(context, (view, year, month, day) -> {
            String selected = String.format("%04d-%02d-%02d", year, month + 1, day);
            targetView.setText(selected);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
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
