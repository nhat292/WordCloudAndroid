package com.infinitystudios.wordcloud.viewholders;

import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.infinitystudios.wordcloud.R;
import com.infinitystudios.wordcloud.listeners.RecyclerViewItemListener;
import com.infinitystudios.wordcloud.models.Word;

/**
 * Created by Nhat on 3/27/18.
 */

public class WordViewHolder extends RecyclerView.ViewHolder {

    private TextView txtWord, txtNumber, txtUndefined;
    private View viewColor;
    private ImageButton imgBtnEdit, imgBtnDelete;
    private AppCompatCheckBox checkboxDelete;

    private RecyclerViewItemListener mListener;

    public WordViewHolder(View itemView, RecyclerViewItemListener listener) {
        super(itemView);
        mListener = listener;
        txtWord = itemView.findViewById(R.id.txtWord);
        txtNumber = itemView.findViewById(R.id.txtNumber);
        txtUndefined = itemView.findViewById(R.id.txtUndefined);
        viewColor = itemView.findViewById(R.id.viewColor);
        checkboxDelete = itemView.findViewById(R.id.checkboxDelete);
        imgBtnEdit = itemView.findViewById(R.id.imgBtnEdit);
        imgBtnDelete = itemView.findViewById(R.id.imgBtnDelete);
    }

    public void bind(final Word word, final int position, boolean enableDelete) {
        txtWord.setText(word.getWord());
        txtNumber.setText(String.valueOf(word.getNumber()));

        if (word.getColor() == null) {
            showOrHideColor(false);
        } else {
            try {
                int color = Color.parseColor(word.getColor());
                viewColor.setBackgroundColor(color);
                showOrHideColor(true);
            } catch (Exception e) {
                showOrHideColor(false);
            }
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if (view == checkboxDelete) {
                        word.setChecked(checkboxDelete.isChecked());
                        mListener.onClick(position, RecyclerViewItemListener.TYPE_CHECKED);
                    } else if (view == imgBtnEdit) {
                        mListener.onClick(position, RecyclerViewItemListener.TYPE_EDIT);
                    } else {
                        mListener.onClick(position, RecyclerViewItemListener.TYPE_DELETE);
                    }
                } else {
                    Log.e(getClass().getSimpleName(), "RecyclerViewItemListener must not be null");
                }
            }
        };


        if (enableDelete) {
            showOrHideOptions(false);
            checkboxDelete.setVisibility(View.VISIBLE);
            checkboxDelete.setChecked(word.isChecked());
            checkboxDelete.setOnClickListener(listener);
        } else {
            showOrHideOptions(true);
            checkboxDelete.setVisibility(View.GONE);
            checkboxDelete.setChecked(false);
        }

        imgBtnEdit.setOnClickListener(listener);
        imgBtnDelete.setOnClickListener(listener);
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

    private void showOrHideOptions(boolean isShow) {
        if (isShow) {
            imgBtnDelete.setVisibility(View.VISIBLE);
            imgBtnEdit.setVisibility(View.VISIBLE);
        } else {
            imgBtnDelete.setVisibility(View.GONE);
            imgBtnEdit.setVisibility(View.GONE);
        }
    }

}
