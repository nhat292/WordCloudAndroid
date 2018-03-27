package com.infinitystudios.wordcloud.listeners;

/**
 * Created by Nhat on 3/27/18.
 */

public interface RecyclerViewItemListener {

    int TYPE_ITEM = 0;
    int TYPE_DELETE = 1;
    int TYPE_EDIT = 2;

    void onClick(int position, int type);
}
