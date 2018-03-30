package com.infinitystudios.wordcloud.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infinitystudios.wordcloud.App;
import com.infinitystudios.wordcloud.enums.CloudRotation;
import com.infinitystudios.wordcloud.models.Word;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.infinitystudios.wordcloud.utils.Constants.DEFAULT_INCREASING_PERCENT;

/**
 * Created by Nhat on 3/28/18.
 */

public final class SharedPreferenceData {

    private static final String KEY_SHOW_GUIDE_MAIN = "ShowGuideMain";
    private static final String KEY_WORD_DATA = "WordData";
    private static final String KEY_INCREASING_RELATION = "IncreasingRelation";
    private static final String KEY_ROTATION = "Rotation";

    private SharedPreferenceData() {

    }

    private static SharedPreferences getSharedPreferences() {
        return App.getInstance().getSharedPreferences("WordCloudPref", Context.MODE_PRIVATE);
    }


    public static boolean isShowGuideMain() {
        return getSharedPreferences().getBoolean(KEY_SHOW_GUIDE_MAIN, true);
    }

    public static void setShowGuideMain(boolean isShow) {
        getSharedPreferences().edit().putBoolean(KEY_SHOW_GUIDE_MAIN, isShow).apply();
    }

    public static ArrayList<Word> getWordData() {
        String json = getSharedPreferences().getString(KEY_WORD_DATA, null);
        if (json == null) {
            return Word.getDummies();
        }
        Type type = new TypeToken<ArrayList<Word>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static void setWordData(ArrayList<Word> words) {
        getSharedPreferences().edit().putString(KEY_WORD_DATA, new Gson().toJson(words)).apply();
    }

    public static int getIncreasingRelation() {
        return getSharedPreferences().getInt(KEY_INCREASING_RELATION, DEFAULT_INCREASING_PERCENT);
    }

    public static void setIncreasingRelation(int value) {
        getSharedPreferences().edit().putInt(KEY_INCREASING_RELATION, value).apply();
    }

    public static String getRotation() {
        return getSharedPreferences().getString(KEY_ROTATION, CloudRotation.FREE_ROTATION.getValue());
    }

    public static void setRotation(String rotation) {
        getSharedPreferences().edit().putString(KEY_ROTATION, rotation).apply();
    }

}
