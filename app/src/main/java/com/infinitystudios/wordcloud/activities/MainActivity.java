package com.infinitystudios.wordcloud.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.views.WordCloudView;

public class MainActivity extends BaseActivity implements WordCloudView.WordCloudListener {

    private static final int REQUEST_WORD_LIST = 100;

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

    }

    @Override
    public void onPageFinished(WebView view) {
        if (data == null) {
            data = getString(R.string.dummies_data);
        }
        wordCloudView.updateData(data);
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

    public void buttonClick(View view) {
        if (view.getId() == R.id.btnWordList) { // Open word list activity
            Intent intent = new Intent(this, WordListActivity.class);
            intent.putExtra("DATA", data);
            startActivityForResult(intent, REQUEST_WORD_LIST);
        } else if (view.getId() == R.id.btnUpdate) { // Update
            String words = editText.getText().toString().trim();
            if (words.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in before update!", Toast.LENGTH_LONG).show();
                return;
            }
            data = words;
            wordCloudView.reload();
        } else {
            Intent intent = new Intent(this, LandscapeActivity.class);
            intent.putExtra("DATA", data);
            startActivity(intent);
        }
    }
}
