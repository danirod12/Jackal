package com.github.danirod12.jackal.server.util;

public enum GameColor {

    UNKNOWN(-1, '0'),

    WHITE(0, 'f'),
    RED(1, 'c'),
    GREEN(2, 'a'),
    YELLOW(3, 'e');

    private final char colorCode;
    private final int id;

    GameColor(int id, char color) {
        this.id = id;
        colorCode = color;
    }

    public char getColorCode() {
        return colorCode;
    }

    private final static GameColor[] GAME_COLORS = new GameColor[] { WHITE, RED, GREEN, YELLOW };

    public static GameColor[] getGameColors() {
        return GAME_COLORS;
    }

    public int getId() {
        return id;
    }

}
