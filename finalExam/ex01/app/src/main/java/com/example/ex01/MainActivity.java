package com.example.ex01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] animalName = { "Dog", "Cat", "Duck" };
        final ArrayList<String> nameList =new ArrayList<>(Arrays.asList(animalName));

        final int[] animalImageID = { R.drawable.dog, R.drawable.cat, R.drawable.duck };

        final String[] animalDesc = { "강아지 설명", "고양이 설명", "오리 설명" };

        ListView listView = (ListView) findViewById(R.id.listView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(adapter);

//        final TextView title = (TextView) findViewById(R.id.textView);
//        final ImageView imageView = (ImageView) findViewById(R.id.ImageView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("name", nameList.get(position));
                intent.putExtra("imageSrc", animalImageID);
                intent.putExtra("desc", animalDesc[position]);
                startActivity(intent);
            }
        });

    }
}