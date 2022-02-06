package com.github.danirod12.jackal.server.game.tile;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.game.item.GameObject;
import com.github.danirod12.jackal.server.game.item.TeamBoat;
import com.github.danirod12.jackal.server.game.move.MoveDirection;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundGameObjectPacket;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundTileMetadataPacket;
import com.github.danirod12.jackal.server.util.GameColor;

import java.util.ArrayList;
import java.util.List;

public abstract class GameTile {

    private final TileType type;
    private final List<GameObject> items = new ArrayList<>();
    private final int id;

    /*

    0 - void tile
    1 - empty tile
    2 - arrow tile

     */

    public GameTile(TileType type, int id) {
        this.type = type;
        this.id = id;
    }

    /**
     * Build tile metadata packet. Send it when tile becomes open
     */
    public ClientboundTileMetadataPacket getMetadataPacket(int y, int x) {
        return new ClientboundTileMetadataPacket(y, x, id, getMetadata());
    }

    /**
     * Get metadata
     */
    public abstract String getMetadata();

    /**
     * Tile render type
     */
    public TileType getType() { return type; }

    /**
     * Tile items (Coins, players, boats, etc.)
     */
    public List<GameObject> getItems() {
        return items;
    }

    /**
     * Get redirections (Ex. Arrows)
     */
    public abstract MoveDirection[] getRedirections();

    /**
     * Check if tile accessible from colored team (Stronghold, boat, etc.)
     */
    public abstract boolean isAccessible(GameColor color);

    /**
     * Connect item to a tile
     */
    public void addItem(GameObject item) {
        if(item instanceof TeamBoat && !(this instanceof VoidTile))
            throw new UnsupportedOperationException();
        items.add(item);
    }

    /**
     * Disconnect item from tile
     */
    public void removeItem(GameObject item) {
        items.remove(item);
    }

}
