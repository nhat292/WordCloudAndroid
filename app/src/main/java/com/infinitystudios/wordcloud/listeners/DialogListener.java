package com.infinitystudios.wordcloud.listeners;

import android.content.DialogInterface;

/**
 * Created by Nhat on 3/28/18.
 */

public interface DialogListener {

    void onNegative(DialogInterface dialogInterface);

    void onPositive(DialogInterface dialogInterface);
}
