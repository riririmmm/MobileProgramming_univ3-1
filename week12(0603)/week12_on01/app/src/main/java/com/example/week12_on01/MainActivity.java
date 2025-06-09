package com.example.week12_on01;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database;
    String dbName = "myDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 요청
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }

        // 버튼 연결 및 기능 설정
        Button btnCreateDB = findViewById(R.id.btnCreateDB);
        Button btnExportDB = findViewById(R.id.btnExportDB);

        btnCreateDB.setOnClickListener(v -> createDatabase());
        btnExportDB.setOnClickListener(v -> copyDatabaseToSdCard());
    }


    private void createDatabase() {
        try {
            // 데이터베이스 열기 또는 생성
            database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            // 테이블 생성
            database.execSQL("CREATE TABLE IF NOT EXISTS product (" +
                    "name CHAR(20), " +
                    "price INTEGER, " +
                    "makeDate CHAR(20), " +
                    "company CHAR(20), " +
                    "amount INTEGER);");

            // 기존 데이터 삭제 후 삽입 (중복 방지용)
            database.execSQL("DELETE FROM product");

            // 데이터 삽입
            database.execSQL("INSERT INTO product VALUES ('TV', 100, '2017.7.22', 'Samsung', 55);");
            database.execSQL("INSERT INTO product VALUES ('Computer', 150, '2019.5.5', 'LG', 7);");
            database.execSQL("INSERT INTO product VALUES ('Monitor', 50, '2021.9.9', 'Daewoo', 33);");

            Toast.makeText(this, "DB 생성 및 데이터 삽입 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "오류 발생: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void copyDatabaseToSdCard() {
        try {
            File srcFile = new File("/data/data/" + getPackageName() + "/databases/" + dbName);
            File dstFile = new File(Environment.getExternalStorageDirectory(), dbName + "_copy.db");

            InputStream in = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(dstFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            Toast.makeText(this, "SD카드로 복사 완료", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "복사 실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
