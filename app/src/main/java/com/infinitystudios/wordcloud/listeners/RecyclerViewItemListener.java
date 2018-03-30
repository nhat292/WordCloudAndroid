package com.infinitystudios.wordcloud.listeners;

/**
 * Created by Nhat on 3/27/18.
 */

public interface RecyclerViewItemListener {

    int TYPE_ITEM = 0;
    int TYPE_CHECKED = 1;
    int TYPE_VIEW = 2;
    int TYPE_EDIT = 3;
    int TYPE_DELETE = 4;

    void onClick(int position, int type);
}
