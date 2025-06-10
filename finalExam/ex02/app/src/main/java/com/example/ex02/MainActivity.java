package com.example.ex02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2;
    Button btn;

    String fileName, str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Battery Tracker");

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        btn = findViewById(R.id.btn1);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.append("최근 배터리 로그(최대 10개): \n");
                FileInputStream inFs = null;
                try {
                    inFs = new FileInputStream("/sdcard/battery_log.txt");
                } catch (FileNotFoundException e) { }
                byte[] txt = new byte[0];
                try {
                    txt = new byte[inFs.available()];
                } catch (IOException e) { }
                try {
                    inFs.read(txt);
                } catch (IOException e) {
                }
                tv2.setText(new String(txt));
                try {
                    inFs.close();
                } catch (IOException e) {
                }
            }
        });

    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {

                int remain = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                tv1.setText("Battery: " + remain + "%");

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        tv1.setText("Battery: " + remain + "%, Charging: true");
                        str = System.currentTimeMillis() + ", " + remain + ", true";
                        break;
                    default:
                        tv1.setText("Battery: " + remain + "%, Charging: false");
                        str = System.currentTimeMillis() + ", " + remain + ", false";
                        break;
                }

                fileName = "battery_log.txt";

                FileOutputStream outFs = null;
                try {
                    outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                }
                try {
                    outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) { }
                try {
                    outFs.write(str.getBytes());
                } catch (IOException e) { }
                try {
                    outFs.close();
                } catch (IOException e) { }

            }
        }
    };

    // 액티비티가 중지될 때 → 수신자 해제
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br); // 리소스 절약을 위해 등록 해제
    }

    // 액티비티가 다시 화면에 보일 때 → 수신자 등록
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED); // 배터리 상태 변경 인텐트 감지
        registerReceiver(br, intentFilter); // 수신자 등록
    }
}