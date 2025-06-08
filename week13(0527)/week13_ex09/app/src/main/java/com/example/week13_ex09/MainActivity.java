package com.example.week13_ex09;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 화면에 있는 UI 요소들을 참조하기 위한 변수 선언
    SeekBar pb1, pb2;         // 첫 번째와 두 번째 SeekBar
    TextView tvProgress1, tvProgress2; // 각각 SeekBar 위에 표시할 진행률 텍스트
    Button btn;               // "스레드 시작" 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           // 액티비티 초기화
        setContentView(R.layout.activity_main);       // activity_main.xml 파일과 연결

        // XML에서 정의한 뷰들을 ID로 찾아와 자바 변수와 연결
        pb1 = findViewById(R.id.pb1);                 // 첫 번째 SeekBar
        pb2 = findViewById(R.id.pb2);                 // 두 번째 SeekBar
        tvProgress1 = findViewById(R.id.tvProgress1); // 첫 번째 진행률 텍스트
        tvProgress2 = findViewById(R.id.tvProgress2); // 두 번째 진행률 텍스트
        btn = findViewById(R.id.button1);             // 버튼

        // 버튼이 클릭되었을 때 실행될 코드 정의
        btn.setOnClickListener(v -> {

            // 첫 번째 SeekBar를 위한 새로운 스레드 시작
            new Thread(() -> {
                // 현재 SeekBar의 위치부터 100까지 2씩 증가
                for (int i = pb1.getProgress(); i <= 100; i += 2) {
                    int progress = i; // runOnUiThread 내부에서 사용하기 위해 final로 선언

                    // UI 요소는 메인(UI) 스레드에서만 변경 가능하므로 runOnUiThread 사용
                    runOnUiThread(() -> {
                        pb1.setProgress(progress); // SeekBar의 현재 위치 설정
                        tvProgress1.setText("1번 진행률 : " + progress + "%"); // 텍스트 갱신
                    });

                    // 너무 빠르게 변하지 않도록 100ms 대기 (0.1초)
                    SystemClock.sleep(100);
                }
            }).start(); // 스레드 시작

            // 두 번째 SeekBar를 위한 또 다른 스레드 시작
            new Thread(() -> {
                // 현재 SeekBar 위치부터 100까지 1씩 증가
                for (int i = pb2.getProgress(); i <= 100; i++) {
                    int progress = i;

                    // 메인(UI) 스레드에서 진행률과 텍스트 업데이트
                    runOnUiThread(() -> {
                        pb2.setProgress(progress); // SeekBar 위치 설정
                        tvProgress2.setText("2번 진행률 : " + progress + "%"); // 텍스트 갱신
                    });

                    // 0.1초 대기 후 다음 단계로 진행
                    SystemClock.sleep(100);
                }
            }).start(); // 스레드 시작
        });
    }
}
