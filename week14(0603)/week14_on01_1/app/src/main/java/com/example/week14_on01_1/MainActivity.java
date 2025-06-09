package com.example.week14_on01_1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    Button btnStart, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("서비스 테스트 예제");

        // 음악 서비스를 실행/중지하기 위한 인텐트 준비
        intent = new Intent(this, MusicService.class);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서비스 시작
                startService(intent);

                // 토스트 메시지 출력
                Toast.makeText(getApplicationContext(), "음악 서비스 시작됨", Toast.LENGTH_SHORT).show();

                // 액티비티 종료 → 앱은 꺼지지만 음악은 계속 나옴
                finish();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서비스 중지
                stopService(intent);

                // 토스트 메시지 출력
                Toast.makeText(getApplicationContext(), "음악 서비스 중지됨", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
