package com.infinitystudios.wordcloud.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.infinitystudios.wordcloud.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nhat on 3/27/18.
 */

public final class Helper {

    private Helper() {

    }

    public static Map<String, Integer> getMapDataWithUniqueKey(String[] input) {
        Map<String, Integer> map = new HashMap<>();
        for (String str : input) {
            if (str.isEmpty()) continue;
            if (map.containsKey(str)) {
                map.put(str, map.get(str) + 1);
            } else {
                map.put(str, 1);
            }
        }
        return map;
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
}
