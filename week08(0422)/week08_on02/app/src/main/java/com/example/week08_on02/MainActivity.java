package com.example.week08_on02;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends Activity {

    Button btnPrev, btnNext;
    myPictureView myPicture;
    int curNum = 0;
    File[] imageFile = new File[0];
    String imageFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("간단 이미지 뷰어");

        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        myPicture = (com.example.week08_on02.myPictureView) findViewById(R.id.myPictureView1);

        File[] allFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/pictures").listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].isFile()) {
                imageFile = Arrays.copyOf(imageFile, imageFile.length + 1);
                imageFile[imageFile.length-1] = allFiles[i];
            }
        }

        imageFilename = imageFile[curNum].toString();
        myPicture.imagePath = imageFilename;

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curNum <= 0) {
                    Toast.makeText(getApplicationContext(), "첫번째 그림입니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    curNum--;
                    imageFilename = imageFile[curNum].toString();
                    myPicture.imagePath = imageFilename;
                    myPicture.invalidate();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curNum >= imageFile.length - 1) {
                    Toast.makeText(getApplicationContext(), "마지막 그림입니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    curNum++;
                    imageFilename = imageFile[curNum].toString();
                    myPicture.imagePath = imageFilename;
                    myPicture.invalidate();
                }
            }
        });
    }
}