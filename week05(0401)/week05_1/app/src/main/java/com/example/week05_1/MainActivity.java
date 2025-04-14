package com.example.week05_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edit1, edit2;
    Button btnAdd, btnSub, btnMul, btnDiv;
    TextView textView;
    String num1, num2;
    Float result;
    Button[] numBtns = new Button[10];
    Integer[] btnIds = { R.id.Btn0, R.id.Btn1, R.id.Btn2, R.id.Btn3, R.id.Btn4,
            R.id.Btn5, R.id.Btn6, R.id.Btn7, R.id.Btn8, R.id.Btn9 };
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("테이블레이아웃 계산기");

        edit1 = (EditText) findViewById(R.id.Edit1);
        edit2 = (EditText) findViewById(R.id.Edit2);
        btnAdd = (Button) findViewById(R.id.BtnAdd);
        btnSub = (Button) findViewById(R.id.BtnSub);
        btnMul = (Button) findViewById(R.id.BtnMul);
        btnDiv = (Button) findViewById(R.id.BtnDiv);
        textView = (TextView) findViewById(R.id.TextResult);

        for (i = 0; i < numBtns.length; i++) {
            numBtns[i] = (Button) findViewById(btnIds[i]);
        }

        for (i = 0; i < numBtns.length; i++) {
            final int index;
            index = i;

            numBtns[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edit1.isFocused()) {
                        num1 = edit1.getText().toString()
                                + numBtns[index].getText().toString();
                        edit1.setText(num1);
                    } else if (edit2.isFocused()) {
                        num2 = edit2.getText().toString()
                                + numBtns[index].getText().toString();
                        edit2.setText(num2);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "먼저 에디트텍스트를 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    result = Float.parseFloat(num1) + Float.parseFloat(num2);
                    textView.setText("계산 결과: " + result.toString());
                }
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    result = Float.parseFloat(num1) - Float.parseFloat(num2);
                    textView.setText("계산 결과: " + result.toString());
                }
            }
        });

        btnMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    result = Float.parseFloat(num1) * Float.parseFloat(num2);
                    textView.setText("계산 결과: " + result.toString());
                }
            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    result = Float.parseFloat(num1) / Float.parseFloat(num2);
                    textView.setText("계산 결과: " + result.toString());
                }
            }
        });
    }
}