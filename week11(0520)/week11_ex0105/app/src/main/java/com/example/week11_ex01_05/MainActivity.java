package com.example.week11_ex01_05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("리스트뷰 테스트");

        final String[] mid = { "히어로즈", "24시", "로스트", "로스트룸 ", "스몰빌", "탐정몽크",
                "빅뱅이론", "프렌즈", "덱스터", "글리", "가쉽걸", "테이큰", "슈퍼내추럴", "브이" };

        final ArrayList<String> midList = new ArrayList<>(Arrays.asList(mid));

        ListView list = (ListView) findViewById(R.id.listView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, midList);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, mid);    // 라디오 버튼
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, mid);  // 체크박스

        list.setAdapter(adapter);

        final EditText editText = (EditText) findViewById(R.id.editItem);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                midList.add(editText.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), midList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                midList.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}