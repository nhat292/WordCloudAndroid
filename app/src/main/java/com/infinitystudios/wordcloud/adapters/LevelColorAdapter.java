package com.infinitystudios.wordcloud.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.LevelColor;
import com.infinitystudios.wordcloud.viewholders.LevelColorViewHolder;

import java.util.List;

/**
 * Created by Nhat on 3/27/18.
 */

public class LevelColorAdapter extends RecyclerView.Adapter<LevelColorViewHolder> {

    private List<LevelColor> mItems;
    private RecyclerViewItemListener mListener;

    public LevelColorAdapter(List<LevelColor> item, RecyclerViewItemListener listener) {
        this.mItems = item;
        this.mListener = listener;
    }

    @Override
    public LevelColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_colors_item, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new LevelColorViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(LevelColorViewHolder holder, int position) {
        holder.bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}