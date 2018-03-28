package com.infinitystudios.wordcloud.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.Toast;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.DialogListener;
import com.infinitystudios.wordcloud.utils.Helper;
import com.infinitystudios.wordcloud.views.WordCloudView;

/**
 * Created by Nhat on 3/27/18.
 */

public class LandscapeActivity extends BaseActivity implements WordCloudView.WordCloudListener {

    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 200;

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

    @Override
    public void onTouch() {
        showDialog(
                getString(R.string.save),
                getString(R.string.message_save_confirm),
                "Ok",
                "Cancel",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialogInterface) {

                    }

                    @Override
                    public void onPositive(DialogInterface dialogInterface) {
                        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            startCaptureAndSave();
                        } else {
                            requestPermissionsSafely(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    }
                }
        );
    }

    private void startCaptureAndSave() {
        wordCloudView.setDrawingCacheEnabled(true);
        Bitmap bitmap = wordCloudView.getDrawingCache();
        String filename = "WORDCLOUD_" + System.currentTimeMillis() + ".jpg";
        Helper.saveBitmapToFile(bitmap, filename);

        Toast.makeText(getApplicationContext(), "Image saved to phone gallery", Toast.LENGTH_LONG).show();
    }
}
