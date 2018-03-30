package com.infinitystudios.wordcloud.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.infinitystudios.wordcloud.App;
import com.infinitystudios.wordcloud.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nhat on 3/27/18.
 */

public class Word implements Parcelable {

    private String word;
    private int number;
    private String color;

    private boolean checked;

    public Word(String word, int number, String color) {
        this.word = word;
        this.number = number;
        this.color = color;
    }

    protected Word(Parcel in) {
        word = in.readString();
        number = in.readInt();
        color = in.readString();
        checked = in.readByte() != 0;
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public static ArrayList<Word> getDummies() {
        String[] split = App.getInstance().getString(R.string.dummies_data).split(" ");
        Map<String, Integer> map = new HashMap<>();
        for (String str : split) {
            if (str.isEmpty()) continue;
            if (map.containsKey(str)) {
                map.put(str, map.get(str) + 1);
            } else {
                map.put(str, 1);
            }
        }
        ArrayList<Word> words = new ArrayList<>();
        for (Map.Entry entry : map.entrySet()) {
            words.add(new Word((String) entry.getKey(), (int) entry.getValue(), null));
        }

        return words;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(word);
        parcel.writeInt(number);
        parcel.writeString(color);
        parcel.writeByte((byte) (checked ? 1 : 0));
    }
}
