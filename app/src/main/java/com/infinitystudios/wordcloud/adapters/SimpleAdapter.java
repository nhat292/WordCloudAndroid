package com.infinitystudios.wordcloud.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.viewholders.SimpleViewHolder;

import java.util.List;

/**
 * Created by Nhat on 3/27/18.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<String> mItems;
    private RecyclerViewItemListener mListener;

    public SimpleAdapter(List<String> item, RecyclerViewItemListener listener) {
        this.mItems = item;
        this.mListener = listener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new SimpleViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
