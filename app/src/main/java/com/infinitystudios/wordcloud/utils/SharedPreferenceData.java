package com.infinitystudios.wordcloud.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.infinitystudios.wordcloud.App;
import com.infinitystudios.wordcloud.R;

/**
 * Created by Nhat on 3/28/18.
 */

public final class SharedPreferenceData {

    private static final String KEY_SHOW_GUIDE_MAIN = "ShowGuideMain";
    private static final String KEY_WORD_DATA = "WordData";

    private SharedPreferenceData() {

    }

    private static SharedPreferences getSharedPrefences() {
        return App.getInstance().getSharedPreferences("WordCloudPref", Context.MODE_PRIVATE);
    }


    public static boolean isShowGuideMain() {
        return getSharedPrefences().getBoolean(KEY_SHOW_GUIDE_MAIN, true);
    }

    public static void setShowGuideMain(boolean isShow) {
        getSharedPrefences().edit().putBoolean(KEY_SHOW_GUIDE_MAIN, isShow).apply();
    }

    public static String getWordData() {
        return getSharedPrefences().getString(KEY_WORD_DATA, App.getInstance().getString(R.string.dummies_data));
    }

    public static void setWordData(String data) {
        getSharedPrefences().edit().putString(KEY_WORD_DATA, data).apply();
    }
}
