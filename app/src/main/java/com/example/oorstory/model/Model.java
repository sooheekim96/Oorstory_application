package com.example.oorstory.model;

import java.sql.Timestamp;

public class Model {
    public static final String DATABASE_NAME = "oorstory.db";

    // 유저정보
    public final static String USERTABLE = "user";
    public final static String USERID = "id";
    public final static String USERIMG = "image";
    public final static String EMAIL = "email";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";

    // 댓글정보
    public final static String COMMENTTABLE = "comment";
    public final static String COMMENTID = USERID;
    public final static String COMMENTCONTENT = "content";
    public final static String COMMENTDATE = "date";

    // 장소정보
    public final static String PLACETABLE = "place";
    public final static String PLACENAME = "placename";
    public final static String PLACEIMG ="placeimage";
    public final static String PLACEFLOW ="placeflow";
    public final static String LATITUDE = "latitude";
    public final static String LONGTITUDE = "longitude";
    public final static String MISSIONNAME = "missionname";
    public final static String MISSIONFLOW = "missionflow";
    public final static String MISSIONIMG = "missionimage";
    // 장소완료정보
    public final static String PLACEISFIN = "fin";
    public final static String PLACEFINDATE = "date";
    public final static String PLACEGAUGE = "percent";

    // 스토리정보
    public final static String STORYTABLE = "story";
    public final static String STORYNAME = "name";
    public final static String STORYIMG = "image";
    public final static String STORYDIFF ="difficulty";
    public final static String STORYTHEME = "theme";
    public final static String STORYTIME = "time";
    public final static String STORYFAV = "fav";
    public final static String NUMBER = "number";
    public final static String ROUTESTART = "start";
    public final static String ROUTEFIRST = "first";
    public final static String ROUTESECOND = "second";
    public final static String ROUTETHIRD = "third";
    public final static String ROUTEFINAL = "final";

    // 스토리게이지
    public final static String GAUGETABLE = "gauge";
    public final static String STORYISFIN = "fin";
    public final static String STORYFINTIME = "time";
    public final static String STORYFINDATE = "date";
    public final static String STORYGAUGE = "percent";

    // 공통사항
    public final static String INDEX = "_no";
}
