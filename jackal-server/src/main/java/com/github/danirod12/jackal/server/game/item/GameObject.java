package com.github.danirod12.jackal.server.game.item;

import com.github.danirod12.jackal.server.game.tile.GameTile;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundGameObjectPacket;
import com.github.danirod12.jackal.server.util.GameColor;
import com.github.danirod12.jackal.server.util.Pair;

import java.util.UUID;

public abstract class GameObject {

    private final UUID uuid = UUID.randomUUID();
    private final int id;

    public GameObject(int id) {
        this.id = id;
    }

    /*

    0 - boat item
    1 - player item

     */

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Tile connected color. Could be null
     */
    public abstract GameColor getColor();

    /**
     * Create, update or destroy item
     *
     * @param action 0 - Create/update, 1 - destroy
     */
    public ClientboundGameObjectPacket getUpdatePacket(int action, int y, int x) {
        if (action < 0 || action > 1) throw new UnsupportedOperationException();
        return new ClientboundGameObjectPacket(action, uuid.toString(), id, y, x, getMetadata());
    }

    /**
     * Build item metadata
     */
    public abstract String getMetadata();

    /**
     * Get related tile
     */
    public GameTile getRelatedTile(GameTile[][] board) {

        for (GameTile[] tiles : board)
            for (GameTile tile : tiles)
                if (tile.getItems().contains(this))
                    return tile;
        return null;

    }

    /**
     * Get related tile YX
     */
    public Pair<Integer, Integer> getRelatedTileYX(GameTile[][] board) {

        for (int y = 0; y < board.length; y++)
            for (int x = 0; x < board[y].length; ++x)
                if (board[y][x].getItems().contains(this))
                    return new Pair<>(y, x);
        return null;

    }

}
