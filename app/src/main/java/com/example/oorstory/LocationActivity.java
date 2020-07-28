package com.example.oorstory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LocationActivity extends AppCompatActivity {

    EditText input1;
    int select = 0;
    String lats[];
    String lngs[];
    public static String defaultUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        input1 = (EditText)findViewById(R.id.search_loc_et);

        ImageButton requestBtn = (ImageButton) findViewById(R.id.search_loc_iv);
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userStr = input1.getText().toString();
                String urlStr = defaultUrl + userStr + "&key=AIzaSyAHE7vqzlMfhfcfTniHb9UpEIYBBjR3UHY & language=ko";

                ConnectThread thread = new ConnectThread(urlStr);
                thread.start();
            }
        });

        // 메인화면으로 돌아가기
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    private void findLatLng(String output){
        Log.d("output", output);
        try {
            JSONObject jsonObject = new JSONObject(output);
            String status = jsonObject.getString("status");
            String condition = status.trim();

            if (condition.equals("OK")){
                JSONArray jsonResultsArray = new JSONArray(jsonObject.getString("results"));
                int jsonResultsLength = jsonResultsArray.length();

                if(jsonResultsLength > 5){
                    Toast.makeText(LocationActivity.this, "검색된 결과 값이 너무 많습니다.", Toast.LENGTH_LONG).show();
                }
                else if (jsonResultsLength>=1){
                    final String addresses[] = new String[jsonResultsLength];
                    lats = new String[jsonResultsLength];
                    lngs = new String[jsonResultsLength];

                    for (int i = 0; i < jsonResultsLength; i++){
                        String address = jsonResultsArray.getJSONObject(i).getString("formatted_address");

                        JSONObject geoObject = new JSONObject(jsonResultsArray.getJSONObject(i).getString("geometry"));
                        JSONObject locObject = new JSONObject(geoObject.getString("location"));
                        String lat = locObject.getString("lat");
                        String lng = locObject.getString("lng");

                        addresses[i] = address;
                        lats[i] = lat;
                        lngs[i] = lng;

//                        Toast.makeText(this, address, Toast.LENGTH_LONG).show();
                    }

                    AlertDialog.Builder ab = new AlertDialog.Builder(LocationActivity.this);
                    ab.setTitle("아래에서 해당 주소를 선택하세요");
                    ab.setSingleChoiceItems(addresses, select, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            select= i;
                        }
                    }).setPositiveButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Toast.makeText(LocationActivity.this, "lat : "+lats[select] + "\nlng : " + lngs[select], Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                            intent.putExtra("address", addresses[select]);
                            finish();
                            startActivity(intent);

                            //txtmsg.setText("lat : "+lats[select] + "\nlng : " + lngs[select]);;
                        }
                    }).setNegativeButton("취소", null);
                    ab.show();
                } else if(jsonResultsLength == 1){
                    JSONObject geoObject = new JSONObject(jsonResultsArray.getJSONObject(0).getString("geometry"));
                    JSONObject locObject = new JSONObject(geoObject.getString("location"));
                    String lat = locObject.getString("lat");
                    String lng = locObject.getString("lng");

                    String address = jsonResultsArray.getJSONObject(0).getString("formatted_address");
                    Toast.makeText(LocationActivity.this, address, Toast.LENGTH_LONG).show();

//                    txtmsg.setText("lat : "+lat + "\nlng : "+lng);
                }
            } else {
                Toast.makeText(LocationActivity.this, "해당 조회 결과 값이 없습니다.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ConnectThread extends Thread {
        String urlStr;

        public ConnectThread(String inStr){
            urlStr = inStr;
        }

        public void run(){
            try {
                final String output = request(urlStr);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        findLatLng(output);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private String request(String urlStr) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection)url.openConnection();

                if(conn !=null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept-Charset","UTF-8");

                    int resCode = conn.getResponseCode();

                    Log.d("resCode", String.valueOf(resCode));
                    if(resCode == HttpURLConnection.HTTP_OK){
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        String line = null;
                        while(true) {
                            line = reader.readLine();
                            if(line == null){
                                break;
                            }
                                output.append(line + "\n");
                        }

                        reader.close();
                        conn.disconnect();
                    }
                }
                } catch (IOException e) {
                    Log.e("SampleHTTP", "Exception in processing response.", e);
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return output.toString();
        }
    }
}