package com.github.danirod12.jackal.client.util;

import java.awt.*;

public enum GameColor {

    UNKNOWN(-1, Color.BLACK),

    WHITE(0, Color.WHITE),
    RED(1, Color.RED),
    GREEN(2, Color.GREEN),
    YELLOW(3, Color.YELLOW);

    private final Color color;
    private final int id;

    GameColor(int id, Color color) {
        this.id = id;
        this.color = color;
    }

    public static GameColor parseColor(int id) {
        for (GameColor color : values())
            if (color.id == id)
                return color;
        return GameColor.UNKNOWN;
    }

    public static GameColor parseColor(String name) {
        for (GameColor color : values())
            if (color.name().equalsIgnoreCase(name))
                return color;
        return GameColor.UNKNOWN;
    }

    public Color asRenderColor() {
        return color;
    }

}
