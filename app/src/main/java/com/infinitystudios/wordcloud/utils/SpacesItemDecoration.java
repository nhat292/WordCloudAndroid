package com.infinitystudios.wordcloud.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Nhat on 12/27/17.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int top, right, bottom, left;
    private boolean grid;

    public SpacesItemDecoration(int top, int right, int bottom, int left, boolean grid) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.grid = grid;
    }

    public SpacesItemDecoration(int space, boolean grid) {
        this.top = space;
        this.right = space;
        this.bottom = space;
        this.left = space;
        this.grid = grid;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = top;
        } else {
            outRect.top = grid ? top : 0;
        }
    }
}