package com.infinitystudios.wordcloud.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.Word;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordViewHolder extends RecyclerView.ViewHolder {

    private TextView txtWord, txtNumber;

    public WordViewHolder(View itemView) {
        super(itemView);

        txtWord = itemView.findViewById(R.id.txtWord);
        txtNumber = itemView.findViewById(R.id.txtNumber);
    }

    public void bind(Word word, final RecyclerViewItemListener listener, final int position) {
        txtWord.setText(word.getWord());
        txtNumber.setText(String.valueOf(word.getNumber()));

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
