package com.projects.ahmedtarek.playforme.models;

/**
 * Created by Ahmed Tarek on 1/15/2017.
 */
public class Mood {
    private String moodName;
    private int moodIcon;

    public Mood(String moodName, int moodIcon) {
        this.moodName = moodName;
        this.moodIcon = moodIcon;
    }

    public String getMoodName() {
        return moodName;
    }

    public int getMoodIcon() {
        return moodIcon;
    }
}
