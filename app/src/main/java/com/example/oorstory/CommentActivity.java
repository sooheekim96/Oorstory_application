package com.example.oorstory;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    RecyclerCommentsAdapter cAdapter = null;
    RecyclerView c_recycler = null;

    ArrayList<RecyclerComments> cList = new ArrayList<RecyclerComments>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        c_recycler = findViewById(R.id.c_recycler);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        c_recycler.setLayoutManager(manager);

        cAdapter = new RecyclerCommentsAdapter(cList);

        addItem("장한솔", "재밌어요", "2020-08-22");
        addItem("유다연", "재미없어요", "2020-08-23");
    }

    public void addItem( String nickname, String comment, String date) {
        RecyclerComments item = new RecyclerComments();

        item.setNickname(nickname);
        item.setComment(comment);
        item.setDate(date);

        cList.add(item);
    }
}
