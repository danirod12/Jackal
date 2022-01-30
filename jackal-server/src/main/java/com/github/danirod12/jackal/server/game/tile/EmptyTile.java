package com.github.danirod12.jackal.server.game.tile;

public class EmptyTile extends GameTile {

    public EmptyTile(TileType style) {

        super(style);

        if(!style.isTerrain()) throw new IllegalArgumentException(style.name() + " not a terrain");

    }

}
