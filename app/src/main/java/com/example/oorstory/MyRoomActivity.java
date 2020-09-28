package com.example.oorstory;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRoomActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView mRecyclerView = null;
    TextView finish_tv, like_tv;

    RecyclerAdapter mAdapter_finish = null;
    RecyclerAdapter mAdapter_like = null;

    ArrayList<RecyclerItem> mList_finish = new ArrayList<RecyclerItem>();
    ArrayList<RecyclerItem> mList_like = new ArrayList<RecyclerItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        finish_tv = findViewById(R.id.finish_tv);
        like_tv = findViewById(R.id.like_tv);

        finish_tv.setOnClickListener(this);
        like_tv.setOnClickListener(this);
        findViewById(R.id.back_btn_myRoom).setOnClickListener(this);

        mRecyclerView = findViewById(R.id.recycler2);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mAdapter_finish = new RecyclerAdapter(mList_finish);
        mAdapter_like = new RecyclerAdapter(mList_like);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            set_category_item();
        }

        // 초기값 설정
        like_tv.setBackgroundResource(R.color.colorCategoryNonClick);
        finish_tv.setBackgroundResource(R.color.colorCategoryClick);
        mRecyclerView.setAdapter(mAdapter_finish);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void set_category_item() {
        // 데이터 가져오기
        // 데이터 반영하기
        addItem(getDrawable(R.drawable.example), "첫번째", "음식", 1, 40, true);
        addItem(getDrawable(R.drawable.example), "두번째", "역사", 1, 40, true);
        addItem(getDrawable(R.drawable.example), "세번째", "자연", 1, 40, false);
        addItem(getDrawable(R.drawable.example), "네번째", "음식", 1, 40, true);
        addItem(getDrawable(R.drawable.example), "다섯번째", "역사", 1, 40, true);
        addItem(getDrawable(R.drawable.example), "여섯번째", "자연", 1, 40, false);
        mAdapter_finish.notifyDataSetChanged();
        mAdapter_like.notifyDataSetChanged();
    }

    public void addItem(Drawable title_pic, String title, String theme, int star, int time, boolean is_starred) {
        RecyclerItem item = new RecyclerItem();

        item.setTitle_story_iv(title_pic);
        item.setTitle(title);
        item.setTheme(theme);
        item.setStar_num(star);
        item.setTime(time);
        item.setIsStarred(is_starred);

        if (is_starred==true) {
            mList_like.add(item);
        } else if (true) {//finish 여부를 가져와야함
            mList_finish.add(item);
        }
    }

    @Override
    public void onClick(View v) {
        finish_tv.setBackgroundResource(R.color.colorCategoryNonClick);
        like_tv.setBackgroundResource(R.color.colorCategoryNonClick);

        switch (v.getId()) {
            case R.id.finish_tv:
                finish_tv.setBackgroundResource(R.color.colorCategoryClick);
                mRecyclerView.setAdapter(mAdapter_finish);
                break;

            case R.id.like_tv:
                like_tv.setBackgroundResource(R.color.colorCategoryClick);
                mRecyclerView.setAdapter(mAdapter_like);
                break;

            case R.id.back_btn_myRoom :
                finish();
                break;
        }
    }
}