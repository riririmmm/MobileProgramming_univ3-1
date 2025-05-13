package com.example.week10_off02;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("메인 엑티비티");

        Button btnNewActivity = (Button) findViewById(R.id.btnNewActivity);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rdoGruop);
        RadioButton rdoAdd = (RadioButton) findViewById(R.id.rdoAdd);
        RadioButton rdoSub = (RadioButton) findViewById(R.id.rdoSub);
        RadioButton rdoMul = (RadioButton) findViewById(R.id.rdoMul);
        RadioButton rdoDiv = (RadioButton) findViewById(R.id.rdoDiv);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == RESULT_OK) {
                        int hap = data.getIntExtra("Hap", 0);
                        Toast.makeText(getApplicationContext(), "합계 :" + hap, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtNum1 = (EditText) findViewById(R.id.edtNum1);
                EditText edtNum2 = (EditText) findViewById(R.id.edtNum2);
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("Num1", Integer.parseInt(edtNum1.getText().toString()));
                intent.putExtra("Num2", Integer.parseInt(edtNum2.getText().toString()));

                if (radioGroup.getCheckedRadioButtonId() == R.id.rdoAdd) {
                    intent.putExtra("OPERATION", "ADD");
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdoSub) {
                    intent.putExtra("OPERATION", "SUB");
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdoMul) {
                    intent.putExtra("OPERATION", "MUL");
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdoDiv) {
                    intent.putExtra("OPERATION", "DIV");
                }

                launcher.launch(intent);

                //startActivityForResult(intent, 0);
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            int hap = data.getIntExtra("Hap", 0);
            Toast.makeText(getApplicationContext(), "합계 :" + hap, Toast.LENGTH_SHORT).show();
        }
    }*/
}