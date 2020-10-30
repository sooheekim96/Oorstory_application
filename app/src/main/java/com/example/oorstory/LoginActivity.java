package com.example.oorstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.oorstory.model.Model;
import com.example.oorstory.model.SharedPreferenceUtil;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

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
            case R.id.login_btn:
                dbHelper = new UserdbHelper(this);
                email = "" + et_email.getText().toString().trim();
                passwd = "" + et_passwd.getText().toString().trim();
                Cursor cursor = dbHelper.readUser();
                try {
                    while (cursor.moveToNext()) { //while cursor goes last one
                        String id = cursor.getString(1);
                        String em = cursor.getString(2);
                        String pw = cursor.getString(4);
                        if (em.equals(email) && pw.equals(passwd)) {
                            Log.i(TAG, Model.USERTABLE + " email : " + email + "pw: " + passwd);
                            SharedPreferenceUtil.getInstance(getApplicationContext()).setUserid(id);
                            cursor.close();
                            intent = new Intent(LoginActivity.this, PrologueActivity.class);
                            finish();
                            startActivity(intent);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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