package com.example.week13_on02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Google 지도 객체를 제어할 변수
    GoogleMap gMap;

    // 레이아웃에 정의된 MapFragment 참조 변수
    MapFragment mapFrag;

    // 지도에 오버레이(이미지)를 추가할 옵션 객체
    GroundOverlayOptions videoMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 레이아웃 설정
        setTitle("구글 지도 활용"); // 액티비티 제목 설정

        // 레이아웃에 정의된 fragment(id: map)를 MapFragment로 캐스팅
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        // 비동기적으로 지도 객체 준비 완료되면 콜백 받도록 설정
        mapFrag.getMapAsync(this);
    }

    // 지도가 준비되었을 때 자동 호출되는 콜백 메서드
    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map; // 전달받은 지도 객체를 전역 변수에 저장

        // 지도 타입을 위성지도(Satellite)로 설정
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // 초기 카메라 위치를 서울 월드컵경기장 부근으로 설정 (위도, 경도, 줌레벨)
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(37.568256, 126.897240), 13));

        // 줌 컨트롤 버튼을 지도에 표시
        gMap.getUiSettings().setZoomControlsEnabled(true);

        // 사용자가 지도 아무 곳이나 클릭하면 실행되는 리스너 등록
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // 클릭한 지점에 GroundOverlay(이미지 오버레이)를 생성
                videoMark = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.presence_video_busy)) // 이미지 리소스
                        .position(point, 400f, 400f); // 클릭 위치 중심, 가로/세로 길이 400m

                // 지도에 오버레이 추가
                gMap.addGroundOverlay(videoMark);
            }
        });
    }

    // 옵션 메뉴 생성 (액션바에 메뉴 항목 추가)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "위성 지도");      // 메뉴 ID 1
        menu.add(0, 2, 0, "일반 지도");      // 메뉴 ID 2
        menu.add(0, 3, 0, "월드컵경기장 바로가기"); // 메뉴 ID 3
        return true;
    }

    // 옵션 메뉴 항목 선택 시 호출되는 메서드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: // "위성 지도" 선택 시
                gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // 위성 + 도로 지도
                return true;
            case 2: // "일반 지도" 선택 시
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // 일반 지도
                return true;
            case 3: // "월드컵경기장 바로가기" 선택 시
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(37.568256, 126.897240), 13)); // 해당 위치로 카메라 이동
                return true;
        }
        return false; // 위에 해당하지 않으면 false 반환
    }
}
