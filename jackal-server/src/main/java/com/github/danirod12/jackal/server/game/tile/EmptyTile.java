package com.github.danirod12.jackal.server.game.tile;

import com.github.danirod12.jackal.server.game.move.MoveDirection;
import com.github.danirod12.jackal.server.util.GameColor;

public class EmptyTile extends GameTile {

    public EmptyTile(TileType style) {

        super(style, 1);

        if(!style.isTerrain()) throw new IllegalArgumentException(style.name() + " not a terrain");

    }

    @Override
    public String getMetadata() {
        return null;
    }

    @Override
    public MoveDirection[] getRedirections() {
        return new MoveDirection[0];
    }

    @Override
    public boolean isAccessible(GameColor color) {
        return true;
    }

}
