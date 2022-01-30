package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.game.tile.TileType;

public class ClientboundTileCreatePacket extends NamedPacket {

    private final String data;

    /**
     * Send tile for first time
     */
    public ClientboundTileCreatePacket(int x, int y, TileType type) {
        super(21);
        this.data = x + ":" + y + ":" + type.getId();
    }

    @Override
    public String data() { return data; }

}
