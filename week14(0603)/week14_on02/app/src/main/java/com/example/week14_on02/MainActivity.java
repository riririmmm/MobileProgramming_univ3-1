package com.example.week14_on02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView imageBattery;    // 배터리 이미지 아이콘
    EditText editBattery;      // 배터리 상태를 출력하는 텍스트 상자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("배터리 상태 체크");

        // 레이아웃의 View들을 연결
        imageBattery = (ImageView) findViewById(R.id.ivBattery);
        editBattery = (EditText) findViewById(R.id.editBattery);
    }

    // 브로드캐스트 수신자: 배터리 상태가 변경될 때마다 호출됨
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();  // 수신된 인텐트의 액션 문자열을 가져옴

            // 배터리 상태가 변경되었을 때만 동작
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                // 1. 배터리 잔량 가져오기
                int remain = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

                // EditText에 배터리 퍼센트 표시
                editBattery.setText("현재 충전량: " + remain + "%\n");

                // 2. 잔량에 따라 이미지 변경
                if (remain >= 90)
                    imageBattery.setImageResource(R.drawable.battery_100);
                else if (remain >= 70)
                    imageBattery.setImageResource(R.drawable.battery_80);
                else if (remain >= 50)
                    imageBattery.setImageResource(R.drawable.battery_60);
                else if (remain >= 20)
                    imageBattery.setImageResource(R.drawable.battery_20);
                else
                    imageBattery.setImageResource(R.drawable.battery_0);

                // 3. 전원 연결 상태 확인
                int plug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
                switch (plug) {
                    case 0:
                        editBattery.append("전원 연결: 안됨");
                        break;
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        editBattery.append("전원 연결: 어댑터 연결됨");
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        editBattery.append("전원 연결: USB 연결됨");
                        break;
                }

                // 4. 배터리 상태(EXTRA_STATUS)를 가져와 토스트 메시지로 출력
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                String statusStr = "";

                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusStr = "배터리 상태: 충전 중";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusStr = "배터리 상태: 충전 완료";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        statusStr = "배터리 상태: 방전 중";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        statusStr = "배터리 상태: 충전 안 함";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        statusStr = "배터리 상태: 알 수 없음";
                        break;
                }

                // 토스트로 출력
                Toast.makeText(getApplicationContext(), statusStr, Toast.LENGTH_SHORT).show();
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

        // 배터리 상태 변경에 반응하기 위한 인텐트 필터 생성
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED); // 배터리 상태 변경 인텐트 감지
        registerReceiver(br, intentFilter); // 수신자 등록
    }
}
