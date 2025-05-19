package com.example.week11_on02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("갤러리 영화 포스터");
        getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Gallery gallery = (Gallery) findViewById(R.id.gallery1);
        MyGalleryAdapter galAdapter = new MyGalleryAdapter(this);
        gallery.setAdapter(galAdapter);
    }

    public class MyGalleryAdapter extends BaseAdapter {
        Context context;
        Integer[] posterID = { R.drawable.mov01, R.drawable.mov02, R.drawable.mov03, R.drawable.mov04, R.drawable.mov05,
                R.drawable.mov06, R.drawable.mov07, R.drawable.mov08, R.drawable.mov09, R.drawable.mov10,
                R.drawable.mov11, R.drawable.mov12, R.drawable.mov13, R.drawable.mov14, R.drawable.mov15,
                R.drawable.mov16, R.drawable.mov17, R.drawable.mov18, R.drawable.mov19, R.drawable.mov20,
                R.drawable.mov21, R.drawable.mov22, R.drawable.mov23, R.drawable.mov24, R.drawable.mov25
        };

        public MyGalleryAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return posterID.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new Gallery.LayoutParams(200, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5,5,5,5);

            imageView.setImageResource(posterID[position]);

            final int pos = position;

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ImageView ivPoster = (ImageView) findViewById(R.id.ivPoster);
                    ivPoster.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ivPoster.setImageResource(posterID[position]);
                    return false;
                }
            });

            return imageView;
        }
    }
}