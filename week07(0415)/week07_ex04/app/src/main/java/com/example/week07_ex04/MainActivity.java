package com.example.week07_ex04;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvName, tvEmail;
    Button btn;
    EditText dlgEdit1, dlgEdit2;
    TextView toastText;
    View dialogView, toastTextView;
    Button hint1, hint2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btn = (Button) findViewById(R.id.btn1);

        hint1 = (Button) findViewById(R.id.hint1);
        hint2 = (Button) findViewById(R.id.hint2);

        hint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "dialogView = (View) View.inflate(MainActivity.this,\n" +
                                "                        R.layout.dialog1, null);\n" +
                                "                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);\n" +
                                "                dlg.setTitle(\"사용자 정보 입력\");\n" +
                                "                dlg.setIcon(R.drawable.btn_star_big_on);\n" +
                                "                dlg.setView(dialogView);\n" +
                                "                dlg.setPositiveButton(\"확인\", new DialogInterface.OnClickListener() {\n" +
                                "                    @Override\n" +
                                "                    public void onClick(DialogInterface dialog, int which) {\n" +
                                "                        dlgEdit1 = (EditText) dialogView.findViewById(R.id.dlgEdit1);\n" +
                                "                        dlgEdit2 = (EditText) dialogView.findViewById(R.id.dlgEdit2);\n" +
                                "\n" +
                                "                        tvName.setText(dlgEdit1.getText().toString());\n" +
                                "                        tvEmail.setText(dlgEdit2.getText().toString());\n" +
                                "                    }\n" +
                                "                });",
                        Toast.LENGTH_SHORT).show();
            }
        });

        hint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "dlg.setNegativeButton(\"취소\", new DialogInterface.OnClickListener() {\n" +
                        "                    @Override\n" +
                        "                    public void onClick(DialogInterface dialog, int which) {\n" +
                        "                        Toast toast = new Toast(MainActivity.this);\n" +
                        "                        toastTextView = (View) View.inflate(MainActivity.this,\n" +
                        "                                R.layout.toast1, null);\n" +
                        "                        toastText = (TextView) toastTextView.findViewById(R.id.toastText);\n" +
                        "                        toastText.setText(\"취소되었습니다.\");\n" +
                        "                        toast.setView(toastTextView);\n" +
                        "                        toast.show();\n" +
                        "                    }\n" +
                        "                });\n" +
                        "                dlg.show();", Toast.LENGTH_SHORT).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = (View) View.inflate(MainActivity.this,
                        R.layout.dialog1, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("사용자 정보 입력");
                dlg.setIcon(R.drawable.btn_star_big_on);
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dlgEdit1 = (EditText) dialogView.findViewById(R.id.dlgEdit1);
                        dlgEdit2 = (EditText) dialogView.findViewById(R.id.dlgEdit2);

                        tvName.setText(dlgEdit1.getText().toString());
                        tvEmail.setText(dlgEdit2.getText().toString());
                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = new Toast(MainActivity.this);
                        toastTextView = (View) View.inflate(MainActivity.this,
                                R.layout.toast1, null);
                        toastText = (TextView) toastTextView.findViewById(R.id.toastText);
                        toastText.setText("취소되었습니다.");
                        toast.setView(toastTextView);
                        toast.show();
                    }
                });
                dlg.show();
            }
        });
    }
}