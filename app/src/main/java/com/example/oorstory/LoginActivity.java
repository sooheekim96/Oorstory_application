package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView guest = findViewById(R.id.guest_tv);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.signup_btn).setOnClickListener(this);
        guest.setOnClickListener(this);
        guest.setText(Html.fromHtml("<u>Try Guest Mode</u>", 100));
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
            case R.id.guest_tv :
                intent = new Intent(LoginActivity.this, PrologueActivity.class);
                finish();
                startActivity(intent);
                break;
        }
    }
}