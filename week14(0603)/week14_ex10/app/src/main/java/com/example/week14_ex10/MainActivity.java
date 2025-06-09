package com.example.week14_ex10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.provider.CallLog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnCall;
    EditText edtCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // XML 레이아웃 연결

        // 런타임 권한 요청 (API 23 이상 필수)
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALL_LOG},
                PackageManager.PERMISSION_GRANTED);

        // 위젯 ID와 Java 변수 연결
        btnCall = (Button) findViewById(R.id.btnCall);
        edtCall = (EditText) findViewById(R.id.edtCall);

        // 버튼 클릭 시 통화 기록 가져오기
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCall.setText(getCallHistory()); // EditText에 통화 기록 출력
            }
        });
    }

    // 통화 기록을 문자열로 반환하는 메서드
    public String getCallHistory() {
        // 가져올 항목: 날짜, 통화 유형, 전화번호, 통화 시간
        String[] callSet = new String[]{
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION
        };

        // CallLog 콘텐츠 프로바이더를 통해 통화 기록 조회
        Cursor c = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, callSet,
                null, null, null
        );

        // 커서가 null이거나 레코드가 없으면
        if (c == null || c.getCount() == 0) {
            if (c != null) c.close();
            return "통화기록 없음";
        }

        // 문자열을 누적할 버퍼 생성
        StringBuffer callBuff = new StringBuffer();
        callBuff.append("\n날짜 : 구분 : 전화번호 : 통화시간\n\n");

        // 커서를 첫 번째 레코드로 이동
        c.moveToFirst();
        do {
            // 1. 통화 날짜 가져오기
            long callDate = c.getLong(0);
            SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
            String date_str = datePattern.format(new Date(callDate));
            callBuff.append(date_str + " : ");

            if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
                callBuff.append("수신 : ");
            else
                callBuff.append("발신 : ");

            // 3. 전화번호와 통화 시간 가져오기
            callBuff.append(c.getString(2) + " : ");
            callBuff.append(c.getString(3) + "초\n");

        } while (c.moveToNext()); // 다음 통화 기록이 있을 때까지 반복

        c.close(); // 커서 닫기
        return callBuff.toString(); // 최종 문자열 반환
    }
}
