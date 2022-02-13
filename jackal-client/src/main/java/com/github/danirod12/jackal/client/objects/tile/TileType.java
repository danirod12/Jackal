package com.github.danirod12.jackal.client.objects.tile;

import com.github.danirod12.jackal.client.render.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    /**
     * Returns a texture of this tile type
     *
     * @param closed a {@link Boolean} that represents the state of the needed texture
     * @return a texture of a corresponding tile type
     */
    public BufferedImage getTexture(boolean closed) {
        BufferedImage texture;

        if (closed) {
            switch (this.getId()) {
                case 1: {texture = ImageLoader.SAND; break;}
                case 2: {texture = ImageLoader.GRASS; break;}
                case 3: {texture = ImageLoader.ROCK; break;}
                default: {texture = ImageLoader.generateImage(64, 64, Color.GRAY); break;}
            }
        } else {
            switch (this.getId()) {
                case 1: {texture = ImageLoader.OPEN_SAND; break;}
                case 2: {texture = ImageLoader.OPEN_GRASS; break;}
                case 3: {texture = ImageLoader.OPEN_ROCK; break;}
                default: {texture = ImageLoader.generateImage(64, 64, Color.LIGHT_GRAY); break;}
            }
        }
        return texture;
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
