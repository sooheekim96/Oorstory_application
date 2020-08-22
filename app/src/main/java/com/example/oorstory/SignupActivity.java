package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.back_btn_signup).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_btn_signup){
            finish();
        }
    }
}