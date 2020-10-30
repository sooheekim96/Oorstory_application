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

        public static final String SQL_CREATE_TABLE2 =
                "create table if not exists "+ Model.COMMENTTABLE+
                        "(_no integer primary key autoincrement,"+
                        "id text,"+
                        "content text,"+
                        "date text);";

        public static final String SQL_DELETE_TABLE2 =
                "DROP TABLE IF EXISTS "+Model.COMMENTTABLE;

        public static final String SQL_CREATE_TABLE3 =
                "create table if not exists "+ Model.PLACETABLE+
                        "(_no integer primary key autoincrement,"+
                        "placename text,"+
                        "placeimage text,"+
                        "placeflow text,"+
                        "latitude float,"+
                        "longitude float,"+
                        "missionname text,"+
                        "missionflow text,"+
                        "missionimage text,"+
                        "fin integer"+
                        "date text"+
                        "percent integer);";

        public static final String SQL_DELETE_TABLE3 =
                "DROP TABLE IF EXISTS "+Model.PLACETABLE;

        public static final String SQL_CREATE_TABLE4 =
                "create table if not exists "+ Model.STORYTABLE+
                        "(_no integer primary key autoincrement,"+
                        "name text,"+
                        "image text,"+
                        "flow text,"+
                        "difficulty integer,"+
                        "theme text,"+
                        "time integer,"+
                        "fav integer,"+
                        "number integer,"+
                        "start integer,"+
                        "first integer,"+
                        "second integer,"+
                        "third integer,"+
                        "final integer);";

        public static final String SQL_DELETE_TABLE4 =
                "DROP TABLE IF EXISTS "+Model.STORYTABLE;

        public static final String SQL_CREATE_TABLE5 =
                "create table if not exists "+ Model.GAUGETABLE+
                        "(_no integer primary key autoincrement,"+
                        "name text,"+
                        "fin integer,"+
                        "time text,"+
                        "date text,"+
                        "percent integer);";

        public static final String SQL_DELETE_TABLE5 =
                "DROP TABLE IF EXISTS "+Model.GAUGETABLE;
    }
}
