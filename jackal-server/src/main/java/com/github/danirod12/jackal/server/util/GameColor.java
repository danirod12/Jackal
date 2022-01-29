package com.github.danirod12.jackal.server.util;

public enum GameColor {

    UNKNOWN('0'),

    WHITE('f'),
    RED('c'),
    GREEN('a'),
    YELLOW('e');

    private final char colorCode;

    GameColor(char color) {
        colorCode = color;
    }

    public char getColorCode() {
        return colorCode;
    }
}
