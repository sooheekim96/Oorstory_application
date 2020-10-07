package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.oorstory.TimeNotiService.LocalBinder;
import com.skt.Tmap.TMapTapi;

import java.util.HashMap;

public class  StopWatchActivity extends AppCompatActivity {

    private TextView timeView;
    private String time;
    private TextView storyTitle;
    private String title;

    private Button naviStart;
    private boolean isStart = false;
    private Button startStop;
    boolean mBound = false;
    TMapTapi tMapTapi;

    TimeNotiService mService;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        storyTitle = findViewById(R.id.storyTitle);
        startStop = findViewById(R.id.arrival_btn);
        timeView = findViewById(R.id.timeView);
        tMapTapi = new TMapTapi(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        storyTitle.setText(title);

        //서비스 결과 받는 Receiver 등록
        RegisterReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("StopWatch");
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                time = intent.getStringExtra("time");
                timeView.setText(time);
            }
        };



        //Start TimeNotiService:
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStart) {
                    //서비스 시작
                    serviceIntent = new Intent(StopWatchActivity.this, TimeNotiService.class);
                    serviceIntent.putExtra("title", title);
                    ServiceStart(serviceIntent);

                    //TMAP 미설치 시 설치 안내
                    boolean istMapApp = tMapTapi.isTmapApplicationInstalled();
                    if (istMapApp == false) {
                        Log.e("test", "Tmap uninstalled");
                        tMapInstall();
                        invokeRoute();
                    } else {
                        invokeRoute();
                        naviStart.setEnabled(false);
                    }
                    isStart = true;
                    naviStart.setText(R.string.arrived);
                }
                else{
                    Log.e("arrival_btn", "stopService");
                    naviStart.setText(R.string.navistart);
                    isStart = false;
                    unbindService(connection);
                    stopService(serviceIntent);
                }

            }
        });

    }

    private void ServiceStart(Intent serviceIntent){
        //서비스 시작하기 (SDK에 따라 호출함수 달라짐)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (serviceIntent!=null) {
                Log.e("startForegroundService", "start");
                startForegroundService(serviceIntent);
            }
        } else {
            Log.e("startService", "start");
            startService(serviceIntent);
        }
        bindService(serviceIntent, connection, BIND_AUTO_CREATE );
    }

    private void RegisterReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("StopWatch");
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                time = intent.getStringExtra("time");
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
        pathInfo.put("rGoName", "T타워");
        pathInfo.put("rGoX", "126.985302");
        pathInfo.put("rGoY", "37.570841");

        pathInfo.put("rStName", "출발지");
        pathInfo.put("rStX", "126.926252");
        pathInfo.put("rStY", "37.557607");

        pathInfo.put("rV1Name", "경유지");
        pathInfo.put("rV1X", "126.976867");
        pathInfo.put("rV1Y", "37.576016");
        pathInfo.put("rSOpt", "6");
        tMapTapi.invokeRoute(pathInfo);
    }


}