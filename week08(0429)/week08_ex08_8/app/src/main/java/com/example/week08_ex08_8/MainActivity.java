package com.example.week08_ex08_8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRead;
        final EditText editRaw;

        btnRead = (Button) findViewById(R.id.btnRead);
        editRaw = (EditText) findViewById(R.id.editRaw);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStream inputS = getResources().openRawResource(R.raw.raw_text);
                    byte[] txt = new byte[inputS.available()];
                    inputS.read(txt);
                    editRaw.setText(new String(txt));
                    inputS.close();
                } catch (IOException e) { }
            }
        });
    }
}