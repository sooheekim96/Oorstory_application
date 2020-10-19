package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.oorstory.TimeNotiService.LocalBinder;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapGpsManager.onLocationChangedCallback;

import java.util.HashMap;
import java.util.Locale;

public class  StopWatchActivity extends Activity implements onLocationChangedCallback{

    TMapGpsManager gps;
    PermissionManager permissionManager = null; // 권한요청 관리자
    boolean tracking_mode = false;
    TMapPoint curLocation;

    @Override
    public void onLocationChange(Location location){
        LogManager.printLog("onLocationChange :::> " + location.getLatitude() +  " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());
    }

    private TextView timeView;
    private int seconds;
    private int pre_time = 0;
    private String time;
    private TextView storyTitle;
    private String title;
    private ImageButton start_btn;
    private ImageButton pause_btn;

    private boolean isStart = false;
    private Button startStop;
    boolean mBound = false;
    TMapTapi tMapTapi;

    TimeNotiService mService;
    Intent serviceIntent;
    ImageButton back_btn;

    @Override
    protected  void onStart() {
        super.onStart();

        //Start TimeNotiService:
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStart) {
                    //위치추적 시작
                    tracking_mode = true;

                    //서비스 시작
                    ServiceStart();

                    //TMAP 미설치 시 설치 안내
                    ChecktmapInstalled();

                    invokeRoute();
                    isStart = true;
                    startStop.setText(R.string.arrived);
                    pause_btn.setVisibility(View.VISIBLE);
                }
                else{
                    Log.e("arrival_btn", "stopService");
                    startStop.setText(R.string.navistart);
                    isStart = false;
                    //stopService foloowed by unbindService
                    stopService(serviceIntent);
                    unbindService(connection);


                    tracking_mode = false;
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isStart){
                    moveTaskToBack(true);
                } else{
                    final Intent intentLocal = new Intent();
                    intentLocal.setAction("activateButton");
                    sendBroadcast(intentLocal);finish();
                }
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStart){
                    stopService(serviceIntent);
                    unbindService(connection);
                    isStart = false;
                    pre_time = seconds;
                    start_btn.setVisibility(View.VISIBLE);
                    pause_btn.setVisibility(View.GONE);
                }
            }
        });
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStart){
                   ServiceStart();
                   isStart = true;
                   pause_btn.setVisibility(View.VISIBLE);
                   start_btn.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        gps = new TMapGpsManager(StopWatchActivity.this);
        gps.setLocationCallback(); // 현재 위치상태 변경 시 호출되는 콜백 인터페이스를 설정하고 그 성공여부를 반환
        permissionManager = new PermissionManager(this); // 권한요청 관리자

        storyTitle = findViewById(R.id.storyTitle);
        startStop = findViewById(R.id.startStop);
        timeView = findViewById(R.id.timeView);
        tMapTapi = new TMapTapi(this);
        back_btn= findViewById(R.id.back_btn_toStory);
        start_btn = findViewById(R.id.start);
        pause_btn = findViewById(R.id.pause);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        storyTitle.setText(title);

        //위치확인 권한
        PermissionRequest();
        //서비스 결과 받는 Receiver 등록
        RegisterReceiver();
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    private void ServiceStart(){
        serviceIntent = new Intent(StopWatchActivity.this, TimeNotiService.class);
        serviceIntent.putExtra("title", title);

        //서비스 시작하기 (SDK에 따라 호출함수 달라짐)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (serviceIntent !=null) {
                Log.e("startForegroundService", "start");
                startForegroundService(serviceIntent);
            }
        }else {
            Log.e("startService", "start");
            startService(serviceIntent);
        }
        bindService(serviceIntent, connection, BIND_AUTO_CREATE );
    }

    private void ChecktmapInstalled (){
        boolean istMapApp = tMapTapi.isTmapApplicationInstalled();
        if (!istMapApp) {
            Log.e("test", "Tmap uninstalled");
            tMapInstall();
        }
    }

    private void PermissionRequest(){
        permissionManager.request(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
            @Override
            public void granted() {
                gps = new TMapGpsManager(StopWatchActivity.this);
                gps.setMinTime(1000);
                gps.setMinDistance(5);
                gps.setProvider(gps.GPS_PROVIDER);
                gps.OpenGps();
                gps.setProvider(gps.NETWORK_PROVIDER);
                gps.OpenGps();

            }

            @Override
            public void denied() {
                Log.w("LOG", "위치정보 접근 권한이 필요합니다.");
            }
        });
        curLocation = gps.getLocation();
    }

    //권한 요청(permissionRequest)의 결과를 받아오는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionManager.setResponse(requestCode, grantResults); // 권한요청 관리자에게 결과 전달
    }

    private void RegisterReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("StopWatch");
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                seconds = pre_time + intent.getIntExtra("timer", 0);
                Log.e("seconds", Integer.toString(seconds));
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                time = String.format(Locale.getDefault(), "%d:%02d:%02d",
                        hours, minutes, secs);
                timeView.setText(time);
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void tMapInstall() {
        new Thread() {
            @Override
            public void run() {
                Uri uri = Uri.parse(tMapTapi.getTMapDownUrl().get(0));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        }.start();
    }

    //이륜차 주행 길안내
    public void invokeRoute(){
        HashMap pathInfo = new HashMap();

        //목적지 정보
        pathInfo.put("rGoName", "T타워");
        pathInfo.put("rGoX", "126.985302");
        pathInfo.put("rGoY", "37.570841");

        //출발지 정보 - 현재 위치
        String log = Double.toString(curLocation.getLongitude());
        String lat = Double.toString(curLocation.getLatitude());

        pathInfo.put("rStName", "출발지");
        pathInfo.put("rStX", log);
        pathInfo.put("rStY", lat);

        //주행옵션 - 이륜차(자전거)
        pathInfo.put("rSOpt", "6");

        tMapTapi.invokeRoute(pathInfo);
    }


}