package com.example.week4_1_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edit1, edit2;
    Button BtnAdd, BtnSub, BtnMul, BtnDiv, BtnRem;
    TextView textResult;
    String num1, num2;
    Float result;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("초간단 계산기");

        edit1 = (EditText) findViewById(R.id.Edit1);
        edit2 = (EditText) findViewById(R.id.Edit2);
        BtnAdd = (Button) findViewById(R.id.BtnAdd);
        BtnSub = (Button) findViewById(R.id.BtnSub);
        BtnMul = (Button) findViewById(R.id.BtnMul);
        BtnDiv = (Button) findViewById(R.id.BtnDiv);
        BtnRem = (Button) findViewById(R.id.BtnRem);
        textResult = (TextView) findViewById(R.id.TextResult);

        String n1 = edit1.getText().toString();
        String n2 = edit2.getText().toString();

        BtnAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();
                result = Float.parseFloat(num1) + Float.parseFloat(num2);
                textResult.setText("계산 결과: " + result.toString());
                return false;
            }
        });

        BtnSub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();
                result = Float.parseFloat(num1) - Float.parseFloat(num2);
                textResult.setText("계산 결과: " + result.toString());
                return false;
            }
        });

        BtnMul.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();
                result = Float.parseFloat(num1) * Float.parseFloat(num2);
                textResult.setText("계산 결과: " + result.toString());
                return false;
            }
        });

        BtnDiv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();
                result = Float.parseFloat(num1) / Float.parseFloat(num2);
                textResult.setText("계산 결과: " + result.toString());
                return false;
            }
        });

        BtnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();
                if (num1.length() == 0 || num2.length() == 0) {
                    Toast.makeText(getApplicationContext(), "값이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else if (num2.equals("0")) {
                    Toast.makeText(getApplicationContext(), "0으로 나눌 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    result = Float.parseFloat(num1) % Float.parseFloat(num2);
                    textResult.setText("계산 결과: " + result.toString());
                }
            }
        });

    }
}