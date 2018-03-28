package com.infinitystudios.wordcloud.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.adapters.WordAdapter;
import com.infinitystudios.wordcloud.listeners.DialogListener;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.Word;
import com.infinitystudios.wordcloud.utils.Helper;
import com.infinitystudios.wordcloud.utils.SharedPreferenceData;
import com.infinitystudios.wordcloud.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordListActivity extends BaseActivity {

    private RecyclerView recyclerViewWord;
    private RelativeLayout rlDelete;
    private TextView txtSelectCount;
    private CheckBox checkboxAll;
    private TextView txtEmpty;

    private List<Word> mItems = new ArrayList<>();
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


        int spacingInPixels = (int) getResources().getDimension(R.dimen.word_item_spacing);
        recyclerViewWord.addItemDecoration(new SpacesItemDecoration(0, spacingInPixels, 0, 0, false));

        mWordAdapter = new WordAdapter(mItems, new RecyclerViewItemListener() {
            @Override
            public void onClick(final int position, int type) {
                if (type == TYPE_CHECKED) {
                    updateChecked(getCheckedCount());
                }

                if (type == TYPE_ITEM) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WordListActivity.this);
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(WordListActivity.this, R.layout.simple_list_item);
                    arrayAdapter.add(getString(R.string.edit));
                    arrayAdapter.add(getString(R.string.delete));
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            if (which == 0) { // Edit
                                showEditPopup(position);
                            } else { // Delete
                                // Show delete layout
                                rlDelete.setVisibility(View.VISIBLE);

                                // Notify adapter changed
                                mWordAdapter.setEnableDelete(true);
                                mItems.get(position).setChecked(true);
                                notifyChangeAllItemsDelay();

                                // Update selected count
                                updateChecked(1);
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
        recyclerViewWord.setAdapter(mWordAdapter);
        showEmptyIfNeeded();
    }

    @Override
    public void onBackPressed() {
        if (hasChanged) {
            StringBuilder stringBuilder = new StringBuilder("");
            for (Word word : mItems) {
                for (int i = 0; i < word.getNumber(); i++) {
                    stringBuilder.append(word.getWord()).append(" ");
                }
            }
            String data = stringBuilder.toString();
            SharedPreferenceData.setWordData(data);
            Intent intent = new Intent();
            intent.putExtra("DATA", data);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
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
                                mItems.clear();
                                mWordAdapter.notifyDataSetChanged();
                            } else {
                                for (int i = mItems.size() - 1; i >= 0; i--) {
                                    if (mItems.get(i).isChecked()) {
                                        mItems.remove(i);
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
        String data = getIntent().getStringExtra("DATA");
        String[] split = Helper.replaceSpecialCharacters(data).split(" ");

        Map<String, Integer> map = Helper.getMapDataWithUniqueKey(split);

        for (Map.Entry entry : map.entrySet()) {
            mItems.add(new Word((String) entry.getKey(), (int) entry.getValue()));
        }

        // Sort array list
        Collections.sort(mItems, new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                if (w1.getNumber() < w2.getNumber()) {
                    return 1;
                } else if (w1.getNumber() > w2.getNumber()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    private void showEditPopup(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WordListActivity.this);
        builder.setTitle(getString(R.string.edit));

        View view = getLayoutInflater().inflate(R.layout.edittext_item, null);
        builder.setView(view);
        final EditText editWord = view.findViewById(R.id.editWord);
        final EditText editNumber = view.findViewById(R.id.editNumber);
        editWord.setText(mItems.get(position).getWord());
        editNumber.setText(String.valueOf(mItems.get(position).getNumber()));

        builder.setNegativeButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String word = editWord.getText().toString();
                String number = editNumber.getText().toString();
                if (!word.isEmpty() && !number.isEmpty()) {
                    mItems.get(position).setWord(word);
                    mItems.get(position).setNumber(Integer.parseInt(number));
                    mWordAdapter.notifyItemChanged(position);
                    hasChanged = true;
                }
                dialogInterface.dismiss();
            }
        });

        builder.create();
        builder.show();
        editWord.setSelection(editWord.getText().toString().length());
    }

    private int getCheckedCount() {
        int count = 0;
        for (Word w : mItems) {
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
                mWordAdapter.notifyItemRangeChanged(0, mItems.size() - 1);
            }
        }, 100);
    }

    private void notifyChangeAllItems() {
        mWordAdapter.notifyItemRangeChanged(0, mItems.size() - 1);
    }

    private void hideDelete() {
        mWordAdapter.setEnableDelete(false);
        notifyChangeAllItemsDelay();
        rlDelete.setVisibility(View.GONE);
    }

    private void selectAll(boolean checked) {
        for (Word w : mItems) {
            w.setChecked(checked);
        }
    }

    private void showEmptyIfNeeded() {
        if (mItems.size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

}
