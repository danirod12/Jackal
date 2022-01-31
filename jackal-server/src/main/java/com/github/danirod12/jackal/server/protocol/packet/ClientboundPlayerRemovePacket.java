package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

public class ClientboundPlayerRemovePacket extends NamedPacket {

    private final String data;

    /**
     * Remove player data on client. Also, all metadata will be destroyed
     */
    public ClientboundPlayerRemovePacket(ServerSideConnection connection) {
        super(3);
        this.data = connection.getName();
    }

    @Override
    public String data() {
        return data;
    }

}
