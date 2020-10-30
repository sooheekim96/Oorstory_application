package com.example.oorstory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import com.example.oorstory.model.Model;
import com.example.oorstory.model.Place;
import com.example.oorstory.model.SharedPreferenceUtil;
import com.example.oorstory.model.Story;
import com.example.oorstory.model.UserModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class UserdbHelper extends SQLiteOpenHelper {
    //    Initialize Variable
    public static UserdbHelper UserdbHelper = null;
    Context context;

    public static UserdbHelper getInstance(Context context){
        if(UserdbHelper == null){
            UserdbHelper = new UserdbHelper(context);
            UserdbHelper.getReadableDatabase();
            Log.i(TAG,"db created writable constructor2222");
        }
        return UserdbHelper;
    }


    public UserdbHelper(Context context){
        super(context, Model.DATABASE_NAME, null, 1);
        SQLiteDatabase db = getReadableDatabase();
        Log.i(TAG,"db created writable constructor");
    }

    public void onCreate(SQLiteDatabase db) {
        //Create Tables
        db.execSQL(UserModel.userInfoModel.SQL_CREATE_TABLE);
        db.execSQL(UserModel.userInfoModel.SQL_CREATE_TABLE2);
        db.execSQL(UserModel.userInfoModel.SQL_CREATE_TABLE3);
        db.execSQL(UserModel.userInfoModel.SQL_CREATE_TABLE4);
        db.execSQL(UserModel.userInfoModel.SQL_CREATE_TABLE5);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Existing Table
        db.execSQL(UserModel.userInfoModel.SQL_DELETE_TABLE);
        db.execSQL(UserModel.userInfoModel.SQL_DELETE_TABLE2);
        db.execSQL(UserModel.userInfoModel.SQL_DELETE_TABLE3);
        db.execSQL(UserModel.userInfoModel.SQL_DELETE_TABLE4);
        db.execSQL(UserModel.userInfoModel.SQL_DELETE_TABLE5);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }

    //Create Insert Method
    public long insertUserRecord(String id, String email, String image, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Model.USERID, id);
            values.put(Model.EMAIL, email);
            values.put(Model.USERIMG, image);
            values.put(Model.PASSWORD, password);
            long answer = db.insert(Model.USERTABLE, null, values);
            return answer;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public long insertComRecord(Context applicationContext, String content, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Model.USERID, SharedPreferenceUtil.getInstance(applicationContext).getUserid());
            values.put(Model.COMMENTCONTENT, content);
            values.put(Model.COMMENTDATE, date);
            return db.insert(Model.COMMENTTABLE, null, values);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public long insertPlaceRecord(String name, String img, String flow, float latitude, float longitude, String missionname, String missionflow, String missionimg, int fin, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Model.PLACENAME, name);
            values.put(Model.PLACEIMG, img);
            values.put(Model.PLACEFLOW, flow);
            values.put(Model.LATITUDE, latitude);
            values.put(Model.LONGTITUDE, longitude);
            values.put(Model.MISSIONNAME, missionname);
            values.put(Model.MISSIONFLOW, missionflow);
            values.put(Model.MISSIONIMG, missionimg);
            values.put(Model.PLACEISFIN, fin);
            values.put(Model.PLACEFINDATE, date);
            return db.insert(Model.PLACETABLE, null, values);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public long insertStoryRecord(String name, int diff, String theme, int time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Model.STORYNAME, name);
            values.put(Model.STORYDIFF, diff);
            values.put(Model.STORYNAME, theme);
            values.put(Model.STORYTIME, time);
            return db.insert(Model.STORYTABLE, null, values);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public long insertGaugeRecord(String name, int fin, String time, String date, int percent){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Model.STORYNAME, name);
            values.put(Model.STORYISFIN, fin);
            values.put(Model.STORYFINTIME, time);
            values.put(Model.STORYFINDATE, date);
            values.put(Model.STORYGAUGE, percent);
            return db.insert(Model.GAUGETABLE, null, values);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    //Read Method
    public Cursor readComment(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Model.COMMENTTABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor readUser() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Model.USERTABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor readStory() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Model.STORYTABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor readPlace() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Model.PLACETABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor readGauge() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Model.GAUGETABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Place getPlace(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Model.PLACETABLE,
                new String[]{Model.INDEX, Model.PLACENAME, Model.PLACEIMG, Model.PLACEFLOW, Model.LATITUDE, Model.LONGTITUDE,
                            Model.MISSIONNAME, Model.MISSIONFLOW, Model.MISSIONIMG, Model.PLACEISFIN, Model.PLACEFINDATE, Model.PLACEGAUGE},
                Model.INDEX + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Place place = new Place(
                cursor.getInt(cursor.getColumnIndex(Model.INDEX)),
                cursor.getString(cursor.getColumnIndex(Model.PLACENAME)),
                cursor.getString(cursor.getColumnIndex(Model.PLACEIMG)),
                cursor.getString(cursor.getColumnIndex(Model.PLACEFLOW)),
                cursor.getFloat(cursor.getColumnIndex(Model.LATITUDE)),
                cursor.getFloat(cursor.getColumnIndex(Model.LONGTITUDE)),
                cursor.getString(cursor.getColumnIndex(Model.MISSIONNAME)),
                cursor.getString(cursor.getColumnIndex(Model.MISSIONFLOW)),
                cursor.getString(cursor.getColumnIndex(Model.MISSIONIMG)),
                cursor.getInt(cursor.getColumnIndex(Model.PLACEISFIN)),
                cursor.getString(cursor.getColumnIndex(Model.PLACEFINDATE)),
                cursor.getInt(cursor.getColumnIndex(Model.PLACEGAUGE))
                );
        return place;
    }

    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Model.PLACETABLE + " ORDER BY " +
                Model.INDEX + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setPlaceId(cursor.getInt(cursor.getColumnIndex(Model.INDEX)));
                place.setPlaceName(cursor.getString(cursor.getColumnIndex(Model.PLACENAME)));
                place.setPlaceImg(cursor.getString(cursor.getColumnIndex(Model.PLACEIMG)));
                place.setPlaceFlow(cursor.getString(cursor.getColumnIndex(Model.PLACEFLOW)));
                place.setPlaceLatitude(cursor.getFloat(cursor.getColumnIndex(Model.LATITUDE)));
                place.setPlaceLongitude(cursor.getFloat(cursor.getColumnIndex(Model.LONGTITUDE)));
                place.setMissionName(cursor.getString(cursor.getColumnIndex(Model.MISSIONNAME)));
                place.setMissionFlow(cursor.getString(cursor.getColumnIndex(Model.MISSIONFLOW)));
                place.setMissionImg(cursor.getString(cursor.getColumnIndex(Model.MISSIONIMG)));
                place.setplaceIsfin(cursor.getInt(cursor.getColumnIndex(Model.PLACEISFIN)));
                place.setPlaceFindate(cursor.getString(cursor.getColumnIndex(Model.PLACEFINDATE)));
                place.setplaceGauge(cursor.getInt(cursor.getColumnIndex(Model.PLACEGAUGE)));
                places.add(place);
            } while (cursor.moveToNext());
        }
        return places;
    }

    public int getPlacesCount() {
        String countQuery = "SELECT  * FROM " + Model.PLACETABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public Story getStory(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Model.STORYTABLE,
                new String[]{Model.INDEX, Model.STORYNAME, Model.STORYIMG, Model.STORYTHEME, Model.STORYDIFF, Model.STORYTIME},
                Model.INDEX + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Story story = new Story(
                cursor.getInt(cursor.getColumnIndex(Model.INDEX)),
                cursor.getString(cursor.getColumnIndex(Model.STORYNAME)),
                cursor.getString(cursor.getColumnIndex(Model.STORYIMG)),
                cursor.getInt(cursor.getColumnIndex(Model.STORYDIFF)),
                cursor.getString(cursor.getColumnIndex(Model.STORYTHEME)),
                cursor.getInt(cursor.getColumnIndex(Model.STORYTIME)));
        return story;
    }

    public List<Story> getAllStories() {
        List<Story> stories = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Model.STORYTABLE + " ORDER BY " +
                Model.INDEX + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Story story = new Story();
                story.setId(cursor.getInt(cursor.getColumnIndex(Model.INDEX)));
                story.setName(cursor.getString(cursor.getColumnIndex(Model.STORYNAME)));
                story.setImg(cursor.getString(cursor.getColumnIndex(Model.STORYIMG)));
                story.setDiff(cursor.getInt(cursor.getColumnIndex(Model.STORYDIFF)));
                story.setTheme(cursor.getString(cursor.getColumnIndex(Model.STORYTHEME)));
                story.setTime(cursor.getInt(cursor.getColumnIndex(Model.STORYTIME)));
                stories.add(story);
            } while (cursor.moveToNext());
        }
        return stories;
    }


    public int getStoryCount() {
        String countQuery = "SELECT  * FROM " + Model.STORYTABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }





    // delete user info
    public void delete(Context context){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Model.USERTABLE, "id?=",new String[]{SharedPreferenceUtil.getInstance(context).getUserid()});
    }

    // update user info(nickname, image)
    public void update(Context context, String col, String str){
        SQLiteDatabase db = getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        if(col=="nickname"){
            values.put(Model.NICKNAME, str);
        }
        else if(col=="image"){
            values.put(Model.USERIMG, str);
        }
        // Which row to update, based on the user id
        db.update(Model.USERTABLE, values, Model.USERID+" LIKE ?", new String[]{SharedPreferenceUtil.getInstance(context).getUserid()});
//        if this doesn't work, try this
//        String sql = "UPDATE "+Model.USERTABLE+" SET "+Model.NICKNAME+" = "+str+" WHERE id = "+readId()+";";
//        String sql = "UPDATE "+Model.USERTABLE+" SET "+Model.USERIMG+" = "+str+" WHERE id = "+readId()+";";
//        db.execSQL(sql);
    }
}


