package com.infinitystudios.wordcloud.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.adapters.WordAdapter;
import com.infinitystudios.wordcloud.listeners.DialogListener;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.Word;
import com.infinitystudios.wordcloud.utils.SharedPreferenceData;
import com.infinitystudios.wordcloud.utils.SpacesItemDecoration;

import java.util.ArrayList;

import static com.infinitystudios.wordcloud.utils.Helper.formatColorValues;
import static com.infinitystudios.wordcloud.utils.Helper.sortWordsDescending;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordListActivity extends BaseActivity {

    // Views
    private RecyclerView recyclerViewWord;
    private RelativeLayout rlDelete;
    private TextView txtSelectCount;
    private CheckBox checkboxAll;
    private TextView txtEmpty;

    // Other variables
    private ArrayList<Word> mWords;
    private WordAdapter mWordAdapter;
    private boolean hasChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        prepareData();

        recyclerViewWord = findViewById(R.id.recyclerViewWord);
        rlDelete = findViewById(R.id.rlDelete);
        txtSelectCount = findViewById(R.id.txtSelectCount);
        checkboxAll = findViewById(R.id.checkboxAll);
        txtEmpty = findViewById(R.id.txtEmpty);

        rlDelete.setVisibility(View.GONE);

        setup();
    }

    @Override
    public void onBackPressed() {
        if (hasChanged) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("DATA", new ArrayList<>(mWords));
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    private void setup() {
        int spacingInPixels = (int) getResources().getDimension(R.dimen.word_item_spacing);
        recyclerViewWord.addItemDecoration(new SpacesItemDecoration(0, spacingInPixels, 0, 0, false));

        mWordAdapter = new WordAdapter(mWords, new RecyclerViewItemListener() {
            @Override
            public void onClick(final int position, int type) {
                if (type == TYPE_CHECKED) {
                    updateChecked(getCheckedCount());
                }

                if (type == TYPE_EDIT) {
                    showEditPopup(position);
                }
                if (type == TYPE_DELETE) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Show delete layout
                            rlDelete.setVisibility(View.VISIBLE);

                            // Notify adapter changed
                            mWordAdapter.setEnableDelete(true);
                            mWords.get(position).setChecked(true);
                            notifyChangeAllItemsDelay();

                            // Update selected count
                            updateChecked(1);
                        }
                    }, 400);
                }
            }
        });
        recyclerViewWord.setAdapter(mWordAdapter);
        showEmptyIfNeeded();
    }


    public void buttonClick(View view) {
        if (view.getId() == R.id.imgBtnDelete) {
            if (getCheckedCount() == 0) {
                showDialog(
                        null,
                        getString(R.string.message_select_at_least_one_word),
                        getString(R.string.ok),
                        null,
                        null
                );
                return;
            }
            showDialog(
                    getString(R.string.delete),
                    getString(R.string.message_delete_confirm),
                    getString(R.string.yes),
                    getString(R.string.no),
                    new DialogListener() {
                        @Override
                        public void onNegative(DialogInterface dialogInterface) {

                        }

                        @Override
                        public void onPositive(DialogInterface dialogInterface) {
                            if (checkboxAll.isChecked()) {
                                hasChanged = true;
                                mWords.clear();
                                mWordAdapter.notifyDataSetChanged();
                            } else {
                                for (int i = mWords.size() - 1; i >= 0; i--) {
                                    if (mWords.get(i).isChecked()) {
                                        mWords.remove(i);
                                        mWordAdapter.itemRemoved(i);
                                        hasChanged = true;
                                    }
                                }
                            }
                            hideDelete();
                            showEmptyIfNeeded();
                        }
                    }
            );
        }
        if (view.getId() == R.id.imgBtnCancel) {
            hideDelete();
            selectAll(false);
        }
        if (view.getId() == R.id.checkboxAll) {
            selectAll(checkboxAll.isChecked());
            notifyChangeAllItems();
            updateChecked(getCheckedCount());
        }

    }

    private void prepareData() {
        mWords = SharedPreferenceData.getWordData();
        sortWordsDescending(mWords);
    }


    private String currentColor;

    private void showEditPopup(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit));

        final View view = getLayoutInflater().inflate(R.layout.edit_popup_layout, null);
        builder.setView(view);

        final EditText editWord = view.findViewById(R.id.editWord);
        final EditText editNumber = view.findViewById(R.id.editNumber);
        final SeekBar seekBarRed = view.findViewById(R.id.seekBarRed);
        final SeekBar seekBarGreen = view.findViewById(R.id.seekBarGreen);
        final SeekBar seekBarBlue = view.findViewById(R.id.seekBarBlue);
        final TextView txtUndefined = view.findViewById(R.id.txtUndefined);
        final View viewColor = view.findViewById(R.id.viewColor);
        final Button btnRemove = view.findViewById(R.id.btnRemove);

        btnRemove.setVisibility(View.GONE);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = null;
                txtUndefined.setVisibility(View.VISIBLE);
                viewColor.setVisibility(View.GONE);
                btnRemove.setVisibility(View.GONE);
            }
        });

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int red = seekBarRed.getProgress();
                int green = seekBarGreen.getProgress();
                int blue = seekBarBlue.getProgress();
                currentColor = formatColorValues(red, green, blue);
                if (txtUndefined.getVisibility() == View.VISIBLE) {
                    txtUndefined.setVisibility(View.GONE);
                    viewColor.setVisibility(View.VISIBLE);
                    btnRemove.setVisibility(View.VISIBLE);
                }
                viewColor.setBackgroundColor(Color.parseColor(currentColor));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        seekBarRed.setOnSeekBarChangeListener(listener);
        seekBarGreen.setOnSeekBarChangeListener(listener);
        seekBarBlue.setOnSeekBarChangeListener(listener);

        Word word = mWords.get(position);

        editWord.setText(word.getWord());
        editNumber.setText(String.valueOf(word.getNumber()));

        if (word.getColor() == null) {
            viewColor.setVisibility(View.GONE);
        } else {
            currentColor = word.getColor();
            btnRemove.setVisibility(View.VISIBLE);
            try {
                int intColor = Color.parseColor(currentColor);
                seekBarRed.setProgress(Color.red(intColor));
                seekBarGreen.setProgress(Color.green(intColor));
                seekBarBlue.setProgress(Color.blue(intColor));
                viewColor.setBackgroundColor(Color.parseColor(word.getColor()));
                txtUndefined.setVisibility(View.GONE);
            } catch (Exception e) {
                viewColor.setVisibility(View.GONE);
            }
        }

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    String word = editWord.getText().toString();
                    String number = editNumber.getText().toString();
                    if (!word.isEmpty() && !number.isEmpty()) {
                        mWords.get(position).setWord(word);
                        mWords.get(position).setColor(currentColor);
                        int n = Integer.parseInt(number);
                        boolean notifyForAll = false;
                        if (n != mWords.get(position).getNumber()) {
                            notifyForAll = true;
                        }
                        mWords.get(position).setNumber(Integer.parseInt(number));

                        if (notifyForAll) {
                            sortWordsDescending(mWords);
                            notifyChangeAllItems();
                        } else {
                            mWordAdapter.notifyItemChanged(position);
                        }
                        hasChanged = true;
                    }
                    // Save to SharedPreferences
                    SharedPreferenceData.setWordData(mWords);
                } catch (Exception e) {
                    showToast(R.string.message_number_wrong_format);
                    e.printStackTrace();
                }
            }
        });

        builder.create();
        builder.show();
        editWord.setSelection(editWord.getText().toString().length());
    }

    private int getCheckedCount() {
        int count = 0;
        for (Word w : mWords) {
            if (w.isChecked()) {
                count++;
            }
        }
        return count;
    }

    private void updateChecked(int count) {
        txtSelectCount.setText(String.format(getString(R.string.select_format), count));
    }

    private void notifyChangeAllItemsDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyChangeAllItems();
            }
        }, 100);
    }

    private void notifyChangeAllItems() {
        mWordAdapter.notifyItemRangeChanged(0, mWords.size());
    }

    private void hideDelete() {
        mWordAdapter.setEnableDelete(false);
        rlDelete.setVisibility(View.GONE);
        notifyChangeAllItemsDelay();
    }

    private void selectAll(boolean checked) {
        for (Word w : mWords) {
            w.setChecked(checked);
        }
    }

    private void showEmptyIfNeeded() {
        if (mWords.size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

}
