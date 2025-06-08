package com.example.week13_on01_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // UI 요소 선언
    ListView listViewMP3;          // MP3 파일 목록을 보여줄 리스트뷰
    Button btnPlay, btnStop;       // 재생, 정지 버튼
    TextView tvMP3;                // 현재 재생 중인 음악 파일명을 보여줄 텍스트뷰
    ProgressBar pbMP3;             // 음악 재생 중 표시할 프로그레스바

    ArrayList<String> mp3List;     // MP3 파일 이름 목록 저장용 리스트
    String selectedMP3;            // 현재 선택된 MP3 파일 이름

    boolean isPaused = false;       // 일시정지 상태 플래그
    Button btnPause;                // 일시정지 버튼

    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/Ringtones/"; // MP3 파일들이 저장된 경로
    MediaPlayer mPlayer;          // 실제 음악 재생을 담당할 MediaPlayer 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 레이아웃 설정
        setTitle("간단 MP3 플레이어");            // 액티비티 제목 설정

        // 외부 저장소 권한 요청 (Android 6.0 이상)
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        mp3List = new ArrayList<>();    // MP3 리스트 초기화

        // mp3Path 경로에 있는 파일들을 가져와 확장자가 .mp3인 파일만 리스트에 추가
        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;
        assert listFiles != null;
        for (File file : listFiles) {
            fileName = file.getName(); // 파일명
            extName = fileName.substring(fileName.length() - 3);    // 확장자 추출(mp3)
            if (extName.equals((String) "mp3"))
                mp3List.add(fileName);  // .mp3 파일만 리스트에 추가
        }

        // 리스트뷰에 MP3 목록 연결
        listViewMP3 = (ListView) findViewById(R.id.listViewMP3);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, mp3List);  // 라디오 버튼 있는 리스트 형식
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 하나만 선택 가능
        listViewMP3.setAdapter(adapter);    // 어댑터 연결
        listViewMP3.setItemChecked(0, true);    // 첫 번째 항목 선택

        // 리스트 항목 클릭 시 선택된 MP3 파일 이름 저장
        listViewMP3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMP3 = mp3List.get(position);    // 선택한 항목 이름 저장
            }
        });
        selectedMP3 = mp3List.get(0);   // 초기 선택값 설정

        // UI 컴포넌트 연결
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        tvMP3 = (TextView) findViewById(R.id.tvMP3);
        pbMP3 = (ProgressBar) findViewById(R.id.pbMP3);

        // ▶ 버튼 클릭 시 음악 재생
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mPlayer = new MediaPlayer();    // MediaPlayer 객체 생성
                    mPlayer.setDataSource(mp3Path + selectedMP3);   // 파일 경로 설정
                    mPlayer.prepare();  // 준비 (버퍼링 등)
                    mPlayer.start();    // 재생 시작
                    btnPlay.setClickable(false);    // 듣기 버튼 비활성화
                    btnStop.setClickable(true);     // 중지 버튼 활성화
                    btnPause.setClickable(true);    // 일시정지 버튼 활성화
                    tvMP3.setText("실행 중인 음악: " + selectedMP3);  // 제목 표시
                    btnPause.setText("일시정지");
                    pbMP3.setVisibility(View.VISIBLE);  // 프로그레스바 표시
                } catch (IOException e) {
                    // 예외 발생 시 무시 (파일 못 찾는 경우 등)
                }
            }
        });

        // ■ 버튼 클릭 시 음악 정지
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();     // 재생 중단
                mPlayer.reset();    // MediaPlayer 상태 초기화
                btnPlay.setClickable(true);     // 듣기 버튼 활성화
                btnStop.setClickable(false);    // 중지 버튼 비활성화
                btnPause.setClickable(false);   // 일시정지 버튼 비활성화
                tvMP3.setText("실행 중인 음악: ");    // 제목 초기화
                pbMP3.setVisibility(View.INVISIBLE);    // 프로그레스바 숨김
            }
        });

        btnStop.setClickable(false); // 처음에는 정지 버튼 비활성화

        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setClickable(false);   // 처음에는 일시정지 버튼 비활성화

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    btnPlay.setClickable(true);
                    btnStop.setClickable(false);
                    pbMP3.setVisibility(View.INVISIBLE);
                    btnPause.setText("다시 듣기");
                    isPaused = true;
                } else if (mPlayer != null && isPaused) {
                    mPlayer.start();
                    btnPlay.setClickable(false);
                    btnStop.setClickable(true);
                    pbMP3.setVisibility(View.VISIBLE);
                    btnPause.setText("일시정지");
                    isPaused = false;
                }
            }
        });

    }
}