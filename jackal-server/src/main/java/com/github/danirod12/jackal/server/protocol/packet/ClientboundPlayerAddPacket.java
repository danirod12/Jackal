package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

public class ClientboundPlayerAddPacket extends NamedPacket {

    private final String name;

    public ClientboundPlayerAddPacket(ServerSideConnection connection) {
        super(1);
        if(!connection.isAuthorized()) throw new UnsupportedOperationException("Cannot broadcast nullable player");
        this.name = connection.getName();
    }

    @Override
    public String data() {
        return name;
    }

}
