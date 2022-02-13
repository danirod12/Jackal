package com.github.danirod12.jackal.client.objects.tile;

public class ClosedTile extends GameTile {

    public ClosedTile(TileType style) {

        super(style);

        if(!style.isTerrain()) throw new IllegalArgumentException(style.name() + " not terrain");

    }

}
