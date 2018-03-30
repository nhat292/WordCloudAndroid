package com.infinitystudios.wordcloud.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.DialogListener;
import com.infinitystudios.wordcloud.models.Word;
import com.infinitystudios.wordcloud.utils.Helper;
import com.infinitystudios.wordcloud.utils.SharedPreferenceData;
import com.infinitystudios.wordcloud.views.WordCloudView;

import java.util.ArrayList;

/**
 * Created by Nhat on 3/27/18.
 */

public class LandscapeActivity extends BaseActivity implements WordCloudView.WordCloudListener {

    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 200;

    // Views
    private WordCloudView wordCloudView;

    // Other variables
    private ArrayList<Word> mWords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);

        mWords = getIntent().getParcelableArrayListExtra("DATA");

        wordCloudView = findViewById(R.id.wordCloudView);
        wordCloudView.setListener(this);
    }

    @Override
    public void onPageFinished(WebView view) {
        if (mWords == null) {
            mWords = SharedPreferenceData.getWordData();
        }
        wordCloudView.updateData(mWords);
    }

    @Override
    public void onTouch() {
        showDialog(
                getString(R.string.save),
                getString(R.string.message_save_confirm),
                getString(R.string.ok),
                getString(R.string.cancel),
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

    @Override
    public void onWordClick(String word, String link) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCaptureAndSave();
            }
        }
    }

    public void refreshClick(View view) {
        wordCloudView.reload();
    }

    private void startCaptureAndSave() {
        wordCloudView.setDrawingCacheEnabled(true);
        Bitmap bitmap = wordCloudView.getDrawingCache();
        String filename = "WORDCLOUD_" + System.currentTimeMillis() + ".jpg";
        Helper.saveBitmapToFile(bitmap, filename);

        showToast(R.string.message_image_saved_to_gallery);
    }
}
