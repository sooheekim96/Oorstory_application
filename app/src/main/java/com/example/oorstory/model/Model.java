package com.example.oorstory.model;

import java.sql.Timestamp;

public class Model {
    public static final String DATABASE_NAME = "oorstory.db";

    public final static String USERTABLE = "user";
    public final static String USERID = "id";
    public final static String USERIMG = "image";
    public final static String EMAIL = "email";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";
//    public final static USERID = "";
//    public final static USERID = "";

    public final static String PLACETABLE = "place";
    public final static Integer PLACEID =0;
    public final static String PLACENAME = "name";
    public final static String PLACEIMG ="image";
    public final static String PLACEFLOW ="flow";
    public final static Float LATITUDE = (float) 0;
    public final static Float LONGTITUDE = (float) 0;
    public final static Integer MISSIONID = 0;

    public final static String STORYTABLE = "story";
    public final static String STORYID = "storyid";
    public final static String STORYNAME = "name";
    public final static String STORYIMG ="image";
    public final static String STORYDIFF ="difficulty";
    public final static String STORYTHEME = "theme";
    public final static int STORYTIME = 0;
    public final static Integer NUMBER = 0;
    public final static Integer ROUTESTART = 0;
    public final static Integer ROUTEFIRST = 0;
    public final static Integer ROUTESECOND = 0;
    public final static Integer ROUTETHIRD = 0;
    public final static Integer ROUTEFINAL = 0;

    public final static Boolean PLACEISFIN = false;
    public final static int PLACEFINDATE = 0;

    public final static int STORYFINTIME = 0;
    public final static int STORYFINDATE = 0;
    public final static int STORYGAUGE = 0;
    public final static boolean STORYISFIN = false;
    public final static boolean STORYFAV = false;
    // 미션
//    private Integer missionId;
    public final static String MISSIONNAME = "name";
    public final static String MISSIONFLOW = "flow";
    public final static String MISSIONIMG = "image";
}
