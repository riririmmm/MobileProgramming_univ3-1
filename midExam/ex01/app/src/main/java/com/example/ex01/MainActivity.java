package com.example.ex01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editName, editEmail;
    RadioGroup rdoGroup;
    RadioButton rdo1, rdo2, rdo3;
    Spinner spinner;
    CheckBox ch;
    Button btn;
    TextView textView;
    String result;
    Boolean isEdit, isRdo, isSpinner, isCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        rdoGroup = (RadioGroup) findViewById(R.id.rdoGroup);
        rdo1 = (RadioButton) findViewById(R.id.rdo1);
        rdo2 = (RadioButton) findViewById(R.id.rdo2);
        rdo3 = (RadioButton) findViewById(R.id.rdo3);
        spinner = (Spinner) findViewById(R.id.spinner);
        ch = (CheckBox) findViewById(R.id.checkbox);
        btn = (Button) findViewById(R.id.btn1);
        textView = (TextView) findViewById(R.id.textView);

        String[] sessions = {"키노트", "안드로이드 개발", "웹 프론트엔드", "데이터 과학", "클라우드 컴퓨팅"}; //스피너 아이템 리스트

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sessions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinnerSession은 xml의 spinner 객체를 가르킴
        spinner.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();

                String edit = null;
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이름과 이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    isEdit = false;
                } else {
                    edit = "[신청 결과]\n이름: " + name + "\n이메일: " + email + "\n";
                    isEdit = true;
                }

                String rdo = null;
                if (rdo1.isChecked()) {
                    isRdo = true;
                    rdo = "참가 유형: 학생\n";
                } else if (rdo2.isChecked()) {
                    isRdo = true;
                    rdo = "참가 유형: 일반\n";
                } else if (rdo3.isChecked()) {
                    isRdo = true;
                    rdo = "참가 유형: 기업\n";
                } else {
                    Toast.makeText(getApplicationContext(), "참가 유형을 선택해주세요", Toast.LENGTH_SHORT).show();
                    isRdo = false;
                }

                String spin = null;
                if (spinner.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "관심 세션을 선택하세요.", Toast.LENGTH_SHORT).show();
                    isSpinner = false;
                } else {
                    spin = "관심 세션: " + spinner.getSelectedItem().toString();
                    isSpinner = true;
                }

                String checked = null;
                if (ch.isChecked()) {
                    checked = "개인정보 동의: 동의함";
                    isCheck = true;
                } else {
                    Toast.makeText(getApplicationContext(), "개인정보 동의에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    isCheck = false;
                }

                if (isEdit && isRdo && isSpinner && isCheck) {
                    result = edit + rdo + spin + checked;
                    textView.setText(result);
                }
            }
        });
    }
}