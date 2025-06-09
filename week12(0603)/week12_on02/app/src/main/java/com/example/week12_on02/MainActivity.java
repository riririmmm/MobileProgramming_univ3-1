package com.example.week12_on02;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;                     // DB에서 데이터를 가져오기 위한 Cursor 클래스
import android.database.sqlite.SQLiteDatabase;     // SQLite DB 접근 클래스
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;                        // 사용자에게 짧은 메시지를 띄울 때 사용

public class MainActivity extends AppCompatActivity {

    // DB 헬퍼 클래스 인스턴스
    MyDBHelper myHelper;

    // 입력 및 출력에 사용될 EditText들
    EditText edtName, edtNumber;             // 사용자로부터 이름과 인원 수를 입력받음
    EditText edtNameResult, edtNumberResult; // DB 조회 결과 출력용 (이름 / 인원)

    // 각종 기능 버튼
    Button btnInit, btnInsert, btnSelect, btnUpdate, btnDelete;

    // SQLite DB 객체
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // 레이아웃과 연결

        // [1] 레이아웃의 각 뷰와 자바 변수 연결
        edtName = findViewById(R.id.edtName);
        edtNumber = findViewById(R.id.edtNumber);
        edtNameResult = findViewById(R.id.edtNameResult);
        edtNumberResult = findViewById(R.id.edtNumberResult);

        btnInit = findViewById(R.id.btnInit);
        btnInsert = findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);
        btnUpdate = findViewById(R.id.btnUpdate);   // 수정 기능 버튼
        btnDelete = findViewById(R.id.btnDelete);   // 삭제 기능 버튼

        // DB 헬퍼 객체 생성 (DB 생성 및 테이블 생성도 내부적으로 이뤄짐)
        myHelper = new MyDBHelper(this);

        // [2] 초기화 버튼: 기존 테이블 삭제 후 재생성
        btnInit.setOnClickListener(v -> {
            sqlDB = myHelper.getWritableDatabase();     // 쓰기 가능 DB 열기
            myHelper.onUpgrade(sqlDB, 1, 2);            // 강제로 onUpgrade 호출 → 테이블 삭제 및 생성
            sqlDB.close();                              // DB 닫기
            Toast.makeText(getApplicationContext(), "초기화됨", Toast.LENGTH_SHORT).show();
            callSelect();                               // 초기화 후 조회 결과 자동 갱신
        });

        // [3] 입력 버튼: 이름과 인원 수를 DB에 저장
        btnInsert.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();       // 이름 입력값
            String numberStr = edtNumber.getText().toString().trim(); // 인원 입력값

            // 이름 또는 인원 수가 비어 있는 경우 처리
            if (name.isEmpty() || numberStr.isEmpty()) {
                Toast.makeText(getApplicationContext(), "이름과 인원을 모두 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int number = Integer.parseInt(numberStr);  // 숫자인지 확인
                sqlDB = myHelper.getWritableDatabase();    // DB 열기

                // 동일 이름이 있으면 덮어쓰기
                sqlDB.execSQL("INSERT OR REPLACE INTO groupTBL VALUES('" + name + "', " + number + ");");
                sqlDB.close();                             // DB 닫기

                Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                callSelect();                              // 자동 조회로 반영 결과 확인
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "인원은 숫자로 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        // [4] 조회 버튼: DB의 전체 내용을 하단 출력창에 표시
        btnSelect.setOnClickListener(v -> callSelect());

        // [5] 수정 버튼: 이름이 동일한 레코드의 인원 수를 업데이트
        btnUpdate.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String numberStr = edtNumber.getText().toString().trim();

            if (name.isEmpty() || numberStr.isEmpty()) {
                Toast.makeText(getApplicationContext(), "이름과 인원을 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int number = Integer.parseInt(numberStr);  // 숫자인지 확인
                sqlDB = myHelper.getWritableDatabase();

                // 해당 이름의 인원 정보를 수정
                sqlDB.execSQL("UPDATE groupTBL SET gNumber = " + number + " WHERE gName = '" + name + "';");
                sqlDB.close();

                Toast.makeText(getApplicationContext(), "수정됨", Toast.LENGTH_SHORT).show();
                callSelect();  // 자동 조회
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "인원은 숫자로 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        // [6] 삭제 버튼: 이름에 해당하는 그룹을 DB에서 제거
        btnDelete.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "삭제할 그룹 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            sqlDB = myHelper.getWritableDatabase();

            // 해당 이름의 레코드를 삭제
            sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '" + name + "';");
            sqlDB.close();

            Toast.makeText(getApplicationContext(), "삭제됨", Toast.LENGTH_SHORT).show();
            callSelect(); // 삭제 결과 자동 반영
        });
    }

    // 🔁 공통 조회 함수: groupTBL 전체 내용을 하단 EditText에 표시
    private void callSelect() {
        sqlDB = myHelper.getReadableDatabase(); // 읽기 전용 DB 열기
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null); // 전체 조회 쿼리

        // 제목 + 구분선으로 초기화
        String strNames = "그룹이름\r\n----------\r\n";
        String strNumbers = "인원\r\n----------\r\n";

        // 커서를 통해 레코드 읽기 (0번: 이름, 1번: 인원)
        while (cursor.moveToNext()) {
            strNames += cursor.getString(0) + "\r\n";
            strNumbers += cursor.getString(1) + "\r\n";
        }

        // 결과 출력
        edtNameResult.setText(strNames);
        edtNumberResult.setText(strNumbers);

        cursor.close();  // 커서 해제
        sqlDB.close();   // DB 닫기
    }
}
