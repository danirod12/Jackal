package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

public class ClientboundPlayerMetadataPacket extends NamedPacket {

    public ClientboundPlayerMetadataPacket(ServerSideConnection connection) {
        super(4);
        throw new UnsupportedOperationException("Feature not available yet");
    }

    @Override
    public String data() {
        throw new UnsupportedOperationException("Feature not available yet");
    }

}
