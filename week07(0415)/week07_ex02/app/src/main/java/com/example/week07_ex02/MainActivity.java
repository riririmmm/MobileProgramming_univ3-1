package com.example.week07_ex02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    EditText edit;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);

        edit = (EditText) findViewById(R.id.edit1);
        img = (ImageView) findViewById(R.id.imageView);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.beom) {
            img.setImageResource(R.drawable.beom);
            return true;
        } else if (item.getItemId() == R.id.chooja) {
            img.setImageResource(R.drawable.chooja);
            return true;
        } else if (item.getItemId() == R.id.hanra) {
            img.setImageResource(R.drawable.hanra);
            return true;
        } else if (item.getItemId() == R.id.imgRotation) {
            int r = Integer.parseInt(String.valueOf(edit.getText()));
            img.setRotation(r);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu1, menu);
        return true;
    }
}