package com.github.danirod12.jackal.server.game.tile;

public abstract class GameTile {

    private final TileType type;

    public GameTile(TileType type) {
        this.type = type;
    }

    public TileType getType() { return type; }

}
