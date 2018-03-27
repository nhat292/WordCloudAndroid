package com.infinitystudios.wordcloud.utils;

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
}
