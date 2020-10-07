package com.example.oorstory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton setting_btn;
    TextView place_tv, food_tv, hist_tv, nature_tv;
    RecyclerView mRecyclerView = null;
    RecyclerAdapter mAdapter = null;
    RecyclerAdapter mAdapter_place = null;
    RecyclerAdapter mAdapter_food = null;
    RecyclerAdapter mAdapter_hist = null;
    RecyclerAdapter mAdapter_nature = null;

    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();
    ArrayList<RecyclerItem> mList_place = new ArrayList<RecyclerItem>();
    ArrayList<RecyclerItem> mList_food = new ArrayList<RecyclerItem>();
    ArrayList<RecyclerItem> mList_hist = new ArrayList<RecyclerItem>();
    ArrayList<RecyclerItem> mList_nature = new ArrayList<RecyclerItem>();

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting_btn = findViewById(R.id.setting_btn);
        place_tv = findViewById(R.id.place_tv);
        food_tv = findViewById(R.id.food_tv);
        hist_tv = findViewById(R.id.hist_tv);
        nature_tv = findViewById(R.id.nature_tv);

        mRecyclerView = findViewById(R.id.recycler1);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mAdapter_place = new RecyclerAdapter(mList_place);
        mAdapter_food = new RecyclerAdapter(mList_food);
        mAdapter_hist = new RecyclerAdapter(mList_hist);
        mAdapter_nature = new RecyclerAdapter(mList_nature);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            set_category_item();
        }

        place_tv.performClick();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(MainActivity.this, "" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                if (parent.getChildCount()>0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parent.getChildAt(0)).setGravity(Gravity.RIGHT);
                }
            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String address = intent.getStringExtra("address");
            if (address != null && !address.equals("")) {
                Toast.makeText(MainActivity.this, "Main : " + address, Toast.LENGTH_LONG).show();
            }
        }

        findViewById(R.id.myRoom_btn).setOnClickListener(this);// open MyRoom
        findViewById(R.id.setting_btn).setOnClickListener(this);// open setting
        findViewById(R.id.location_LL).setOnClickListener(this);//open Location
    }

    public void addItem(Drawable title_pic, String title, String theme, int star, int time, boolean is_starred) {
        RecyclerItem item = new RecyclerItem();

        item.setTitle_story_iv(title_pic);
        item.setTitle(title);
        item.setTheme(theme);
        item.setStar_num(star);
        item.setTime(time);
        item.setIsStarred(is_starred);

        if (theme.equals("명소")) {
            mList_place.add(item);
        } else if (theme.equals("음식")) {
            mList_food.add(item);
        } else if (theme.equals("역사")) {
            mList_hist.add(item);
        } else if (theme.equals("자연")) {
            mList_nature.add(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void set_category_item() {
        addItem(getDrawable(R.drawable.example), "서울 한복판에서 김서방 5명 찾기", "명소", 3, 150, false);

        addItem(getDrawable(R.drawable.example), "완벽한 퇴근길", "음식", 1, 40, true);

        addItem(getDrawable(R.drawable.example), "완벽한 퇴근길", "역사", 1, 40, true);

        addItem(getDrawable(R.drawable.example), "완벽한 퇴근길", "자연", 1, 40, true);


        mAdapter_place.notifyDataSetChanged();
        mAdapter_food.notifyDataSetChanged();
        mAdapter_hist.notifyDataSetChanged();
        mAdapter_nature.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void category_onClick(View view) {
        place_tv.setBackgroundResource(R.color.colorCategoryNonClick);
        food_tv.setBackgroundResource(R.color.colorCategoryNonClick);
        nature_tv.setBackgroundResource(R.color.colorCategoryNonClick);
        hist_tv.setBackgroundResource(R.color.colorCategoryNonClick);

        switch (view.getId()) {
            case R.id.place_tv:
                place_tv.setBackgroundResource(R.color.colorCategoryClick);
                mRecyclerView.setAdapter(mAdapter_place);
//                mRecyclerView.notify();
                break;

            case R.id.food_tv:
                food_tv.setBackgroundResource(R.color.colorCategoryClick);
                mRecyclerView.setAdapter(mAdapter_food);
//                mRecyclerView.notify();
                break;

            case R.id.hist_tv:
                hist_tv.setBackgroundResource(R.color.colorCategoryClick);
                mRecyclerView.setAdapter(mAdapter_hist);
//                mRecyclerView.notify();
                break;

            case R.id.nature_tv:
                nature_tv.setBackgroundResource(R.color.colorCategoryClick);
                mRecyclerView.setAdapter(mAdapter_nature);
//                mRecyclerView.notify();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.myRoom_btn :
                intent = new Intent(MainActivity.this, MyRoomActivity.class);
                // this.finish();
                startActivity(intent);
                break;
            case R.id.setting_btn :
                intent = new Intent(MainActivity.this, SettingActivity.class);
                // this.finish();
                startActivity(intent);
                break;
            case R.id.location_LL:
                intent = new Intent(MainActivity.this, LocationActivity.class);
                // this.finish();
                startActivity(intent);
                break;
        }
    }

    // 뒤로가기 두번 클릭시 (2초 내) 어플 종료하기
   @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
    }

}