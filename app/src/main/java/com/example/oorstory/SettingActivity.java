package com.example.oorstory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_frameLayout, new SettingsFragment())
                .commit();

        findViewById(R.id.back_btn_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
        private Preference feedback;
        private Preference logout;
        private Preference signout;
        private Preference opensource;
        private Preference image;
        private Preference nickname;
        public SharedPreferences prefs;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            feedback = findPreference("feedback");
            logout = findPreference("logout");
            signout = findPreference("signout");
            opensource = findPreference("opensource");
            image = findPreference("image");
            nickname = findPreference("nickname");

            // 클릭 이벤트
            feedback.setOnPreferenceClickListener(this);
            logout.setOnPreferenceClickListener(this);
            signout.setOnPreferenceClickListener(this);
            opensource.setOnPreferenceClickListener(this);
            image.setOnPreferenceClickListener(this);

            // 변경 이벤트
            nickname.setOnPreferenceChangeListener(this);
        }

        // 클릭 이벤트
        @Override
        public boolean onPreferenceClick(final Preference preference) {
            switch (preference.getKey()) {
                case "logout":
                    showDialog("로그아웃", "로그아웃 하시겠습니까?", preference.getKey());
                    return false;

                case "signout":
                    showDialog("회원탈퇴", "회원탈퇴 하시겠습니까?", preference.getKey());
                    return false;

                case "opensource":
                    return false;

                case "feedback":
                    Intent email = new Intent((Intent.ACTION_SEND));
                    email.setType("plan/text");
                    String[] address = {"email@address.com"}; // 이메일 받을 주소, 변경 필요
                    email.putExtra(Intent.EXTRA_EMAIL, address);
                    email.putExtra(Intent.EXTRA_SUBJECT, "feedback email");
                    email.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(email);
                    return false;

                default:
                    return false;
            }
        }

        public void showDialog(String title, String message, final String key){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(title).setMessage(message)
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (key.equals("logout")){}
                            else if (key.equals("signout")){}

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().finish();
                            startActivity(intent);
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals("nickname")){
                //nickname 변경
            }
            return false;
        }
    }
}