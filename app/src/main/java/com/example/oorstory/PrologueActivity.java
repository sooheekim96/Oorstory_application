package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PrologueActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar mProgBar;
    private TextView progTv;
    private String[] prologues = {"프롤로그입니다.1", "프롤로그입니다.2", "프롤로그입니다.3", "프롤로그입니다.4", "프롤로그입니다.5", "프롤로그입니다.6"};
    private int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prologue);

        progTv = findViewById(R.id.prologue_tv);
        mProgBar = findViewById(R.id.prologue_pb);

        // 초기값 지정
        index = 0;
        if (prologues.length>0) {
            progTv.setText(prologues[0]);
            mProgBar.setMax(prologues.length-1);
        }

        // 클릭 이벤트
        findViewById(R.id.skip_tv).setOnClickListener(this);
        findViewById(R.id.next_tv).setOnClickListener(this);
        findViewById(R.id.prev_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PrologueActivity.this, MainActivity.class);

        switch (v.getId()){
            case R.id.skip_tv :
                finish();
                startActivity(intent);
                break;

            case R.id.next_tv :
                index = index+1;
                if (index>=0 && index<prologues.length){
                    mProgBar.setProgress(index);
                    progTv.setText(prologues[index]);
                } else{
                    finish();
                    startActivity(intent);
                    index = index-1;
                }
                break;

            case R.id.prev_tv :
                index = index-1;
                if (index>=0 && index<prologues.length){
                    mProgBar.setProgress(index);
                    progTv.setText(prologues[index]);
                } else{ index = index+1;
                }
                break;
        }
    }
}