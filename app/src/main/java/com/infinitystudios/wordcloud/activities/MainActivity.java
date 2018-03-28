package com.infinitystudios.wordcloud.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.DialogListener;
import com.infinitystudios.wordcloud.utils.Helper;
import com.infinitystudios.wordcloud.utils.SharedPreferenceData;
import com.infinitystudios.wordcloud.views.WordCloudView;

public class MainActivity extends BaseActivity implements WordCloudView.WordCloudListener {

    private static final int REQUEST_WORD_LIST = 100;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 200;

    private WordCloudView wordCloudView;
    private EditText editText;


    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordCloudView = findViewById(R.id.wordCloudView);
        editText = findViewById(R.id.editText);

        wordCloudView.setListener(this);

        showGuideIfNeeded();

    }

    @Override
    public void onPageFinished(WebView view) {
        if (data == null) {
            data = SharedPreferenceData.getWordData();
        }
        wordCloudView.updateData(data);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_WORD_LIST) {
            this.data = data.getStringExtra("DATA");
            wordCloudView.reload();
        }
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

    public void buttonClick(View view) {
        if (view.getId() == R.id.btnWordList) { // Open word list activity
            Intent intent = new Intent(this, WordListActivity.class);
            intent.putExtra("DATA", data);
            startActivityForResult(intent, REQUEST_WORD_LIST);
        } else if (view.getId() == R.id.btnUpdate) { // Update
            String words = editText.getText().toString().trim();
            if (words.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.message_fill_words_before_update), Toast.LENGTH_LONG).show();
                return;
            }
            data = words;
            wordCloudView.reload();
            SharedPreferenceData.setWordData(data);
        } else {
            Intent intent = new Intent(this, LandscapeActivity.class);
            intent.putExtra("DATA", data);
            startActivity(intent);
        }
    }

    private void startCaptureAndSave() {
        wordCloudView.setDrawingCacheEnabled(true);
        Bitmap bitmap = wordCloudView.getDrawingCache();
        String filename = "WORDCLOUD_" + System.currentTimeMillis() + ".jpg";
        Helper.saveBitmapToFile(bitmap, filename);

        Toast.makeText(getApplicationContext(), getString(R.string.message_image_saved_to_gallery), Toast.LENGTH_LONG).show();
    }

    private void showGuideIfNeeded() {
        if (SharedPreferenceData.isShowGuideMain()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            View view = getLayoutInflater().inflate(R.layout.guide_layout, null);
            builder.setView(view);
            final AppCompatCheckBox checkBox = view.findViewById(R.id.checkboxDoNotShowAgain);

            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (checkBox.isChecked()) {
                        SharedPreferenceData.setShowGuideMain(false);
                    }
                    dialogInterface.dismiss();
                }
            });

            builder.create();
            builder.show();
        }
    }
}
