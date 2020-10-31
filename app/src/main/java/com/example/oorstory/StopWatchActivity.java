package com.example.oorstory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.oorstory.TimeNotiService.LocalBinder;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapGpsManager.onLocationChangedCallback;
//import com.unity3d.player.UnityPlayerActivity;

import java.util.HashMap;
import java.util.Locale;

public class  StopWatchActivity extends Activity implements onLocationChangedCallback{

    TMapGpsManager gps;
    PermissionManager permissionManager = null; // 권한요청 관리자
    AlertDialog.Builder builder; // 종료 경고 알람창
    TMapPoint curLocation;
    final BroadcastReceiver broadcastReceiver = new broadCastReceiver();

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
    private ImageButton stop_btn;

    private boolean isStart = false;
    private boolean isNaviStart = false;
    private Button ifarrived;
    boolean mBound = false;
    TMapTapi tMapTapi;

    TimeNotiService mService;
    Intent serviceIntent;
    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "created");
        setContentView(R.layout.activity_stop_watch);

        gps = new TMapGpsManager(StopWatchActivity.this);
        gps.setLocationCallback(); // 현재 위치상태 변경 시 호출되는 콜백 인터페이스를 설정하고 그 성공여부를 반환
        permissionManager = new PermissionManager(this); // 권한요청 관리자

        storyTitle = findViewById(R.id.storyTitle);
        ifarrived = findViewById(R.id.startStop);
        timeView = findViewById(R.id.timeView);
        tMapTapi = new TMapTapi(this);
        back_btn= findViewById(R.id.back_btn_toStory);
        //start_btn = findViewById(R.id.start);
        pause_btn = findViewById(R.id.pause);
        stop_btn = findViewById(R.id.stop);

        pause_btn.setEnabled(false);
        stop_btn.setEnabled(false);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        storyTitle.setText(title);

        //위치확인 권한
        PermissionRequest();
        //서비스 결과 -- Time을 받는 Receiver 등록
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("StopWatch");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected  void onStart() {
        super.onStart();

        //종료시 경고창 setting
        setAlertDialog();


        //Start TimeNotiService:
        ifarrived.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TMAP 미설치 시 설치 안내
                ChecktmapInstalled();
                Log.e("isNaviStart", String.valueOf(isNaviStart));

                if (!isNaviStart) {
                    //타이머 시작

                    ServiceStart();

                    //네비게이션 시작
                    invokeRoute();

                } else {//stop

                    //기록 초기화
                    pre_time = 0;
                    //안내 종료 --
                    isNaviStart = false;
                    unregisterReceiver(broadcastReceiver);

                    closeGame();

                    //위치 확인
                    if (ifinThisArea()) {
                        Toast.makeText(StopWatchActivity.this, "목적지 도착!", Toast.LENGTH_SHORT);

                        ServiceStop();
                        /*
                        loading
                         */

                        /*
                         AR game 실행
                         ...
                        */
                        /*Intent unityIntent = new Intent(StopWatchActivity.this, UnityPlayerActivity.class);
                        startActivity(unityIntent);*/

                    } else {
                        Toast.makeText(StopWatchActivity.this, "목적지 주변이 아닙니다", Toast.LENGTH_SHORT);

                        //실험용
                        //확인 절차를 위해 넣었으니 이 부분을 삭제 바람
                        /*Intent unityIntent = new Intent(StopWatchActivity.this, UnityPlayerActivity.class);
                        startActivity(unityIntent);*/

                    }
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceStop();

                if (!isNaviStart) {
                    closeGame();

                }else {

                    //이전 기록 임시 저장
                    pre_time = seconds;
                    Log.d("pretime", Integer.toString(pre_time));

                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    ServiceStop();

                    //이전 기록 임시 저장
                    pre_time = seconds;
                    Log.d("pretime", Integer.toString(pre_time));

                }
                else{
                    pause_btn.setBackgroundResource(R.drawable.ic_start);
                    ServiceStart();

                }
            }
        });

        /*.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNaviStart) {
                    ServiceStart();

                }
            }
        });*/

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {

                    //스탑워치 초기화
                    pre_time = 0;
                    timeView.setText("0:00:00");

                    ServiceStop();
                }
            }
        });
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        Log.e("onDestory", "destroy");
        unbindService(connection);
    }


    private void closeGame(){
        //game 종료 후 game start 버튼 활성화
        final Intent intentLocal = new Intent();
        intentLocal.setAction("activateButton");
        sendBroadcast(intentLocal);

        finish();
    }

    private boolean ifinThisArea(){
        float[] result = new float[1];

        Location.distanceBetween( gps.getLocation().getLatitude(), gps.getLocation().getLongitude(),
                (Double)37.570841,(Double)126.985302, result);
        Log.d("location", Double.toString(gps.getLocation().getLatitude()) +" "+
                Double.toString(gps.getLocation().getLongitude()));
        Log.d("distanceBetween", Float.toString(result[0]));

        if (result[0]<50){
            return true;
        }

        return false;
    }

    private void setAlertDialog(){
        builder = new AlertDialog.Builder(StopWatchActivity.this);
        builder.setMessage(R.string.close_messg)
                .setTitle(R.string.close_title);

        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //resume
                ServiceStart();

            }
        });
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                unregisterReceiver(broadcastReceiver);

                //activate gamestart button
                final Intent intentLocal = new Intent();
                intentLocal.setAction("activateButton");
                sendBroadcast(intentLocal);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed(){
        ServiceStop();

        if (!isNaviStart) {
            closeGame();
            unregisterReceiver(broadcastReceiver);
        }else{

            //이전 기록 임시 저장
            pre_time = seconds;
            Log.d("pretime", Integer.toString(pre_time));

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void ServiceStart(){
        serviceIntent = new Intent(StopWatchActivity.this, TimeNotiService.class);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("pretime", pre_time);

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

        isStart = true;

        //화면 전환
        ifarrived.setText(R.string.arrived);
        //pause_btn.setVisibility(View.VISIBLE);
        pause_btn.setBackgroundResource(R.drawable.ic_pause);
        pause_btn.setEnabled(true);

        stop_btn.setEnabled(true);
        //pause_btn.setBackgroundColor(ContextCompat.getColor(this,
        //        R.color.colorBlack));
        //pause_btn.(ContextCompat.getColor(this,
         //       R.color.colorBlack));

        bindService(serviceIntent, connection, BIND_AUTO_CREATE );
    }

    private void ServiceStop(){
        if(isStart){ // service Intent로 할 수도 있을 것 같다.
            stopService(serviceIntent);
            unbindService(connection);

            pause_btn.setBackgroundResource(R.drawable.ic_start);
            //start_btn.setVisibility(View.VISIBLE);
            //pause_btn.setVisibility(View.GONE);
            //stop_btn.setVisibility(View.GONE);

            isStart = false;
        }

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

    public class broadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            seconds = intent.getIntExtra("seconds", 0);
            //Log.e("service seconds", Integer.toString(seconds) );
            time = intent.getStringExtra("timer");

            timeView.setText(time);
        }
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

        isNaviStart = true;
        Log.e("isNavi", "start");
        tMapTapi.invokeRoute(pathInfo);

    }


}