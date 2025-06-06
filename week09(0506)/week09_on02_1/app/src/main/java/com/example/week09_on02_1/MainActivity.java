package com.example.week09_on02_1;

import static android.graphics.BlurMaskFilter.Blur.NORMAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    ImageButton ibZoomIn, ibZoomOut, ibRotate, ibBright, ibDark, ibGray, ibBlur, ibEmboss;
    MyGraphicView graphicView;
    static float scaleX = 1, scaleY = 1;
    static float angle = 0;
    static float color = 1;
    static float satur = 1;
    static boolean isBlur = false;
    static boolean isEmboss = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("미니 포토샵");

        LinearLayout pictureLayout = (LinearLayout) findViewById(R.id.pictureLayout);
        graphicView = (MyGraphicView) new MyGraphicView(this);
        pictureLayout.addView(graphicView);

        clickIcons();
    }

    private static class MyGraphicView extends View {
        public MyGraphicView(Context context) {
            super(context);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);

            int cenX = this.getWidth() / 2;
            int cenY = this.getHeight() / 2;

            Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.pic);

            // 1) 화면 중앙 좌표 계산
            int picX = (this.getWidth() - picture.getWidth()) / 2;
            int picY = (this.getHeight() - picture.getHeight()) / 2;

            canvas.scale(scaleX, scaleY, cenX, cenY);
            canvas.rotate(angle, cenX, cenY);

            Paint paint = new Paint();
            float[] array = {
                    color, 0, 0, 0, 0,
                    0, color, 0, 0, 0,
                    0, 0, color, 0, 0,
                    0, 0, 0, 1, 0
            };

            ColorMatrix cm = new ColorMatrix(array);

            if (satur == 0) cm.setSaturation(satur);

            paint.setColorFilter(new ColorMatrixColorFilter(cm));

            if (isBlur) {
                BlurMaskFilter blur = new BlurMaskFilter(30, NORMAL);
                paint.setMaskFilter(blur);
            } else if (isEmboss) {
                // 4-1) “알파 마스크”를 이용해 테두리만 엠보싱하려면,
                //      extractAlpha() 로 원본 픽셀 경계만 뽑아야 합니다.
                //      EmbossMaskFilter 는 알파 기반으로 입체감을 그려주기 때문입니다.
                Bitmap alphaBmp = picture.extractAlpha();

                // 4-2) EmbossMaskFilter 를 걸 Paint 생성
                Paint emPaint = new Paint();
                emPaint.setStyle(Paint.Style.STROKE);
                emPaint.setStrokeWidth(10f);         // 테두리 굵기
                emPaint.setColor(Color.WHITE);        // 테두리 색
                emPaint.setMaskFilter(new EmbossMaskFilter(
                        new float[]{10f, 3f, 3f}, // 빛 방향 (x, y, z)
                        0.5f,                     // ambient (주변광)
                        6f,                       // specular (반사광 강도)
                        3.5f                      // blur radius
                ));

                // 4-3) 알파 비트맵을 엠보싱 Paint 로 같은 위치에 그려주면
                //      이미지 경계만 양각(relief)처럼 강조됩니다.
                canvas.drawBitmap(alphaBmp, picX, picY, emPaint);
                alphaBmp.recycle();

            }

            canvas.drawBitmap(picture, picX, picY, paint);

            picture.recycle();
        }
    }

    private void clickIcons() {
        ibZoomIn = (ImageButton) findViewById(R.id.ibZoonIn);
        ibZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleX = scaleX + 0.2f;
                scaleY = scaleY + 0.2f;
                graphicView.invalidate();
            }
        });

        ibZoomOut = (ImageButton) findViewById(R.id.ibZoonOut);
        ibZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleX = scaleX - 0.2f;
                scaleY = scaleY - 0.2f;
                graphicView.invalidate();
            }
        });

        ibRotate = (ImageButton) findViewById(R.id.ibRotate);
        ibRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                angle = angle + 20;
                graphicView.invalidate();
            }
        });

        ibBright = (ImageButton) findViewById(R.id.ibBright);
        ibBright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = color + 0.2f;
                graphicView.invalidate();
            }
        });

        ibDark = (ImageButton) findViewById(R.id.ibDark);
        ibDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = color - 0.2f;
                graphicView.invalidate();
            }
        });

        ibGray = (ImageButton) findViewById(R.id.ibGray);
        ibGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satur == 0)
                    satur = 1;
                else
                    satur = 0;
                graphicView.invalidate();
            }
        });

        ibBlur = findViewById(R.id.ibBlur);
        ibBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBlur = !isBlur;
                isEmboss = false; // 동시에 둘 다 적용 안 되게
                graphicView.invalidate();
            }
        });

        ibEmboss = findViewById(R.id.ibEmboss);
        ibEmboss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEmboss = !isEmboss;
                isBlur = false;
                graphicView.invalidate();
            }
        });
    }
}