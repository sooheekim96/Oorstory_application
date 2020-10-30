package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    EditText et_email, et_passwd;
    private String email, passwd;
    private UserdbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.login_email);
        et_passwd = findViewById(R.id.login_passwd);

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
                dbHelper = new UserdbHelper(this);
                email = ""+et_email.getText().toString().trim();
                passwd = ""+et_passwd.getText().toString().trim();
                long id = dbHelper.readLoginRecord(
                        ""+email,
                        ""+passwd
                );
                if (id==1) {
                    intent = new Intent(LoginActivity.this, PrologueActivity.class);
                    finish();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "아이디나 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
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