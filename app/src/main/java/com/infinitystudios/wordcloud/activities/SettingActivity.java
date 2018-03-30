package com.infinitystudios.wordcloud.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.enums.CloudRotation;
import com.infinitystudios.wordcloud.utils.SharedPreferenceData;

import static com.infinitystudios.wordcloud.utils.Constants.INCREASING_MIN_VALUE;
import static com.infinitystudios.wordcloud.utils.Helper.getScreenHeight;

/**
 * Created by Nhat on 3/29/18.
 */

public class SettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private static final int REQUEST_CHANGE_COLORS = 100;

    // Views
    private TextView txtTitle, txtPercent, txtDegree;
    private LinearLayout llContent;
    private SeekBar seekBarPercent, seekBarDegree;
    private RadioGroup radioGroupRotation;

    private boolean mHasChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        txtTitle = findViewById(R.id.txtTitle);
        txtPercent = findViewById(R.id.txtPercent);
        txtDegree = findViewById(R.id.txtDegree);
        llContent = findViewById(R.id.llContent);
        seekBarPercent = findViewById(R.id.seekBarPercent);
        seekBarDegree = findViewById(R.id.seekBarDegree);
        radioGroupRotation = findViewById(R.id.radioGroupRotation);
        txtDegree = findViewById(R.id.txtDegree);

        ViewTreeObserver vto = llContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                txtTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int screenHeight = getScreenHeight(SettingActivity.this);
                int contentHeight = llContent.getMeasuredHeight();
                int marginTop = (screenHeight - contentHeight - 50) / 2;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txtTitle.getLayoutParams();
                params.setMargins(0, marginTop, 0, 0);
                txtTitle.setLayoutParams(params);
            }
        });

        setup();
    }

    @Override
    public void onBackPressed() {
        if (mHasChanged) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    public void buttonClick(View view) {
        if (view.getId() == R.id.btnChangeColors) {
            startActivityForResult(new Intent(this, SettingColorsActivity.class), REQUEST_CHANGE_COLORS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CHANGE_COLORS) {
            mHasChanged = true;
        }
    }

    private void setup() {
        int p = SharedPreferenceData.getIncreasingRelation();
        txtPercent.setText(p + "%");
        seekBarPercent.setProgress(p - INCREASING_MIN_VALUE);

        final String rotation = SharedPreferenceData.getRotation();
        if (rotation.equals(CloudRotation.NO_ROTATION.getValue())) {
            hideCustom();
            radioGroupRotation.check(R.id.radioNoRotation);
        } else if (rotation.equals(CloudRotation.FREE_ROTATION.getValue())) {
            hideCustom();
            radioGroupRotation.check(R.id.radioFreeRotation);
        } else if (rotation.equals(CloudRotation.PERPENDICULAR.getValue())) {
            hideCustom();
            radioGroupRotation.check(R.id.radioSymmetry);
        } else {
            updateAndShowCustomIfNeeded(rotation);
        }

        radioGroupRotation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                mHasChanged = true;
                if (checkedId == R.id.radioNoRotation) {
                    hideCustom();
                    SharedPreferenceData.setRotation(CloudRotation.NO_ROTATION.getValue());
                } else if (checkedId == R.id.radioFreeRotation) {
                    hideCustom();
                    SharedPreferenceData.setRotation(CloudRotation.FREE_ROTATION.getValue());
                } else if (checkedId == R.id.radioSymmetry) {
                    hideCustom();
                    SharedPreferenceData.setRotation(CloudRotation.PERPENDICULAR.getValue());
                } else {
                    updateAndShowCustomIfNeeded(String.valueOf(seekBarDegree.getProgress()));
                    SharedPreferenceData.setRotation(String.valueOf(seekBarDegree.getProgress()));
                }
            }
        });

        seekBarPercent.setOnSeekBarChangeListener(this);
        seekBarDegree.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mHasChanged = true;
        if (seekBar == seekBarPercent) {
            int percent = progress + INCREASING_MIN_VALUE;
            SharedPreferenceData.setIncreasingRelation(percent);
            txtPercent.setText(percent + "%");
            mHasChanged = true;
        }
        if (seekBar == seekBarDegree) {
            updateAndShowCustomIfNeeded(String.valueOf(seekBar.getProgress()));
            SharedPreferenceData.setRotation(String.valueOf(seekBarDegree.getProgress()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void hideCustom() {
        seekBarDegree.setVisibility(View.GONE);
        txtDegree.setVisibility(View.GONE);
    }

    private void updateAndShowCustomIfNeeded(String rotation) {
        radioGroupRotation.check(R.id.radioCustom);
        if (seekBarDegree.getVisibility() == View.GONE) {
            seekBarDegree.setVisibility(View.VISIBLE);
            txtDegree.setVisibility(View.VISIBLE);
        }
        seekBarDegree.setProgress(Integer.parseInt(rotation));
        txtDegree.setText(rotation + (char) 0x00B0);
    }

}
