package com.example.oorstory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import java.io.InputStream;
import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

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
        private Preference introduce;
        public SharedPreferences prefs;

        int PICK_IMAGE;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            feedback = findPreference("feedback");
            logout = findPreference("logout");
            signout = findPreference("signout");
            opensource = findPreference("opensource");
            image = findPreference("image");
            nickname = findPreference("nickname");
            introduce = findPreference("introduce");

            // 클릭 이벤트
            feedback.setOnPreferenceClickListener(this);
            logout.setOnPreferenceClickListener(this);
            signout.setOnPreferenceClickListener(this);
            opensource.setOnPreferenceClickListener(this);
            image.setOnPreferenceClickListener(this);
            introduce.setOnPreferenceClickListener(this);

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
                    showMyLicencesDialog(getView());
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

                case "image" :
                    PICK_IMAGE = 100;
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_IMAGE);
                    return false;

                case "introduce" :
                    String url ="https://www.notion.so/Oorstory-72129aa53e684082b189ca4a44f3432f";
                    Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(web);
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

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Check which request we're responding to
            if (requestCode == PICK_IMAGE) {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        // 이미지뷰에 세팅
                        //mPhotoCircleImageView.setImageBitmap(img);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /*
         * Copyright 2013 Philip Schiffer
         *
         *    Licensed under the Apache License, Version 2.0 (the "License");
         *    you may not use this file except in compliance with the License.
         *    You may obtain a copy of the License at
         *
         *        http://www.apache.org/licenses/LICENSE-2.0
         *
         *    Unless required by applicable law or agreed to in writing, software
         *    distributed under the License is distributed on an "AS IS" BASIS,
         *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         *    See the License for the specific language governing permissions and
         *    limitations under the License.
         */
        public void showMyLicencesDialog(final View view) {
            final Notices notices = new Notices();
            notices.addNotice(new Notice("Android Image Cropper", "https://github.com/ArthurHub/Android-Image-Cropper", "Copyright 2016, Arthur Teplitzki, 2013, Edmodo, Inc.", new ApacheSoftwareLicense20()));
            notices.addNotice(new Notice("CircleIndicator", "https://github.com/ongakuer/CircleIndicator", "Copyright (C) 2014 relex", new ApacheSoftwareLicense20()));
            //notices.addNotice(new Notice("Test 1", "http://example.org", "Example Person", new ApacheSoftwareLicense20()));
            //notices.addNotice(new Notice("Test 2", "http://example.org", "Example Person 2", new MITLicense()));

            new LicensesDialog.Builder(getContext())
                    .setNotices(notices)
                    .setIncludeOwnLicense(true)
                    .build()
                    .show();
        }
    }
}