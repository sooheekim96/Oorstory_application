package com.example.oorstory.model;

public class Story {
    int id;
    String name;
    String img;
    int diff;
    String theme;
    int time;

    // constructors
    public Story() {
    }

    public Story(String name, String img, int diff, String theme, int time) {
        this.name = name;
        this.img = img;
        this.diff = diff;
        this.theme = theme;
        this.time = time;
    }

    public Story(int id, String name, String img, int diff, String theme, int time) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.diff = diff;
        this.theme = theme;
        this.time = time;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public void setTheme(String theme){
        this.theme = theme;
    }

    public void setTime(int time){
        this.time = time;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getImg() { return this.img; }

    public long getDiff() {
        return this.diff;
    }

    public String getTheme(){
        return this.theme;
    }

    public int getTime(){
        return this.time;
    }
}
