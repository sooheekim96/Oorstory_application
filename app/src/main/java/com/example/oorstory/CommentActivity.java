package com.example.oorstory;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerCommentsAdapter cAdapter = null;
    RecyclerView c_recycler = null;

    ArrayList<RecyclerComments> cList = new ArrayList<RecyclerComments>();

    EditText et_comment;
    private String content;
    private UserdbHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        c_recycler = findViewById(R.id.c_recycler);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        c_recycler.setLayoutManager(manager);

        cAdapter = new RecyclerCommentsAdapter(cList);

        et_comment = findViewById(R.id.et_comment);
        findViewById(R.id.btn_comment_add).setOnClickListener(this);
        findViewById(R.id.back_btn_toMap).setOnClickListener(this);

        addItem("마카롱케잌", "김서방 찾는거 바로 되신 분? 전 운 좋게 바로 찾았어요 ㅎㅎ", "2020-04-04");
        addItem("붉은하늘", "이 앱이 나를 여기저기 움직이게 만드네! 짱추짱추><", "2020-08-21");
        addItem("실도둑", "댓글보고 쉽겠다 하고 김서방찾기 게임했는데 저만 김서방 찾기 어려운건가요...?", "2020-08-22");
        addItem("김태형", "힌트를 잘 보면서 김서방 찾으려고 노력하면 높은 확률로 김서방 찾을 수 있던데!!","2020-08-22");
        addItem("유다연","재밌네요!","2020-08-23");
        addItem("자전거조아","이런 앱도 있었엌ㅋㅋ재밌닼ㅋㅋㅋ","2020-08-24");

        //DataArrays();

        c_recycler.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
        Button comment_btn = findViewById(R.id.btn_comment_add);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new UserdbHelper(CommentActivity.this);
                content = ""+et_comment.getText().toString();
                String timeStamp = ""+System.currentTimeMillis();
                long id = dbHelper.insertComRecord(
                        getApplicationContext(),
                        ""+content,
                        ""+timeStamp
                );
                Toast.makeText(CommentActivity.this, "댓글 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                insert read last db column and addItem code

            }
        });
        // mapActivity로 돌아가기
        ImageButton back_btn = findViewById(R.id.back_btn_toMap);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                //Intent intent = new Intent(CommentActivity.this, MapActivity.class);
                finish();
                //startActivity(intent);
            }
        });
    }

    public void addItem( String nickname, String comment, String date) {
        RecyclerComments item = new RecyclerComments();

        item.setNickname(nickname);
        item.setComment(comment);
        item.setDate(date);

        cList.add(item);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_comment_add :
                dbHelper = new UserdbHelper(this);
                content = ""+et_comment.getText().toString();
                String timeStamp = ""+System.currentTimeMillis();
                long id = dbHelper.insertComRecord(
                        getApplicationContext(),
                        ""+content,
                        ""+timeStamp
                );
                Toast.makeText(this, "댓글 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                insert read last db column and addItem code
                break;
            case R.id.back_btn_toMap :
                finish();
                break;
        }
    }

    void DataArrays(){
        Cursor cursor = dbHelper.readComment();
        while (cursor.moveToNext()) {
            addItem(cursor.getString(0),cursor.getString(1),cursor.getString(2));
        }
    }
}
