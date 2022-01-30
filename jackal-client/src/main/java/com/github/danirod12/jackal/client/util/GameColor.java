package com.github.danirod12.jackal.client.util;

import java.awt.*;

public enum GameColor {

    UNKNOWN(Color.BLACK),

    WHITE(Color.WHITE),
    RED(Color.RED),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW);

    private final Color color;

    GameColor(Color color) {
        this.color = color;
    }

    public static GameColor parseColor(String name) {
        for(GameColor color : values())
            if(color.name().equalsIgnoreCase(name))
                return color; return GameColor.UNKNOWN;
    }

    public Color asRenderColor() {
        return color;
    }

}
