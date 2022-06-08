package com.github.danirod12.jackal.server.game.tile;

public enum TileType {

    EMPTY(0),

    SAND(1),
    GRASS(2),
    ROCK(3),

    BOAT(10);

    private final int id;

    TileType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isTerrain() {
        return this == SAND || this == GRASS || this == ROCK;
    }

}
