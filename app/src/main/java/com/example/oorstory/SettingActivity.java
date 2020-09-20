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

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
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
            nickname.setOnPreferenceClickListener(this);
        }

        // 클릭 이벤트
        @Override
        public boolean onPreferenceClick(final Preference preference) {
            switch (preference.getKey()) {
                case "feedback":
                    return false;

                case "logout":
                    return false;

                case "signout":
                    return false;

                case "opensource":
                    return false;

                default:
                    return false;
            }
        }
    }
}