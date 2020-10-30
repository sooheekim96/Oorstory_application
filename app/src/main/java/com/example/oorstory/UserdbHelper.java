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
        Log.i(TAG,"db.exect");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Existing Table
        db.execSQL(UserModel.userInfoModel.SQL_DELETE_TABLE);
        Log.i(TAG,"db.exect_upgrade");
        onCreate(db);
        Log.i(TAG,"db.exect_upgrade");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
        Log.i(TAG,"db.exect_downgrade");
    }

    //Create Insert Method
    public long insertRecord(String id, String email, String image, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
//            FileInputStream fs = new FileInputStream(image);
//            byte[] imgbyte = new byte[fs.available()];
//            fs.read(imgbyte);
//            db.beginTransaction();

            values.put(Model.USERID, id);
            values.put(Model.EMAIL, email);
            values.put(Model.USERIMG, image);
//            values.put(Model.USERIMG, imgbyte);
            values.put(Model.PASSWORD, password);
            long answer = db.insert(Model.USERTABLE, null, values);

            db.close();
//            fs.close();

            return answer;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
//        } catch (IOException e){
//            e.printStackTrace();
//            return -1;
//        }
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

//    public long readLoginRecord(String email, String password){
//        SQLiteDatabase db = getReadableDatabase();
//        ContentValues values = new ContentValues();
//        try{
//            String[] projection = {
//                    BaseColumns._ID, //Primary Key
//                    Model.USERID,
//                    Model.EMAIL,
//                    Model.USERIMG,
//                    Model.PASSWORD
//            };
//            String checkEmail = Model.EMAIL + " = ?";
//            String checkPassword = Model.PASSWORD + " = ?";
//            String[] selectionArgs = { email, password };
//            String sortOrder =
//                    Model.EMAIL + " DESC";
//
//            Cursor cursor = db.query(
//                    Model.USERTABLE,
//                    projection,
//                    checkEmail+ " and "+checkPassword,
//                    selectionArgs,
//                    null,
//                    null,
//                    sortOrder
//            );
//            List itemIds = new ArrayList<>();
//            while(cursor.moveToNext()) {
//                long itemId = cursor.getLong(
//                        cursor.getColumnIndexOrThrow(Model._ID));
//                itemIds.add(itemId);
//            }
//            cursor.close();
//
//
//            return 1;
//        } catch (Exception e){
//            e.printStackTrace();
//            return -1;
//        }
//    }
}


