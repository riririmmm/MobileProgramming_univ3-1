package com.example.project;

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
import androidx.appcompat.app.AlertDialog;
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

            // 다이얼로그 뷰 바인딩
            ImageView imageView = dialogView.findViewById(R.id.dialog_image);
            TextView titleView = dialogView.findViewById(R.id.dialog_title);
            TextView descView = dialogView.findViewById(R.id.dialog_desc1);
            TextView startDateView = dialogView.findViewById(R.id.dialog_start_date);
            TextView endDateView = dialogView.findViewById(R.id.dialog_end_date);
            RatingBar ratingBar = dialogView.findViewById(R.id.dialog_rating);
            Button btnSaveMemo = dialogView.findViewById(R.id.btn_save_memo);
            Button btnEdit = dialogView.findViewById(R.id.btn_edit);
            Button btnDelete = dialogView.findViewById(R.id.btn_delete);

            // 콘텐츠 정보 표시
            titleView.setText(content.title);
            descView.setText(content.place);
            startDateView.setText("시작일: " + (content.startDate != null ? content.startDate : "-"));
            endDateView.setText("종료일: " + (content.endDate != null ? content.endDate : "-"));

            Glide.with(holder.itemView.getContext())
                    .load(content.imageUrl)
                    .into(imageView);

            ratingBar.setRating(content.rating);

            // 수정 버튼
            btnEdit.setOnClickListener(v2 -> {
                showEditContentDialog(holder.itemView.getContext(), content, () -> {
                    notifyItemChanged(holder.getAdapterPosition());
                    dialog.dismiss();
                });
            });

            // 삭제 버튼
            btnDelete.setOnClickListener(v2 -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("콘텐츠 삭제")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("삭제", (d, i) -> {
                            new Thread(() -> {
                                MainActivity.db.appDao().deleteContent(content);
                                MainActivity.db.appDao().deleteReadDatesByContentId(content.id);
                                MainActivity.db.appDao().deleteReviewsByContentId(content.id);
                                itemList.remove(holder.getAdapterPosition());

                                new android.os.Handler(Looper.getMainLooper()).post(() -> {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    Toast.makeText(holder.itemView.getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                });
                            }).start();
                        })
                        .setNegativeButton("취소", null)
                        .show();
            });

            // 시작일/종료일은 읽기 전용 → DatePicker 없음

            // 메모 추가 버튼
            btnSaveMemo.setOnClickListener(v2 -> {
                showMemoInputDialog(holder.itemView.getContext(), content.id, () -> {
                    Toast.makeText(holder.itemView.getContext(), "메모가 저장되었습니다", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            });

            // 메모 목록 불러오기
            loadAndAttachMemoList(holder.itemView.getContext(), dialogView, content.id);
        });
    }

    // 다이얼로그에서 시작일/종료일/별점/읽은 날짜 수정할 수 있는 함수
    private void showEditContentDialog(Context context, Content content, Runnable onUpdated) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_content, null);
        AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();

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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                    String todayStr = sdf.format(new Date());
                    Review review = new Review();
                    review.contentId = contentId;
                    review.memo = memoText;
                    review.rating = 0f;
                    review.createdDate = todayStr;

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

    private void loadAndAttachMemoList(Context context, View dialogView, int contentId) {
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

                    // 클릭 시 수정 다이얼로그
                    memoItem.setOnClickListener(v -> {
                        View editView = inflater.inflate(R.layout.dialog_add_memo, null);
                        EditText editMemo = editView.findViewById(R.id.edit_memo);
                        Button btnSave = editView.findViewById(R.id.btn_save);
                        editMemo.setText(review.memo);

                        btnSave.setVisibility(View.GONE);

                        LinearLayout buttonRow = new LinearLayout(context);
                        buttonRow.setOrientation(LinearLayout.HORIZONTAL);
                        buttonRow.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        buttonRow.setGravity(Gravity.END);

                        Button btnDelete = new Button(context);
                        btnDelete.setText("삭제");
                        btnDelete.setTextSize(12f);

                        Button btnSaveNew = new Button(context);
                        btnSaveNew.setText("수정");
                        btnSaveNew.setTextSize(12f);

                        buttonRow.addView(btnDelete);
                        buttonRow.addView(btnSaveNew);

                        ((LinearLayout) editView).addView(buttonRow);

                        AlertDialog editDialog = new AlertDialog.Builder(context).setView(editView).create();

                        btnDelete.setOnClickListener(v2 -> {
                            new AlertDialog.Builder(context)
                                    .setTitle("메모 삭제")
                                    .setMessage("정말 삭제하시겠습니까?")
                                    .setPositiveButton("삭제", (dialogInterface, i) -> {
                                        new Thread(() -> {
                                            MainActivity.db.appDao().deleteReview(review);
                                            new android.os.Handler(Looper.getMainLooper()).post(() -> {
                                                Toast.makeText(context, "메모가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                                editDialog.dismiss();
                                                loadAndAttachMemoList(context, dialogView, contentId); // 새로고침
                                            });
                                        }).start();
                                    })
                                    .setNegativeButton("취소", null)
                                    .show();
                        });

                        btnSaveNew.setOnClickListener(btn -> {
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
