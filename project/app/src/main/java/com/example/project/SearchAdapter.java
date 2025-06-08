package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.project.db.AppDatabase;
import com.example.project.model.Content;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final Context context;
    private final String category;
    private final List<SearchItem> searchList;

    public SearchAdapter(Context context, List<SearchItem> searchList, String category) {
        this.context = context;
        this.searchList = searchList;
        this.category = category;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchItem item = searchList.get(position);

        android.util.Log.d("DEBUG", "[onBindViewHolder] title: " + item.title + ", imageUrl: " + item.imageUrl);

        holder.title.setText(item.title);
        holder.desc1.setText(item.desc1);
        holder.desc2.setText(item.desc3);

        Glide.with(context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_search_detail, null);
            builder.setView(dialogView);

            ImageView image = dialogView.findViewById(R.id.image_popup);
            TextView title = dialogView.findViewById(R.id.text_popup_title);
            TextView detail1 = dialogView.findViewById(R.id.text_popup_detail1);
            TextView detail2 = dialogView.findViewById(R.id.text_popup_detail2);
            Button addBtn = dialogView.findViewById(R.id.btn_add_calendar);

            title.setText(item.title);
            detail1.setText(item.desc1);
            detail2.setText(item.desc2);

            Glide.with(context).load(item.imageUrl).into(image);

            AlertDialog dialog = builder.create();
            dialog.show();

            addBtn.setOnClickListener(view -> {
                dialog.dismiss();  // 1차 다이얼로그 닫기

                View calendarView = LayoutInflater.from(context).inflate(R.layout.calendar_add_dialog, null);
                ImageView imgView = calendarView.findViewById(R.id.dialog_image);
                TextView titleView = calendarView.findViewById(R.id.dialog_title);
                TextView detailView = calendarView.findViewById(R.id.dialog_detail);
                DatePicker datePicker = calendarView.findViewById(R.id.dialog_datepicker);

                // 값 넣기
                Glide.with(context).load(item.imageUrl).into(imgView);
                titleView.setText(item.title);
                detailView.setText(item.desc1);
                detailView.setText(item.desc2);

                new AlertDialog.Builder(context)
                        .setTitle("캘린더에 추가")
                        .setView(calendarView)
                        .setPositiveButton("저장", (d, which) -> {
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth() + 1;
                            int day = datePicker.getDayOfMonth();
                            String dateStr = String.format("%04d-%02d-%02d", year, month, day);

                            Content content = new Content();
                            content.title = item.title;
                            content.imageUrl = item.imageUrl;
                            content.category = category;
                            content.place = item.desc1;  // 공연 장소일 수도 있음
                            content.watchedDate = dateStr;

                            new Thread(() -> {
                                MainActivity activity = (MainActivity) context;
                                AppDatabase db = MainActivity.db;

                                // 콘텐츠 저장
                                db.appDao().insertContent(content);

                                // 방금 저장한 content.id 다시 불러오기 (title, date 기준으로 조회)
                                Content inserted = db.appDao().findContentByTitleAndDate(content.title, content.watchedDate);
                                if (inserted != null) {
                                    com.example.project.model.ReadDate rd = new com.example.project.model.ReadDate();
                                    rd.contentId = inserted.id;
                                    rd.readDate = inserted.watchedDate;
                                    db.appDao().insertReadDate(rd);
                                }

                                new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                                        Toast.makeText(context, "캘린더에 저장했습니다.", Toast.LENGTH_SHORT).show()
                                );
                            }).start();

                        })
                        .setNegativeButton("취소", null)
                        .show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView desc1, desc2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_result);
            title = itemView.findViewById(R.id.text_title);
            desc1 = itemView.findViewById(R.id.text_detail1);
            desc2 = itemView.findViewById(R.id.text_detail2);
        }
    }

}
