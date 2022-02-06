package com.github.danirod12.jackal.server.game.tile;

import com.github.danirod12.jackal.server.game.item.GameObject;
import com.github.danirod12.jackal.server.game.item.TeamBoat;
import com.github.danirod12.jackal.server.game.move.MoveDirection;
import com.github.danirod12.jackal.server.util.GameColor;

public class VoidTile extends GameTile {

    public VoidTile() {
        super(TileType.EMPTY, 0);
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
        TeamBoat boat = getBoat();
        return boat == null || boat.getColor() == color;
    }

    /**
     * Get connected boat or null if there is no connected boat
     */
    public TeamBoat getBoat() {
        for (GameObject item : getItems())
            if(item instanceof TeamBoat)
                return ((TeamBoat) item);
        return null;
    }

}
