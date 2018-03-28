package com.infinitystudios.wordcloud.viewholders;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.Word;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordViewHolder extends RecyclerView.ViewHolder {

    private TextView txtWord, txtNumber;
    private LinearLayout llItem;
    private AppCompatCheckBox checkboxDelete;

    public WordViewHolder(View itemView) {
        super(itemView);

        txtWord = itemView.findViewById(R.id.txtWord);
        txtNumber = itemView.findViewById(R.id.txtNumber);
        llItem = itemView.findViewById(R.id.llItem);
        checkboxDelete = itemView.findViewById(R.id.checkboxDelete);
    }

    public void bind(final Word word, final RecyclerViewItemListener listener, final int position, boolean enableDelete) {
        txtWord.setText(word.getWord());
        txtNumber.setText(String.valueOf(word.getNumber()));

        if (enableDelete) {
            checkboxDelete.setVisibility(View.VISIBLE);
            checkboxDelete.setChecked(word.isChecked());
            checkboxDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    word.setChecked(checkboxDelete.isChecked());
                    if (listener != null) {
                        listener.onClick(position, RecyclerViewItemListener.TYPE_CHECKED);
                    } else {
                        Log.e(getClass().getSimpleName(), "RecyclerViewItemListener must not be null");
                    }
                }
            });
        } else {
            checkboxDelete.setVisibility(View.GONE);
            checkboxDelete.setChecked(false);
        }

        llItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onClick(position, RecyclerViewItemListener.TYPE_ITEM);
                } else {
                    Log.e(getClass().getSimpleName(), "RecyclerViewItemListener must not be null");
                }
                return false;
            }
        });
    }


}
