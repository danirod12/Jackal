package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

public class ClientboundPlayerAddPacket extends NamedPacket {

    private final String name;

    /**
     * Client will generate empty Player class with name from connection
     * <p>
     * You should send {@link ClientboundPlayerMetadataPacket} to update player data
     */
    public ClientboundPlayerAddPacket(ServerSideConnection connection) {
        super(1);
        if (!connection.isAuthorized()) throw new UnsupportedOperationException("Cannot broadcast nullable player");
        this.name = connection.getName();
    }

    @Override
    public String data() {
        return name;
    }

}
