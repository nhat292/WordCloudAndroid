package com.infinitystudios.wordcloud.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.adapters.LevelColorAdapter;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.LevelColor;
import com.infinitystudios.wordcloud.models.Word;
import com.infinitystudios.wordcloud.utils.SharedPreferenceData;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.infinitystudios.wordcloud.utils.Helper.formatColorValues;

/**
 * Created by Nhat on 3/30/18.
 */

public class SettingColorsActivity extends BaseActivity implements ColorPickerDialogListener {

    private static final int DIALOG_ID = 0;

    private TextView txtEmpty;
    private RecyclerView recyclerLevelColors;

    private LevelColorAdapter mLevelColorAdapter;
    private List<LevelColor> mItems = new ArrayList<>();
    private ArrayList<Word> mWords;
    private int mSelectedPosition = 0;
    private boolean mHasChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_colors);

        prepareDate();

        txtEmpty = findViewById(R.id.txtEmpty);
        recyclerLevelColors = findViewById(R.id.recyclerLevelColors);

        setup();

    }

    @Override
    public void onBackPressed() {
        if (mHasChanged) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    private void setup() {
        mLevelColorAdapter = new LevelColorAdapter(mItems, new RecyclerViewItemListener() {
            @Override
            public void onClick(int position, int type) {
                if (type == TYPE_VIEW) {
                    StringBuilder messageBuilder = new StringBuilder();
                    Map<String, String> map = new HashMap<>();
                    for (Word w : mWords) {
                        if (w.getNumber() == mItems.get(position).getLevel()) {
                            if (!map.containsKey(w.getWord())) {
                                map.put(w.getWord(), "Any");
                                if (messageBuilder.toString().isEmpty()) {
                                    messageBuilder.append(w.getWord());
                                } else {
                                    messageBuilder.append(" | ").append(w.getWord());
                                }
                            }
                        }
                    }
                    showDialog(
                            getString(R.string.words),
                            messageBuilder.toString(),
                            "OK",
                            null,
                            null
                    );
                } else {
                    mSelectedPosition = position;
                    int selectedColor = mItems.get(position).getColor() != null ? Color.parseColor(mItems.get(position).getColor()) : Color.BLACK;
                    showColorPickerDialog(selectedColor);
                }
            }
        });
        recyclerLevelColors.setAdapter(mLevelColorAdapter);

        showEmptyIfNeeded();
    }

    private void prepareDate() {
        mWords = SharedPreferenceData.getWordData();
        SparseArray<String> map = new SparseArray<>();
        for (Word w : mWords) {
            if (map.indexOfKey(w.getNumber()) == -1) {
                map.put(w.getNumber(), w.getColor());
                mItems.add(new LevelColor(w.getNumber(), w.getColor()));
            }
        }
    }

    private void showEmptyIfNeeded() {
        if (mItems.size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void showColorPickerDialog(int selectedColor) {
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setDialogId(DIALOG_ID)
                .setColor(selectedColor)
                .show(this);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case DIALOG_ID:
                String colorString = formatColorValues(Color.red(color), Color.green(color), Color.blue(color));
                mItems.get(mSelectedPosition).setColor(colorString);
                mLevelColorAdapter.notifyItemChanged(mSelectedPosition);
                for (int i = 0; i < mWords.size(); i++) {
                    if (mWords.get(i).getNumber() == mItems.get(mSelectedPosition).getLevel()) {
                        mWords.get(i).setColor(colorString);
                    }
                }
                SharedPreferenceData.setWordData(mWords);
                mHasChanged = true;
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
