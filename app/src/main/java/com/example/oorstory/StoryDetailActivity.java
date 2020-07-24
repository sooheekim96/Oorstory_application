package com.example.oorstory;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StoryDetailActivity extends AppCompatActivity {

    String title_story = getIntent().getStringExtra("title_story");
    TextView storyTitleName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        storyTitleName = findViewById(R.id.storyTitleName);
        storyTitleName.setText(title_story);
    }
}
