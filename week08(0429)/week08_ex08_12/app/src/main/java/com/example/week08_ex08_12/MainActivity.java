package com.example.week08_ex08_12;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnRead;
    EditText editSD;

    Button btnMkdir, btnRmdir;

    Button btnFileList;
    EditText editFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SD카드 파일");

        // ex8-10. SD카드에서 파일 읽기
        btnRead = (Button) findViewById(R.id.btnRead);
        editSD = (EditText) findViewById(R.id.editSD);

        // 앱에서 외부 저장소(예: SD카드)에 파일을 쓰기 위해 사용자에게 권한을 요청
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream inFs = new FileInputStream("/sdcard/SDCardFile.txt");
                    byte[] txt = new byte[inFs.available()];
                    inFs.read(txt);
                    editSD.setText(new String(txt));
                    inFs.close();
                } catch (IOException e) { }
            }
        });



        // ex8-12. SD카드
//        ActivityCompat.requestPermissions(this,
//                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        btnMkdir = (Button) findViewById(R.id.btnMkdir);
        btnRmdir = (Button) findViewById(R.id.btnRmdir);

        final String strSDpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File myDir = new File(strSDpath + "/myDir");

        btnMkdir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDir.mkdir();
                Toast.makeText(getApplicationContext(), myDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        });

        btnRmdir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDir.delete();
            }
        });



        // ex8-14. 시스템 폴더의 폴더 및 파일 목록
        btnFileList = (Button) findViewById(R.id.btnFileList);
        editFileList = (EditText) findViewById(R.id.editFileList);

        btnFileList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sysDir = Environment.getRootDirectory().getAbsolutePath();
                File[] sysFiles = (new File(sysDir).listFiles());
                String strFnames;
                for (int i = 0; i < sysFiles.length; i++) {
                    if (sysFiles[i].isDirectory() == true)
                        strFnames = "<폴더> " + sysFiles[i].toString();
                    else
                        strFnames = "<파일> " + sysFiles[i].toString();

                    editFileList.setText(editFileList.getText() + "\n" + strFnames);
                }
            }
        });
    }
}