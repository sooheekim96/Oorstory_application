package com.example.oorstory;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;

public class stopWatchFloatingView extends Service  {

    private WindowManager windowManager;
    private View floatingView;
    private String title;

    public stopWatchFloatingView() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null){
            return Service.START_STICKY;
        }else {
            //StoryActivity에서 title 정보 가져오기
            title = intent.getStringExtra("title");

            floatingView = LayoutInflater.from(this).inflate(R.layout.activity_stop_watch_floating_view, null);

            int LAYOUT_FLAG;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else{
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
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
            params.gravity = Gravity.CENTER | Gravity.CENTER ;

            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.addView(floatingView, params);

            //set Text
            TextView storyTitle = (TextView)floatingView.findViewById(R.id.storyTitle);
            storyTitle.setText(title);

            //make view movable
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

            //remove the view and stop this service when the view clicked
            floatingView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    windowManager.removeView(floatingView);
                    stopSelf();
                }
            });

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

}