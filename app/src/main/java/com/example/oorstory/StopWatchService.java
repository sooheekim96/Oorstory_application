package com.example.oorstory;

import android.app.AutomaticZenRule;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static com.example.oorstory.Notification.CHANNEL_1_ID;
import static com.example.oorstory.Notification.CHANNEL_2_ID;

public class StopWatchService extends Service  {

    private WindowManager windowManager;
    private View floatingView;
    private String title;
    private TextView textTime;
    private ImageButton gamestart;

    /*StopWatch watch = new StopWatch();
    final int REFRESH_RATE = 100;*/

    private int seconds = 0;
    private boolean running = true;
    private String time;
    private boolean wasRunning;

    public StopWatchService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        if(intent == null){
            return Service.START_STICKY;
        }else {

            title = intent.getStringExtra("title");
            runTimer();

            //View 만들기
            floatingView = LayoutInflater.from(this).inflate(R.layout.activity_stop_watch_floating_view, null);
            floatingView.setBackgroundColor(Color.TRANSPARENT);

            //check layout flag
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

            //remove the view
            floatingView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    windowManager.removeView(floatingView);
                    //stopSelf();
                }
            });

            floatingView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    windowManager.removeView(floatingView);
                    //stopSelf();
                }
            });

            floatingView.findViewById(R.id.notiArrived).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    windowManager.removeView(floatingView);

                    final Intent intentLocal = new Intent();
                    intentLocal.setAction("activateButton");
                    sendBroadcast(intentLocal);

                    stopSelf();
                }
            });




        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    private void runTimer()
    {
        /*final Intent intentLocal = new Intent();
        intentLocal.setAction("StopWatch");*/

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                time = String.format(Locale.getDefault(), "%d:%02d:%02d",
                                            hours, minutes, secs);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                NotificationUpdate(time);
                //display time
                textTime = (TextView)floatingView.findViewById(R.id.textViewTime) ;
                textTime.setText(time);

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);

               /* intentLocal.putExtra("elapsedTime", time);
                sendBroadcast(intentLocal);*/

            }

        });
    }

    public void NotificationUpdate(String time){

        Intent notificationIntent = new Intent(this, StoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_1_ID )
                .setContentTitle(title)
                .setContentText("Time elapsed : " + time)
                .setSmallIcon(R.drawable.ic_timer)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(3, notification);

    }
}
