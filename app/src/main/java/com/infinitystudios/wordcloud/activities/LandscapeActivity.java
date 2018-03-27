package com.infinitystudios.wordcloud.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.views.WordCloudView;

/**
 * Created by Nhat on 3/27/18.
 */

public class LandscapeActivity extends BaseActivity implements WordCloudView.WordCloudListener {

    private WordCloudView wordCloudView;

    private String data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);

        data = getIntent().getStringExtra("DATA");

        wordCloudView = findViewById(R.id.wordCloudView);
        wordCloudView.setListener(this);
    }

    @Override
    public void onPageFinished(WebView view) {
        if (data == null) {
            data = getString(R.string.dummies_data);
        }
        wordCloudView.updateData(data);
    }
}
