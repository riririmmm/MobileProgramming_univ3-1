package com.example.ex01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String desc = intent.getStringExtra("desc");
        int[] imageSrc = intent.getIntArrayExtra("imageSrc");

        TextView tvTitle = (TextView) findViewById(R.id.second_title);
        ImageView ivDetail = (ImageView) findViewById(R.id.second_Image);
        TextView tvDesc = (TextView) findViewById(R.id.second_desc);

        tvTitle.setText(name);
        tvDesc.setText(desc);

        switch (name) {
            case "Dog":
                ivDetail.setImageResource(imageSrc[0]);
                break;
            case "Cat":
                ivDetail.setImageResource(imageSrc[1]);
                break;
            case "Duck":
                ivDetail.setImageResource(imageSrc[2]);
                break;
        }
    }

}
