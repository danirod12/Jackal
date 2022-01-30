package com.github.danirod12.jackal.client.objects.tile;

import java.awt.*;

public enum TileType {

    EMPTY(0, new Color(0, 0, 0, 0)),

    SAND(1, new Color(204, 143, 7)),
    GRASS(2, new Color(49, 128, 55)),
    ROCK(3, new Color(39, 40, 35)),

    BOAT(10, Color.RED);

    private final int id;
    private final Color debug;

    TileType(int id, Color debug) {
        this.id = id;
        this.debug = debug;
    }

    public Color getAssociatedColor() {
        return debug;
    }

    public static TileType parse(int id) {

        for(TileType type : values())
            if(type.id == id) return type;
        return null;

    }

    public static TileType parse(String s) {

        for(TileType type : values())
            if(type.name().equalsIgnoreCase(s))
                return type; return null;

    }

    public int getId() { return id; }

    public boolean isTerrain() { return this == SAND || this == GRASS || this == ROCK; }

}
