package com.infinitystudios.wordcloud.enums;

/**
 * Created by Nhat on 3/29/18.
 */

public enum CloudRotation {
    NO_ROTATION("NoRotation"),
    FREE_ROTATION("Free"),
    PERPENDICULAR("Perpendicular"),
    CUSTOM("Custom");

    private final String value;

    private CloudRotation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
