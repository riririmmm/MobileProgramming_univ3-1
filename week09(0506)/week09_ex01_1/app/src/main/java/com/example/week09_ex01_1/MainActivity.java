package com.example.week09_ex01_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyGraphicView(this)); // 커스텀 뷰 설정
    }

    private static class MyGraphicView extends View {
        public MyGraphicView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(30);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);

            // 선 3종: BUTT, ROUND, SQUARE
            paint.setStrokeCap(Paint.Cap.BUTT);
            canvas.drawLine(50, 50, 350, 50, paint);

            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(50, 120, 350, 120, paint);

            paint.setStrokeCap(Paint.Cap.SQUARE);
            canvas.drawLine(50, 190, 350, 190, paint);

            // 타원
            paint.setStyle(Paint.Style.FILL);
            RectF oval = new RectF(100, 230, 300, 280);
            canvas.drawOval(oval, paint);

            // 마름모 (Path 사용)
            Path diamond = new Path();
            diamond.moveTo(200, 300); // 위쪽
            diamond.lineTo(170, 330); // 왼쪽
            diamond.lineTo(200, 360); // 아래쪽
            diamond.lineTo(230, 330); // 오른쪽
            diamond.close();
            canvas.drawPath(diamond, paint);

            // 파란색 사각형
            paint.setColor(Color.BLUE);
            canvas.drawRect(130, 380, 230, 480, paint);

            // 반투명 빨간 사각형
            paint.setColor(Color.argb(150, 255, 0, 0));  // 알파: 150
            canvas.drawRect(160, 410, 260, 510, paint);
        }
    }
}
