package com.example.week07_off;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imgView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemRotaion) {
            imageView.setRotation(Float.parseFloat(editText.getText().toString()));
            return true;
        } else if (item.getItemId() == R.id.itemHanra) {
            imageView.setImageResource(R.drawable.hanra);
            item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.itemChooja) {
            imageView.setImageResource(R.drawable.chooja);
            item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.itemBeom) {
            imageView.setImageResource(R.drawable.beom);
            item.setChecked(true);
            return true;
        }

        return false;
    }

}