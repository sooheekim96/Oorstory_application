package com.example.oorstory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.oorstory.model.Model;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class dbHelper extends SQLiteOpenHelper implements BaseColumns {
    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlUser = "create table if not exists "+ Model.USERTABLE+
                "(_no integer primary key autoincrement,"+
                "id text,"+
                "email text,"+
                "image text,"+
                "password text);";
        db.execSQL(sqlUser);

        String sqlComment = "create table if not exists "+ Model.COMMENTTABLE+
                "(_no integer primary key autoincrement,"+
                "id text,"+
                "content text);";
        db.execSQL(sqlComment);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlUser = "drop table if exists "+Model.USERTABLE+";";
        db.execSQL(sqlUser);

        String sqlComment = "drop table if exists "+Model.COMMENTTABLE+";";
        db.execSQL(sqlComment);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
        Log.i(TAG,"db.exect_downgrade");
    }


}
