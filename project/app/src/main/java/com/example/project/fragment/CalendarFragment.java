package com.example.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.CalendarAdapter;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.model.Content;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private RecyclerView recyclerView;
    private CalendarAdapter adapter;
    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recycler_calendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CalendarAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 기본: 오늘 날짜에 해당하는 콘텐츠 표시
        Calendar today = Calendar.getInstance();
        String todayStr = formatDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        updateListByDate(todayStr);

        // 날짜 선택 이벤트 처리
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String selectedDate = formatDate(year, month, dayOfMonth);
            updateListByDate(selectedDate);
        });

        return view;
    }

    // yyyy-MM-dd 형식으로 날짜 문자열 만들기
    private String formatDate(int year, int month, int day) {
        return String.format("%04d-%02d-%02d", year, month + 1, day);
    }

    // DB에서 해당 날짜의 콘텐츠만 가져오기
    private void updateListByDate(String date) {
        new Thread(() -> {
            List<Content> result = new ArrayList<>();

            // watchedDate 기반 콘텐츠
            List<Content> direct = MainActivity.db.appDao().getContentsByDate(date);
            result.addAll(direct);

            // readDate 기반 콘텐츠
            List<Content> read = MainActivity.db.appDao().getContentsByReadDate(date);
            for (Content c : read) {
                if (!containsContent(result, c.id)) {
                    result.add(c);
                }
            }

            // startDate / endDate 기반 콘텐츠
            List<Content> allContents = MainActivity.db.appDao().getAllContents();
            for (Content c : allContents) {
                if ((date.equals(c.startDate) || date.equals(c.endDate)) && !containsContent(result, c.id)) {
                    result.add(c);
                }
            }

            requireActivity().runOnUiThread(() -> {
                adapter.updateList(result);
            });
        }).start();
    }

    // 중복 방지용 헬퍼 메서드
    private boolean containsContent(List<Content> list, int contentId) {
        for (Content c : list) {
            if (c.id == contentId) return true;
        }
        return false;
    }
}
