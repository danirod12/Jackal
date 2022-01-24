package com.github.danirod12.jackal.client.game;

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

    public Color asRenderColor() {
        return color;
    }

}
