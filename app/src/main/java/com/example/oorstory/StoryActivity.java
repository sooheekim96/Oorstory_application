package com.example.oorstory;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skt.Tmap.TMapTapi;
import java.util.HashMap;


public class StoryActivity extends AppCompatActivity {
    private String userLocation;
    private LinearLayout btn_comment;
    private ImageButton gamestart;
    String title, theme, time;
    int star_num;

    TMapTapi tMapTapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        btn_comment = findViewById(R.id.btn_comment);
        // intent 값 얻어오기
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        theme = intent.getStringExtra("theme");
        time = intent.getStringExtra("time");
        star_num = Integer.parseInt(intent.getStringExtra("star_num"));
        Log.e("받아온 정보", title+" "+theme+" "+time);

        // 데이터 넣기
        TextView title_story_detail = (TextView)findViewById(R.id.title_story_detail);
        TextView theme_detail = (TextView)findViewById(R.id.theme_detail);
        TextView time_detail = (TextView)findViewById(R.id.time_detail);
        title_story_detail.setText(title);
        theme_detail.setText(theme);
        time_detail.setText(time);

        // 별점 기록하기
        ImageView[] stars_list = {
                findViewById(R.id.diff1_onStory), findViewById(R.id.diff2_onStory), findViewById(R.id.diff3_onStory),
                findViewById(R.id.diff4_onStory), findViewById(R.id.diff5_onStory)};
        for (int i=0;i<star_num;i++){
            stars_list[i].setImageResource(R.drawable.starred);
        }

        // mapActivity로 돌아가기
        ImageButton back_btn = findViewById(R.id.back_btn_toMap);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(StoryActivity.this, MapActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("theme", theme);
                intent.putExtra("time", time);
                intent.putExtra("star_num", star_num+""); // 별 개수
                finish();
                startActivity(intent);
            }
        });
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoryActivity.this, CommentActivity.class);
                intent.putExtra("title", title);
                finish();
                startActivity(intent);
            }
        });


        // 게임 시작하기 및 타이머 시작 + Tmap
        gamestart = (ImageButton)findViewById(R.id.gameStart);
        tMapTapi = new TMapTapi(this);

        configureApp();
        setOnAuthentication();

        gamestart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                checkSDKVersion();
                //다른 앱 위에 그리기 허용 확인
                if(checkSDKVersion()) {

                    //Start TimeNotiService:
                    Intent serviceIntent = new Intent(StoryActivity.this, StopWatchService.class);
                    serviceIntent.putExtra("title", title);
                    ServiceStart(serviceIntent);

                    //TMAP 미설치 시 설치 안내
                    boolean istMapApp = tMapTapi.isTmapApplicationInstalled();
                    if (istMapApp == false) {
                        Log.e("test", "Tmap uninstalled");
                        tMapInstall();
                        invokeRoute();
                    }else{
                        invokeRoute();
                        gamestart.setEnabled(false);
                    }

                }
            }
        });

       //게임 끝내기, 버튼 재활성화
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("activateButton");

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                gamestart.setEnabled(true);
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);


    }

    // 카브뷰 펼치기/접기 이벤트
    public void unfold_cardview(View view){
        TextView textView;
        switch (view.getId()){
            case R.id.show_game_tv :
                textView = findViewById(R.id.explan_game_tv);
                Log.e("클릭!!게임",(textView.getVisibility()+""));
                break;
            case R.id.show_story_tv :
                textView = findViewById(R.id.explan_story_tv);
                Log.e("클릭!!스토리",(textView.getVisibility()+""));
                break;
            case R.id.show_place_tv :
                textView = findViewById(R.id.explan_place_tv);
                Log.e("클릭!!목표장소",(textView.getVisibility()+""));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        if ((textView.getVisibility()+"").contains("8")) textView.setVisibility(View.VISIBLE);
        else textView.setVisibility(View.GONE);
    }

    // 외부 지도 어플로 연결, 목적지 정보 전달하기
    public void mapIcon_onClick(View view){
        Log.e("맵 아이콘 클릭", "맵 아이콘 클릭");
    }

    // API 설정
    private void configureApp() {
        tMapTapi.setSKTMapAuthentication(getString(R.string.map_api));
    }

    private void setOnAuthentication(){
        tMapTapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                Log.d("test", "성공");
            }

            @Override
            public void SKTMapApikeyFailed(String errorMsg) {
                Log.d("test", "실패");
            }
        });
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

    public boolean checkSDKVersion(){
        Log.e("msg", "들어옴");
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(StoryActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                Log.e("msg", "허용안됨");
                startActivityForResult(intent, 1);
                return false;
            }
            else{ return true; }
        }else{ return true; }
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
    }

}