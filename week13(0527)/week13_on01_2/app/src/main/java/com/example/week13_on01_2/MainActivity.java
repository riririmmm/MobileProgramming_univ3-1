package com.example.week13_on01_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // UI 컴포넌트
    ListView listViewMP3;
    Button btnPlay, btnStop, btnPause;
    TextView tvMP3, tvTime;
    SeekBar pbMP3;

    // 변수
    ArrayList<String> mp3List;
    String selectedMP3;
    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/Ringtones/";
    MediaPlayer mPlayer;
    boolean isPaused = false;

    Thread timeThread;  // 진행 시간 업데이트용 스레드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("간단 MP3 플레이어");

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        // 1. 파일 리스트 불러오기
        mp3List = new ArrayList<>();
        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;
        assert listFiles != null;
        for (File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if (extName.equals("mp3"))
                mp3List.add(fileName);
        }

        // 2. 리스트뷰 설정
        listViewMP3 = findViewById(R.id.listViewMP3);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, mp3List);  // 라디오 버튼 있는 리스트 형식
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);     // 하나만 선택 가능
        listViewMP3.setAdapter(adapter);    // 어댑터 연결
        listViewMP3.setItemChecked(0, true);    // 앱이 시작됐을 때, 첫 번째 MP3 항목을 기본으로 선택된 상태로 표시
        selectedMP3 = mp3List.get(0);

        listViewMP3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMP3 = mp3List.get(position);    // 선택한 항목 이름 저장
            }
        });

        // 3. 컴포넌트 연결
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        btnPause = findViewById(R.id.btnPause);
        tvMP3 = findViewById(R.id.tvMP3);
        tvTime = findViewById(R.id.tvTime);
        pbMP3 = findViewById(R.id.pbMP3);

        btnStop.setClickable(false);
        btnPause.setClickable(false);

        // 4. ▶ 버튼 클릭: 음악 재생
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(mp3Path + selectedMP3);
                    mPlayer.prepare();
                    mPlayer.start();

                    btnPlay.setClickable(false);
                    btnStop.setClickable(true);
                    btnPause.setClickable(true);
                    tvMP3.setText("실행 중인 음악: " + selectedMP3);
                    btnPause.setText("일시정지");
                    pbMP3.setVisibility(View.VISIBLE);

                    // SeekBar 최대값 설정
                    pbMP3.setMax(mPlayer.getDuration());

                    // 진행 시간 및 SeekBar 업데이트 스레드 시작
                    timeThread = new Thread() {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                        public void run() {
                            while (mPlayer != null && mPlayer.isPlaying()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int curPos = mPlayer.getCurrentPosition();
                                        pbMP3.setProgress(curPos);
                                        tvTime.setText("진행 시간 : " + timeFormat.format(curPos));
                                    }
                                });
                                SystemClock.sleep(200);
                            }
                        }
                    };
                    timeThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // 5. ■ 버튼 클릭: 음악 정지
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.reset();
                    btnPlay.setClickable(true);
                    btnStop.setClickable(false);
                    btnPause.setClickable(false);
                    btnPause.setText("일시정지");
                    tvMP3.setText("실행 중인 음악: ");
                    pbMP3.setVisibility(View.INVISIBLE);
                    pbMP3.setProgress(0);
                    tvTime.setText("진행 시간");

                    // 스레드 종료
                    if (timeThread != null && timeThread.isAlive()) {
                        timeThread.interrupt();
                    }
                }
            }
        });

        // 6. ⏸️ 버튼 클릭: 일시정지 및 재개
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    btnPlay.setClickable(true);
                    btnStop.setClickable(true);
                    btnPause.setText("다시 듣기");
                    isPaused = true;
                } else if (mPlayer != null && isPaused) {
                    mPlayer.start();
                    btnPlay.setClickable(false);
                    btnStop.setClickable(true);
                    btnPause.setText("일시정지");
                    isPaused = false;

                    // 재개 시 스레드 다시 시작
                    timeThread = new Thread() {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                        public void run() {
                            while (mPlayer != null && mPlayer.isPlaying()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int curPos = mPlayer.getCurrentPosition();
                                        pbMP3.setProgress(curPos);
                                        tvTime.setText("진행 시간 : " + timeFormat.format(curPos));
                                    }
                                });
                                SystemClock.sleep(200);
                            }
                        }
                    };
                    timeThread.start();
                }
            }
        });

        // SeekBar를 사용자가 직접 움직였을 때 해당 위치부터 재생되도록 설정
        pbMP3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mPlayer != null) {
                    mPlayer.seekTo(progress);  // 사용자가 위치를 직접 조절하면 그 위치로 점프
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 SeekBar 터치 시작할 때
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 SeekBar 터치 끝냈을 때
            }
        });
    }
}
