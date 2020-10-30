package com.example.oorstory.model;

public class Place {

    // 장소 인덱스
    private Integer placeId;

    // 장소 이름
    private String placeName;

    // 장소 사진
    private String placeImg;

    // 장소 설명
    private String placeFlow;

    // 장소 위도
    private Float placeLatitude;

    // 장소 경도
    private Float placeLongitude;

    // 미션 이름
    private String missionName;

    // 미션 설명
    private String missionFlow;

    // 미션 사진
    private String missionImg;

    // 장소 완료여부
    private Integer placeIsfin;

    // 장소 완료날짜
    private String placeFindate;

    // 장소 게이지
    private int placeGauge;

    // constructors
    public Place() {
    }

    public Place(String name, String img, String flow, float latitude, float longitude, String missionname, String missionflow, String missionimg, int fin, String date, int gauge) {
        this.placeName = name;
        this.placeImg = img;
        this.placeFlow = flow;
        this.placeLatitude = latitude;
        this.placeLongitude = longitude;
        this.missionName = missionname;
        this.missionFlow = missionflow;
        this.missionImg = missionimg;
        this.placeIsfin = fin;
        this.placeFindate = date;
        this.placeGauge = gauge;
    }

    public Place(int id, String name, String img, String flow, float latitude, float longitude, String missionname, String missionflow, String missionimg, int fin, String date, int gauge) {
        this.placeId = id;
        this.placeName = name;
        this.placeImg = img;
        this.placeFlow = flow;
        this.placeLatitude = latitude;
        this.placeLongitude = longitude;
        this.missionName = missionname;
        this.missionFlow = missionflow;
        this.missionImg = missionimg;
        this.placeIsfin = fin;
        this.placeFindate = date;
        this.placeGauge = gauge;
    }

    public Integer getPlaceId() {
        return this.placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceImg() {
        return this.placeImg;
    }

    public void setPlaceImg(String placeImg) {
        this.placeImg = placeImg;
    }

    public String getPlaceFlow() {
        return this.placeFlow;
    }

    public void setPlaceFlow(String placeFlow) {
        this.placeFlow = placeFlow;
    }

    public Float getPlaceLatitude() {
        return this.placeLatitude;
    }

    public void setPlaceLatitude(Float placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public Float getPlaceLongitude() {
        return this.placeLongitude;
    }

    public void setPlaceLongitude(Float placeLongitude) {
        this.placeLongitude = placeLongitude;
    }


    public String getMissionName() {
        return this.missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getMissionFlow() {
        return this.missionFlow;
    }

    public void setMissionFlow(String missionFlow) {
        this.missionFlow = missionFlow;
    }

    public String getMissionImg() {
        return this.missionImg;
    }

    public void setMissionImg(String missionImg) {
        this.missionImg = missionImg;
    }

    public int getplaceIsfin() {
        return this.placeIsfin;
    }

    public void setplaceIsfin(int placeIsfin) {
        this.placeIsfin = placeIsfin;
    }

    public String getPlaceFindate() {
        return this.placeFindate;
    }

    public void setPlaceFindate(String placeFindate) {
        this.placeFindate = placeFindate;
    }

    public int getplaceGauge() {
        return this.placeGauge;
    }

    public void setplaceGauge(int placeGauge) {
        this.placeGauge = placeGauge;
    }

}