package com.infinitystudios.wordcloud.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.Word;
import com.infinitystudios.wordcloud.viewholders.WordViewHolder;

import java.util.List;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private List<Word> mItems;
    private RecyclerViewItemListener mListener;

    public WordAdapter(List<Word> item, RecyclerViewItemListener listener) {
        this.mItems = item;
        this.mListener = listener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.bind(mItems.get(position), mListener, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void itemRemoved(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size() - 1);
    }
}
