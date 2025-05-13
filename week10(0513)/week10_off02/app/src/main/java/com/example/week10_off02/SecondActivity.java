package com.example.week10_off02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class SecondActivity extends Activity {

    int hapValue = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        setTitle("Second 액티비티");

        Intent inIntent = getIntent();

        String operation = inIntent.getStringExtra("OPERATION");

        if (operation.equals("ADD")) {
            hapValue = inIntent.getIntExtra("Num1", 0) + inIntent.getIntExtra("Num2", 0);
        } else if (operation.equals("SUB")) {
            hapValue = inIntent.getIntExtra("Num1", 0) - inIntent.getIntExtra("Num2", 0);
        } else if (operation.equals("MUL")) {
            hapValue = inIntent.getIntExtra("Num1", 0) * inIntent.getIntExtra("Num2", 0);
        } else if (operation.equals("DIV")) {
            hapValue = inIntent.getIntExtra("Num1", 0) / inIntent.getIntExtra("Num2", 0);
        }

        Button btnReturn = (Button) findViewById(R.id.btnReturn);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("Hap", hapValue);
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }
}