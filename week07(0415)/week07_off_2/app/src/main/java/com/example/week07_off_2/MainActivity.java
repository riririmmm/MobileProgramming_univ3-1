package com.example.week07_off_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editName, editEmail;
    Button btn;

    EditText dlgEditName, dlgEditEmail;

    View dialogView, toastView;

    TextView toastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = (EditText) findViewById(R.id.editText1);
        editEmail = (EditText) findViewById(R.id.edteText2);
        btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogView = (View) View.inflate(MainActivity.this, R.layout.dialog1, null);

                dlgEditName = (EditText) dialogView.findViewById(R.id.dialogEdit1);
                dlgEditEmail = (EditText) dialogView.findViewById(R.id.dialogEdit2);

                dlgEditName.setText(editName.getText().toString());
                dlgEditEmail.setText(editEmail.getText().toString());

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);

                dlg.setTitle("사용자 정보 입력");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", null);
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = new Toast(MainActivity.this);
                        toastView = (View) View.inflate(MainActivity.this, R.layout.toast1, null);

                        toastText = (TextView) toastView.findViewById(R.id.textView);
                        toast.setView(toastView);

                        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

                        int xOffset = (int) (Math.random() * display.getWidth());
                        int yOffset = (int) (Math.random() * display.getHeight());
                        
                        toast.setGravity(Gravity.TOP|Gravity.LEFT, xOffset, yOffset);
                                
                        toast.show();
                    }
                });
                dlg.show();
            }
        });
    }

}