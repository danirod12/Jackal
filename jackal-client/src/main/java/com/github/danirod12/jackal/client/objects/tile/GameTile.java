package com.github.danirod12.jackal.client.objects.tile;

import java.awt.image.BufferedImage;

public abstract class GameTile {

    private final TileType type;
    private BufferedImage image;

    public GameTile(TileType type) {
        this.type = type;
    }

    public void setTexture(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getTexture() {
        return image;
    }

    public TileType getTileType() {
        return type;
    }

}
