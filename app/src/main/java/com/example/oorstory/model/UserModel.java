package com.example.oorstory.model;

import android.provider.BaseColumns;

import java.sql.Timestamp;


public class UserModel {
    public static class userInfoModel implements BaseColumns {
        public static final String SQL_CREATE_TABLE =
                "create table if not exists "+Model.USERTABLE+
                        "(_no integer primary key autoincrement,"+
                        "id text,"+
                        "email text,"+
                        "image text,"+
                        "password text);";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS "+Model.USERTABLE;
    }

//    public static class userPlaceModel implements BaseColumns {
//        public static final String TABLE_NAME = "userPlaceTable";
//        public static final String COLUMN_PLACEISFIN = "placeIsfin";
//        public static final Timestamp COLUMN_PLACEFINDATE = Timestamp.valueOf("placeFindate");
//        public static final String SQL_CREATE_TABLE =
//                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
//                        _ID + " INTEGER PRIMARY KEY," +
//                        COLUMN_USERID + " TEXT," +
//                        userStoryModel.COLUMN_STORYID + " TEXT," +
//                        COLUMN_PLACEISFIN + " TEXT," +
//                        COLUMN_PLACEFINDATE + " TIME)";
//
//        public static final String SQL_DELETE_TABLE =
//                "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
//
//    public static class userStoryModel implements BaseColumns {
//        public static final String TABLE_NAME = "userStoryTable";
//        public static final String COLUMN_STORYID = "storyid";
//        public static final String COLUMN_STORYFINDATE = "storyFindate";
//        public static final Timestamp COLUMN_STORYFINTIME = Timestamp.valueOf("storyFintime");
//        public static final Integer COLUMN_STORYGAUGE = Integer.valueOf("storyGauge");
//        public static final Integer COLUMN_STORYFAV = Integer.valueOf("storyFav");
//        public static final String SQL_CREATE_TABLE =
//                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
//                        _ID + " INTEGER PRIMARY KEY," +
//                        COLUMN_USERID + " TEXT," +
//                        COLUMN_STORYID + " TEXT," +
//                        COLUMN_STORYFINDATE + " TIMESTAMP," +
//                        COLUMN_STORYFINTIME + " TIME," +
//                        COLUMN_STORYGAUGE + " INTEGER," +
//                        COLUMN_STORYFAV + " INTEGER)";
//
//        public static final String SQL_DELETE_TABLE =
//                "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
}
