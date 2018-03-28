package com.infinitystudios.wordcloud.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.infinitystudios.wordcloud.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordCloudView extends WebView implements View.OnTouchListener {

    public interface WordCloudListener {
        void onPageFinished(WebView view);

        void onTouch();
    }

    private WordCloudListener mListener;

    public void setListener(WordCloudListener listener) {
        this.mListener = listener;
    }

    public WordCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setJavaScriptEnabled(true);
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.setOnTouchListener(this);

        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mListener != null) {
                    mListener.onPageFinished(view);
                } else {
                    Log.e(getClass().getSimpleName(), "WordCloudListener must not be null");
                }
            }
        });

        this.loadUrl("file:///android_asset/index.html");


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    public void updateData(String data) {
        data = Helper.replaceSpecialCharacters(data);
        String[] split = data.split(" ");

        Map<String, Integer> map = Helper.getMapDataWithUniqueKey(split);

        try {
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry entry : map.entrySet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text", entry.getKey());
                jsonObject.put("size", (int) entry.getValue());
                jsonArray.put(jsonObject);
            }
            this.loadUrl("javascript:load(" + jsonArray + ")");
        } catch (JSONException e) {

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mListener != null) {
                mListener.onTouch();
            } else {
                Log.e(getClass().getSimpleName(), "WordCloudListener must not be null");
            }
        }
        return (event.getAction() == MotionEvent.ACTION_MOVE);
    }
}
