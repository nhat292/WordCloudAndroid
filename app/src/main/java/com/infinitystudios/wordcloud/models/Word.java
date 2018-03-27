package com.infinitystudios.wordcloud.models;

/**
 * Created by Nhat on 3/27/18.
 */

public class Word {

    private String word;
    private int number;

    public Word(String word, int number) {
        this.word = word;
        this.number = number;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
