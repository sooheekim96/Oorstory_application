package com.example.oorstory;

import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private Drawable title_story_iv, is_starred, diff1, diff2, diff3, diff4, diff5;
    private String title_story, theme_story;
    int star_num, time_story;
    boolean isStarred;

    public int getStar_num() {
        return star_num;
    }

    public void setStar_num(int star_num) {
        this.star_num = star_num;
    }

    public boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean starred) {
        isStarred = starred;
    }

    public void setTitle_story_iv(Drawable title_story_iv) {
        this.title_story_iv = title_story_iv ;
    }
    public void setStarred(Drawable star) {
        is_starred = star ;
    }
    public void setDiff1(Drawable diff1) {
        this.diff1 = diff1 ;
    }
    public void setDiff2(Drawable diff2) {
        this.diff2 = diff2 ;
    }
    public void setDiff3(Drawable diff3) {
        this.diff3 = diff3 ;
    }
    public void setDiff4(Drawable diff4) {
        this.diff4 = diff4 ;
    }
    public void setDiff5(Drawable diff5) {
        this.diff5 = diff5 ;
    }
    public void setTitle(String title) {
        title_story = title ;
    }
    public void setTheme(String theme) {
        theme_story = theme ;
    }
    public void setTime(int time) {
        time_story = time ;
    }


    public Drawable getTitle_story_iv() {
        return title_story_iv;
    }

    public Drawable getIs_starred() {
        return is_starred;
    }

    public Drawable getDiff1() {
        return diff1;
    }

    public Drawable getDiff2() {
        return diff2;
    }

    public Drawable getDiff3() {
        return diff3;
    }

    public Drawable getDiff4() {
        return diff4;
    }

    public Drawable getDiff5() {
        return diff5;
    }

    public String getTitle_story() {
        return title_story;
    }

    public String getTheme_story() {
        return theme_story;
    }

    public int getTime_story() {
        return time_story;
    }
}
