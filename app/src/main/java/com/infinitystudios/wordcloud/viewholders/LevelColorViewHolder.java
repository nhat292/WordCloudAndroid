package com.infinitystudios.wordcloud.viewholders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.LevelColor;

/**
 * Created by Nhat on 3/27/18.
 */

public class LevelColorViewHolder extends RecyclerView.ViewHolder {

    private TextView txtLevel, txtUndefined;
    private View viewColor;
    private ImageButton imgBtnView, imgBtnEdit;

    private RecyclerViewItemListener mListener;

    public LevelColorViewHolder(View itemView, RecyclerViewItemListener listener) {
        super(itemView);
        mListener = listener;
        txtLevel = itemView.findViewById(R.id.txtLevel);
        txtUndefined = itemView.findViewById(R.id.txtUndefined);
        viewColor = itemView.findViewById(R.id.viewColor);
        imgBtnView = itemView.findViewById(R.id.imgBtnView);
        imgBtnEdit = itemView.findViewById(R.id.imgBtnEdit);
    }

    public void bind(final LevelColor levelColor, final int position) {
        txtLevel.setText(String.format(itemView.getContext().getString(R.string.level_format), levelColor.getLevel()));
        if (levelColor.getColor() == null) {
            showOrHideColor(false);
        } else {
            try {
                int color = Color.parseColor(levelColor.getColor());
                viewColor.setBackgroundColor(color);
                showOrHideColor(true);
            } catch (Exception e) {
                showOrHideColor(false);
                e.printStackTrace();
            }
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if (view == imgBtnView) {
                        mListener.onClick(position, RecyclerViewItemListener.TYPE_VIEW);
                    } else {
                        mListener.onClick(position, RecyclerViewItemListener.TYPE_EDIT);
                    }
                } else {
                    Log.e(getClass().getSimpleName(), "RecyclerViewItemListener must not be null");
                }
            }
        };

        imgBtnView.setOnClickListener(listener);
        imgBtnEdit.setOnClickListener(listener);
    }


    private void showOrHideColor(boolean isShow) {
        if (isShow) {
            viewColor.setVisibility(View.VISIBLE);
            txtUndefined.setVisibility(View.GONE);
        } else {
            viewColor.setVisibility(View.GONE);
            txtUndefined.setVisibility(View.VISIBLE);
        }
    }

}
