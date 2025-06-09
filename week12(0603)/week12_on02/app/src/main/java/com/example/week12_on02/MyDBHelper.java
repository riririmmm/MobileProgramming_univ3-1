package com.example.week12_on02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// SQLiteOpenHelper를 상속하여 DB를 관리하는 클래스
public class MyDBHelper extends SQLiteOpenHelper {

    // 생성자: DB 이름은 groupDB, 버전은 1
    public MyDBHelper(Context context) {
        super(context, "groupDB", null, 1);
    }

    // 최초로 DB가 생성될 때 호출됨 → 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 그룹 이름을 기본키로 하고, 인원 수는 정수형
        db.execSQL("CREATE TABLE groupTBL (gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
    }

    // DB 버전이 변경될 때 호출됨 → 기존 테이블 제거 후 재생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 제거
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
        // 새 테이블 생성
        onCreate(db);
    }
}
