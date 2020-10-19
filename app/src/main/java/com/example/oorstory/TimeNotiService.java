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
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static com.example.oorstory.Notification.CHANNEL_1_ID;

public class TimeNotiService extends Service {

    //floatingview
    private WindowManager windowManager;
    private View floatingView;
    private String title;

    //runtimer
    private int seconds = 0;
    private boolean running = true;
    private String time;

    final Timer timer = new Timer();

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        TimeNotiService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TimeNotiService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("bind", "bind");
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        UpdateNotification("게임을 시작합니다.");
        //setView();
    }

    @Override
    public void onDestroy() {
        Log.e("onDestroy", "destory");

        final Intent intentLocal = new Intent();
        intentLocal.setAction("activateButton");
        sendBroadcast(intentLocal);

        timer.cancel();
        /*handler.removeCallbacks(rt);
        handler.removeCallbacksAndMessages(null);*/
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null){
            return Service.START_STICKY;
        }else {
            title  = intent.getStringExtra("title");

            timer.scheduleAtFixedRate(new TimerTask() {

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
                    Intent intentLocal = new Intent();
                    intentLocal.setAction("StopWatch");
                    intentLocal.putExtra("timer", seconds);
                    sendBroadcast(intentLocal);
                    UpdateNotification(time);
                }
            }, 0, 1000);


            return Service.START_NOT_STICKY;
        }
    }


    public void UpdateNotification(String time){

        Intent notificationIntent = new Intent(this, StopWatchActivity.class);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("time", time);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this, CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText("Game Started " + time)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
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

    /*public void BackToApp(){
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
    }*/
}
