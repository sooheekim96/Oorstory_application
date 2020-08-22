package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.signup_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.login_btn :
                intent = new Intent(LoginActivity.this, PrologueActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.signup_btn :
                intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }
}