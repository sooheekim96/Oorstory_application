package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PrologueActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar mProgBar;
    private TextView progTv;
    private ImageView proImg;
    private TextView progNext;
    private String[] prologues = {
            "우리 열차는 잠시 후 서울역에 도착하겠습니다. ",
            "당신이 꿈에 그리던 서울에 드디어 도착했습니다. ",
            "이 넓은 서울을 어디부터 돌아볼건가요?",
            "잠깐만요 ! 당신이 핸드폰 하나와 자전거만 들고 온 걸 잊은 건 아니죠?",
            "거기다가 한달 후에는 집으로 돌아가야 돼요",
            "당연하죠 ! 지금바로 떠나볼까요?"};
    private String[] prologuesText = {
            "다음으로",
            "여기가 서울인가!",
            "일단 버스타고 강남으로 갈래",
            "뭐?",
            "한달만에 서울을 다 구경할 수 있을까?",
            "그래!"
    };
    private String[] prologuesImg = {String.valueOf(R.drawable.pro1),
            String.valueOf(R.drawable.pro2),
            String.valueOf(R.drawable.pro3),
            String.valueOf(R.drawable.pro4),
            String.valueOf(R.drawable.pro5),
            String.valueOf(R.drawable.pro5)};
    private int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prologue);

        progTv = findViewById(R.id.prologue_tv);
        progNext = findViewById(R.id.next_tv);
        mProgBar = findViewById(R.id.prologue_pb);
        proImg = (ImageView)findViewById(R.id.proImgView);

        // 초기값 지정
        index = 0;
        if (prologues.length>0) {
            progTv.setText(prologues[0]);
            progNext.setText(prologuesText[0]);
            proImg.setImageResource(Integer.parseInt(prologuesImg[0]));
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
                    progNext.setText(prologuesText[index]);
                    progTv.setText(prologues[index]);
                    proImg.setImageResource(Integer.parseInt(prologuesImg[index]));
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
                    progNext.setText(prologues[index]);
                    progTv.setText(prologues[index]);
                    proImg.setImageResource(Integer.parseInt(prologuesImg[index]));
                } else{ index = index+1;
                }
                break;
        }
    }
}
