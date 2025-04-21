package com.example.week06_tabhost;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecImg1 = tabHost.newTabSpec("img1").setIndicator("뱁새");
        tabSpecImg1.setContent(R.id.imgView1);
        tabHost.addTab(tabSpecImg1);

        TabHost.TabSpec tabSpecImg2 = tabHost.newTabSpec("img2").setIndicator("팬더");
        tabSpecImg2.setContent(R.id.imgView2);
        tabHost.addTab(tabSpecImg2);

        TabHost.TabSpec tabSpecImg3 = tabHost.newTabSpec("img3").setIndicator("고양이");
        tabSpecImg3.setContent(R.id.imgView3);
        tabHost.addTab(tabSpecImg3);

        TabHost.TabSpec tabSpecImg4 = tabHost.newTabSpec("img4").setIndicator("너구리");
        tabSpecImg4.setContent(R.id.imgView4);
        tabHost.addTab(tabSpecImg4);

        tabHost.setCurrentTab(0);
    }
}