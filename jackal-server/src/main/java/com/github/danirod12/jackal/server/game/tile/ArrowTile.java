package com.github.danirod12.jackal.server.game.tile;

import com.github.danirod12.jackal.server.game.move.MoveDirection;
import com.github.danirod12.jackal.server.util.GameColor;

public class ArrowTile extends GameTile {

    private final MoveDirection[] directions;

    public ArrowTile(TileType type, MoveDirection... directions) {
        super(type, 2);
        if(directions.length < 1) throw new IllegalArgumentException();
        this.directions = directions;
    }

    @Override
    public String getMetadata() {
        String metadata = null;
        for(MoveDirection direction : directions)
            metadata = metadata == null ? String.valueOf(direction.getId()) : metadata + "," + direction.getId();
        return metadata;
    }

    @Override
    public MoveDirection[] getRedirections() {
        return directions;
    }

    @Override
    public boolean isAccessible(GameColor color) {
        return true;
    }

}
