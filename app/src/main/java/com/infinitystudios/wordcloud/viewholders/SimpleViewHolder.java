package com.infinitystudios.wordcloud.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;

/**
 * Created by Nhat on 3/27/18.
 */

public class SimpleViewHolder extends RecyclerView.ViewHolder {

    private TextView txtItem;

    private RecyclerViewItemListener mListener;

    public SimpleViewHolder(View itemView, RecyclerViewItemListener listener) {
        super(itemView);
        mListener = listener;
        txtItem = itemView.findViewById(R.id.txtItem);
    }

    public void bind(final String item, final int position) {
        txtItem.setText(item);
        txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClick(position, RecyclerViewItemListener.TYPE_ITEM);
                } else {
                    Log.e(getClass().getSimpleName(), "RecyclerViewItemListener must not be null");
                }
            }
        });
    }

}
