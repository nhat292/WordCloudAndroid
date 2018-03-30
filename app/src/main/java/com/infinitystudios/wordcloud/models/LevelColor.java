package com.infinitystudios.wordcloud.models;

/**
 * Created by Nhat on 3/30/18.
 */

public class LevelColor {

    private int level;
    private String color;

    public LevelColor(int level, String color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
