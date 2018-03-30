package com.infinitystudios.wordcloud.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntRange;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.infinitystudios.wordcloud.App;
import com.infinitystudios.wordcloud.models.Word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nhat on 3/27/18.
 */

public final class Helper {

    private Helper() {

    }

    public static void sortWordsDescending(List<Word> words) {
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                if (w1.getNumber() < w2.getNumber()) {
                    return 1;
                } else if (w1.getNumber() > w2.getNumber()) {
                    return -1;
                }
                return 0;
            }
        });
    }


    public static String replaceSpecialCharacters(String input) {
        return input.replaceAll("[|?*<:\\\">+\\\\[\\\\]/',.]", "");
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("Helper", "Directory not created");
        }
        return file;
    }

    public static void saveBitmapToFile(Bitmap bitmap, String fileName) {
        if (isExternalStorageWritable()) {
            File dir = getPublicAlbumStorageDir("wordcloud");
            File file = new File(dir, fileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    MediaStore.Images.Media.insertImage(App.getInstance().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("Helper", "External storage is not available");
        }
    }

    public static String formatColorValues(
            @IntRange(from = 0, to = 255) int red,
            @IntRange(from = 0, to = 255) int green,
            @IntRange(from = 0, to = 255) int blue) {
        return String.format("#%02X%02X%02X",
                assertColorValueInRange(red),
                assertColorValueInRange(green),
                assertColorValueInRange(blue)
        );
    }

    private static int assertColorValueInRange(@IntRange(from = 0, to = 255) int colorValue) {
        return ((0 <= colorValue) && (colorValue <= 255)) ? colorValue : 0;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
