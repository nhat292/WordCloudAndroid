package com.infinitystudios.wordcloud;

import android.app.Application;

/**
 * Created by Nhat on 3/28/18.
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = (App) getApplicationContext();
    }

    public static App getInstance() {
        return instance;
    }
}
