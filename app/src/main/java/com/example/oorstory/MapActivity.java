package com.example.oorstory;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String story_title;
    private HashMap<String, Double[]> locations;
    private FusedLocationProviderClient fusedLocationClient;
    String title, theme, time;
    int star_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // intent 값 얻어오기
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        theme = intent.getStringExtra("theme");
        time = intent.getStringExtra("time");
        star_num = Integer.parseInt(intent.getStringExtra("star_num"));
        Log.e("받아온 정보", title+" "+theme+" "+time+" "+star_num);

        // 데이터 넣기
        TextView title_story_map = (TextView)findViewById(R.id.title_story_map);
        TextView theme_map = (TextView)findViewById(R.id.theme_map);
        TextView time_map = (TextView)findViewById(R.id.time_map);
        title_story_map.setText(title);
        theme_map.setText(theme);
        time_map.setText(time);

        // 별점 기록하기
        ImageView[] stars_list = {
                findViewById(R.id.diff1_onMap), findViewById(R.id.diff2_onMap), findViewById(R.id.diff3_onMap),
                findViewById(R.id.diff4_onMap), findViewById(R.id.diff5_onMap)};
        for (int i=0;i<star_num;i++){
            stars_list[i].setImageResource(R.drawable.starred);
        }

        // mainActivity로 돌아가기
        ImageButton back_btn = findViewById(R.id.back_btn_toMain);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        locations = new HashMap<String, Double[]>();

        //locations에 마커를 표시할 위치 정보 가져와 저장하기
        locations.put("서울역", new Double[]{37.553470, 126.969748}); //서울역
        locations.put("홍대입구역", new Double[]{37.558130, 126.925655}); //홍대입구역
        locations.put("회현역", new Double[]{37.558510, 126.978082}); //회현역

        // 모든 마커의 중심을 저장, x:위도, y:경도
        Double x = 0.0, y = 0.0;

        LatLng latLng;
        for (String region: locations.keySet()){
            Double[] loc = locations.get(region); //위도, 경도 저장
            latLng = new LatLng(loc[0], loc[1]);

            // 마커 표시하기
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng)
                    .title(region)
                    .snippet(findAddress(loc[0], loc[1]));
            googleMap.addMarker(markerOptions);

            // 중심구하기
            x = x+loc[0];
            y = y+loc[1];
        }

        // 지도 중심 및 확대 비율 지정
        latLng = new LatLng(x/locations.size(), y/locations.size());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); // 설정한 위도 경도를 중심으로 지도를 띄워줌
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }

    // 위도, 경도 값으로 주소 얻어오기
    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress);
                }
            }

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "주소취득 실패", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return bf.toString();
    }

    // stroyActivity로 연결
    public void titleMap_Onclick(View view){
        Intent intent = new Intent(MapActivity.this, StoryActivity.class);
        intent.putExtra("title", title.toString());
        intent.putExtra("theme", theme.toString());
        intent.putExtra("time", time.toString());
        intent.putExtra("star_num", star_num+""); // 별 개수
        startActivity(intent);
    }
}