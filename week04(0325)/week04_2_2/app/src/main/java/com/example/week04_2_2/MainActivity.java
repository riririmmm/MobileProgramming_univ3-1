package com.example.week04_2_2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tv2;
    Switch str;
    RadioGroup radioGroup;
    RadioButton rdoS, rdoT, rdoU;
    Button btnFinish, btnReset;
    ImageView img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv2 = (TextView) findViewById(R.id.testView2);
        str = (Switch) findViewById(R.id.str);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        rdoS = (RadioButton) findViewById(R.id.rdoS);
        rdoT = (RadioButton) findViewById(R.id.rdoT);
        rdoU = (RadioButton) findViewById(R.id.rdoU);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnReset = (Button) findViewById(R.id.btnReset);
        img = (ImageView) findViewById(R.id.imgAnimal);

        str.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (str.isChecked()) {
                    tv2.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    btnFinish.setVisibility(View.VISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                } else {
                    tv2.setVisibility(View.INVISIBLE);
                    radioGroup.setVisibility(View.INVISIBLE);
                    btnFinish.setVisibility(View.INVISIBLE);
                    btnReset.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.INVISIBLE);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int rdo = radioGroup.getCheckedRadioButtonId();

                if (rdo == R.id.rdoS)
                    img.setImageResource(R.drawable.cat);
                else if (rdo == R.id.rdoT)
                    img.setImageResource(R.drawable.android13);
                else if (rdo == R.id.rdoU)
                    img.setImageResource(R.drawable.android14);
                else if (rdo == -1) {
                    Toast.makeText(getApplicationContext(), "리셋합니다.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "동물을 먼저 선택하세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup.clearCheck();
                str.setChecked(false);
                img.setImageResource(0);
                tv2.setVisibility(View.INVISIBLE);
                radioGroup.setVisibility(View.INVISIBLE);
                btnFinish.setVisibility(View.INVISIBLE);
                btnReset.setVisibility(View.INVISIBLE);
                img.setVisibility(View.INVISIBLE);
            }
        });
    }
}