package com.example.oorstory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.oorstory.model.Model;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_email, et_id, et_passwd, et_passwd2;
    Button btn_register;
    CircularImageView iv_img;


    private final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$");

    private static final int CAMERA_REQ_CODE = 100;
    private static final int STORAGE_REQ_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;
    private String userid, email, passwd, passwd2;
    private UserdbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        et_email = findViewById(R.id.UserEmail);
        et_id = findViewById(R.id.UserId);
        et_passwd = findViewById(R.id.UserPasswd);
        et_passwd2 = findViewById(R.id.UserPasswd2);
        btn_register = findViewById(R.id.btn_signup);
        iv_img = findViewById(R.id.UserImg);


        dbHelper = new UserdbHelper(this);
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        iv_img.setOnClickListener(v -> imgPick());

        btn_register.setOnClickListener(v -> {
            try{
                userid = ""+et_id.getText().toString().trim();
                email = ""+et_email.getText().toString().trim();
                passwd = ""+et_passwd.getText().toString().trim();
                passwd2 = ""+et_passwd2.getText().toString().trim();
                boolean case1 = (6>userid.length()) || (userid.length()>12);
                boolean case2 = (8>passwd.length()) || (passwd.length()>20 || (isTextValid(passwd)));
                boolean case3 = passwd.length()!=passwd2.length();
                if (case1){
                    Toast.makeText(SignupActivity.this, "6~12자 영문, 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (case2) {
                    Toast.makeText(SignupActivity.this, "8~20자 영문 대소문자, 숫자, 특수문자를 혼합하여 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (case3) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    long id = dbHelper.insertUserRecord(
                            ""+userid,
                            ""+email,
                            ""+imageUri,
                            ""+passwd
                    );

                    if (id>0) {
//                            Toast.makeText(SignupActivity.this, "Record Added against ID:"+id, Toast.LENGTH_SHORT).show();
//                            sleep(10);
                        Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(SignupActivity.this, "Error creating user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch(Exception e) {
                Toast.makeText(SignupActivity.this, "Error creating user", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.back_btn_signup).setOnClickListener(this);
    }

    public boolean isTextValid(String textToCheck) {
        return textPattern.matcher(textToCheck).matches();
    }


    private void imgPick() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Profile Image");
        builder.setItems(options, (dialog, which) -> {
            if (which==0){
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                }
                else {
                    pickFromCamera();
                }
            }
            else if (which==1){
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                }
                else {
                    pickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQ_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result2;
    }

    public void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQ_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera and Storage Permissions are required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQ_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);
                    if (storageAccepted) {
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this, "Storage Permissions is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    iv_img.setImageURI(resultUri);
                }
                else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_btn_signup){
            finish();
        }
    }


}