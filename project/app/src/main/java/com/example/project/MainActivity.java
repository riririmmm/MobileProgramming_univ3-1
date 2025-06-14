package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Bundle;

import com.example.project.db.AppDatabase;
import com.example.project.fragment.CalendarFragment;
import com.example.project.fragment.ReviewFragment;
import com.example.project.fragment.SearchFragment;
import com.example.project.model.Content;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    // Room DB 인스턴스
    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db")
                .fallbackToDestructiveMigration()
                .build();

        // 하단 네비게이션 뷰 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 앱 시작 시 기본 프래그먼트 (캘린더)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CalendarFragment())
                .commit();

        // 기본 탭 선택 UI도 캘린더로 강조
        bottomNavigationView.setSelectedItemId(R.id.nav_calendar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            if (id == R.id.nav_review) {
                fragment = new ReviewFragment();
            } else if (id == R.id.nav_search) {
                fragment = new SearchFragment();
            } else {
                fragment = new CalendarFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        });
    }
}
