package com.example.week11_on03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("스피너 테스트");

        Integer[] poseterID = {R.drawable.mov01, R.drawable.mov02, R.drawable.mov03, R.drawable.mov04, R.drawable.mov05,
                R.drawable.mov06, R.drawable.mov07, R.drawable.mov08, R.drawable.mov09, R.drawable.mov10,
                R.drawable.mov11, R.drawable.mov12, R.drawable.mov13, R.drawable.mov14, R.drawable.mov15,
                R.drawable.mov16, R.drawable.mov17, R.drawable.mov18, R.drawable.mov19, R.drawable.mov20,
                R.drawable.mov21, R.drawable.mov22, R.drawable.mov23, R.drawable.mov24, R.drawable.mov25
        };

        final String[] movie = { "써니", "완득이", "괴물", "라디오 스타", "비열한 거리",
                "왕의 남자", "아일랜드", "웰컴 투 동막골", "헬보이 골든아미", "Back to the Future",
                "여인의 향기", "쥬라기 공원", "포레스트 검프", "Groundhog Day", "흑성탈출 진화의 시작",
                "아름다운 비행", "내 이름은 칸", "해리포터", "마더", "킹콩을 들다",
                "쿵푸팬더 2", "짱구는 못말려", "아저씨", "아바타", "The Godfather" };

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, movie);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageView.setImageResource(poseterID[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}