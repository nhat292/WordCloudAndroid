package com.infinitystudios.wordcloud.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.infinitystudios.wordcloud.enums.CloudRotation;
import com.infinitystudios.wordcloud.models.Word;
import com.infinitystudios.wordcloud.utils.Helper;
import com.infinitystudios.wordcloud.utils.SharedPreferenceData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static com.infinitystudios.wordcloud.utils.Helper.sortWordsDescending;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordCloudView extends WebView implements View.OnTouchListener {

    public interface WordCloudListener {

        void onPageFinished(WebView view);

        void onTouch();

        void onWordClick(String word, String link);

    }

    private WordCloudListener mListener;

    public WordCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void init() {
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setJavaScriptEnabled(true);
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.setOnTouchListener(this);
        this.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");

        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isListenerNotNull()) {
                    mListener.onPageFinished(view);
                }
            }
        });

        this.loadUrl("file:///android_asset/index.html");
    }


    public void updateData(List<Word> words) {
        sortWordsDescending(words);
        try {
            JSONArray jsonArray = new JSONArray();
            for (Word w : words) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text", Helper.replaceSpecialCharacters(w.getWord()));
                int increasingPercent = SharedPreferenceData.getIncreasingRelation();
                float number = w.getNumber();
                if (number > 1) {
                    // Calculate for 2
                    float increaseValueFor2 = increasingPercent / 100;
                    float numberFor2 = 1 + increaseValueFor2;
                    if (number == 2) {
                        number = numberFor2;
                    } else {
                        int evenNum = (int) number / 2;
                        int oddNum = (int) number % 2;
                        number = (evenNum * numberFor2) + (oddNum != 0 ? increaseValueFor2 : 0);
                    }
                }
                jsonObject.put("size", number);
                if (w.getColor() != null) {
                    jsonObject.put("color", w.getColor());
                }
                jsonArray.put(jsonObject);
            }
            String rotation = SharedPreferenceData.getRotation();
            if (rotation.equals(CloudRotation.NO_ROTATION.getValue())) {
                rotation = "360";
            } else if (rotation.equals(CloudRotation.FREE_ROTATION.getValue())) {
                rotation = "30";
            } else if (rotation.equals(CloudRotation.PERPENDICULAR.getValue())) {
                int random = new Random().nextInt(2);
                if (random == 1) {
                    rotation = "90";
                } else {
                    rotation = "270";
                }
            }
            this.loadUrl("javascript:load(" + jsonArray + ", " + rotation + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isListenerNotNull()) {
                mListener.onTouch();
            }
        }
        return (event.getAction() == MotionEvent.ACTION_MOVE);
    }

    private boolean isListenerNotNull() {
        if (mListener == null) {
            Log.e(getClass().getSimpleName(), "WordCloudListener must not be null");
        }
        return mListener != null;
    }

    public void setListener(WordCloudListener listener) {
        this.mListener = listener;
    }

    private class AndroidBridge {

        AndroidBridge() {
        }

        @JavascriptInterface
        public void onWordClick(String word, String link) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            if (isListenerNotNull()) {
                mListener.onWordClick(word, link);
            }
        }
    }
}
