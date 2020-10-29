package com.example.oorstory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.oorstory.model.Model;
import com.example.oorstory.model.UserModel;


public class UserdbHelper extends SQLiteOpenHelper {
    //    Initialize Variable
    public static UserdbHelper UserdbHelper = null;
    Context context;

    public static UserdbHelper getInstance(Context context){
        if(UserdbHelper == null){
            UserdbHelper = new UserdbHelper(context);
            UserdbHelper.getReadableDatabase();
        }
        return UserdbHelper;
    }

    public UserdbHelper(Context context){
        super(context, Model.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Tables
        db.execSQL(UserModel.userInfoModel.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Existing Table
        db.execSQL(UserModel.userInfoModel.SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }

    //Create Insert Method
    public long insertRecord(String id, String email, String image, String password) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Model.USERID, id);
        values.put(Model.EMAIL, email);
        values.put(Model.USERIMG, image);
        values.put(Model.PASSWORD, password);
        long answer = db.insert(Model.USERTABLE, null, values);
        db.close();

        return answer;
    }
    //Read Method
    public Cursor readRecordOrderByID() {
        SQLiteDatabase db = UserdbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID, //Primary Key
                Model.USERID,
                Model.EMAIL,
                Model.USERIMG,
                Model.PASSWORD
        };

        String sortOrder = UserModel.userInfoModel._ID + " DESC";

        Cursor cursor = db.query(
                "user",   // The table to query
                projection,   // The array of columns to return (pass null to get all)
                null,   // where 문에 필요한 column
                null,   // where 문에 필요한 value
                null,   // group by를 적용할 column
                null,   // having 절
                sortOrder   // 정렬 방식
        );
        return cursor;
    }

}


