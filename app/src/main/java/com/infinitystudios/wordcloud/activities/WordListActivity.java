package com.infinitystudios.wordcloud.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.adapters.WordAdapter;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.Word;
import com.infinitystudios.wordcloud.utils.Helper;

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

    private List<Word> mItems = new ArrayList<>();
    private WordAdapter mWordAdapter;
    private boolean hasChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        prepareData();

        recyclerViewWord = findViewById(R.id.recyclerViewWord);

        mWordAdapter = new WordAdapter(mItems, new RecyclerViewItemListener() {
            @Override
            public void onClick(final int position, int type) {
                if (type != TYPE_ITEM) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(WordListActivity.this);
                builder.setTitle(getString(R.string.options));
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(WordListActivity.this, R.layout.simple_list_item);
                arrayAdapter.add("Edit");
                arrayAdapter.add("Delete");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        if (which == 0) { // Edit
                            showEditPopup(position);
                        } else { // Delete
                            mItems.remove(position);
                            mWordAdapter.itemRemoved(position);
                            hasChanged = true;
                        }
                    }
                });
                builder.show();
            }
        });
        recyclerViewWord.setAdapter(mWordAdapter);
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
            Intent intent = new Intent();
            intent.putExtra("DATA", stringBuilder.toString());
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    private void prepareData() {
        String data = getIntent().getStringExtra("DATA");
        String[] split = Helper.replaceSpecialCharacters(data).split(" ");

        Map<String, Integer> map = Helper.getMapDataWithUniqueKey(split);

        for (Map.Entry entry : map.entrySet()) {
            mItems.add(new Word((String) entry.getKey(), (int) entry.getValue()));
        }


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

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
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
}
