package com.example.oorstory;

/*
게임 시작시간을 사용자가 선택할 수 있다
티맵 설치 안내 및 실행
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static com.example.oorstory.Notification.CHANNEL_1_ID;

public class TimeNotiService extends Service {

    //floatingview
    private WindowManager windowManager;
    private View floatingView;
    private String title;
    private TextView textTime;

    //runtimer
    private int seconds = 0;
    private boolean running = true;
    private String time;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        UpdateNotification("게임을 시작합니다.");
        setView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null){
            return Service.START_STICKY;
        }else {
            title  = intent.getStringExtra("title");
            ManageView();
            /*
            도착 버튼 클릭 시, 서비스 종료 및 기존 앱으로 돌아가기
             */
            /*floatingView.findViewById(R.id.notiArrived).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    BackToApp();
                }
            });*/

            //시작버튼을 누르면 스톱워치가 시작함
            floatingView.findViewById(R.id.notiStart).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    runTimer();
                    windowManager.removeView(floatingView);
                }
            });
            return Service.START_NOT_STICKY;
        }
    }

    //메인 스레드
    final Handler handler = new Handler();
    //워킹 스레드
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;

            time = String.format(Locale.getDefault(), "%d:%02d:%02d",
                    hours, minutes, secs);


            if (running) {
                seconds++;
            }
            Log.e("seconds", time);
            UpdateNotification("게임을 시작합니다.");

            // Post the code again
            // with a delay of 1 second.
            handler.postDelayed(this, 1000);

        }

    };

    private void runTimer()
    {
        handler.post(runnable);
    }
    public void UpdateNotification(String time){

        Intent notificationIntent = new Intent(this, StoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText("Time elapsed : " + time)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(5, notification);
    }

    public void setView() {
        floatingView = LayoutInflater.from(this).inflate(R.layout.activity_gamestart_floating_view, null);
        floatingView.setBackgroundColor(Color.TRANSPARENT);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT,
                LAYOUT_FLAG,
                FLAG_NOT_FOCUSABLE,
                PixelFormat.OPAQUE
        );

        params.x = 0;
        params.y = 0;
        params.gravity = Gravity.CENTER | Gravity.CENTER;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);

        //움직일 수 있는 뷰
        floatingView.setOnTouchListener(new View.OnTouchListener(){

            private int x, y;
            private float touchedX, touchedY;
            private WindowManager.LayoutParams updatedParams = params;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParams.x;
                        y = updatedParams.y;

                        touchedX = event.getRawX();
                        touchedY = event.getRawY();

                    case MotionEvent.ACTION_MOVE:

                        updatedParams.x = (int) (x + event.getRawX() - touchedX);
                        updatedParams.y = (int) (x + event.getRawY() - touchedY);

                        windowManager.updateViewLayout(floatingView, updatedParams);

                    default :
                        return true;
                }
            }


        });
    }

    public void ManageView(){

        //set Text
        TextView storyTitle = (TextView)floatingView.findViewById(R.id.storyTitle);
        storyTitle.setText(title);

        //remove the view by cancel button
        floatingView.findViewById(R.id.notiStart).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                windowManager.removeView(floatingView);
            }
        });
    }

    public void BackToApp(){
        //게임 시작 버튼 활성화
        final Intent intentLocal = new Intent();
        intentLocal.setAction("activateButton");
        sendBroadcast(intentLocal);

        //Tmap 실행 후 기본앱으로 돌아오기 -- tmap은 종료 API x => 강제종료?밖에 답이 없나?
        Intent dialogIntent  = new Intent(getApplicationContext(), LocationCheckingActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);

        // 창닫기
        windowManager.removeView(floatingView);

        stopForeground(true);
        stopSelf();
        handler.removeCallbacks(runnable);
    }
}
