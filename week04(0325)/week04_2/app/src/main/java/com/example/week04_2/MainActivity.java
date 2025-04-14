package com.example.week04_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tv2;
    CheckBox ch1;
    RadioGroup radioGroup;
    RadioButton rdoCat, rdoBird, rdoGuri;
    Button btn;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv2 = (TextView) findViewById(R.id.testView2);
        ch1 = (CheckBox) findViewById(R.id.checkbox);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        rdoCat = (RadioButton) findViewById(R.id.rdoCat);
        rdoBird = (RadioButton) findViewById(R.id.rdoBird);
        rdoGuri = (RadioButton) findViewById(R.id.rdoGuri);
        btn = (Button) findViewById(R.id.btn1);
        img = (ImageView) findViewById(R.id.imgAnimal);

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ch1.isChecked()) {
                    tv2.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                } else {
                    tv2.setVisibility(View.INVISIBLE);
                    radioGroup.setVisibility(View.INVISIBLE);
                    btn.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chRdo = radioGroup.getCheckedRadioButtonId();

                if (chRdo == R.id.rdoCat)
                    img.setImageResource(R.drawable.cat);
                else if (chRdo == R.id.rdoBird)
                    img.setImageResource(R.drawable.bird);
                else if (chRdo == R.id.rdoGuri)
                    img.setImageResource(R.drawable.neoguri);
                else {
                    Toast.makeText(getApplicationContext(), "동물을 먼저 선택하세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}