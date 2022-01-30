package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;
import com.github.danirod12.jackal.server.util.MetaValue;

public class ClientboundPlayerMetadataPacket extends NamedPacket {

    private final String data;

    public ClientboundPlayerMetadataPacket(MetaValue meta, ServerSideConnection connection) {
        super(4);
        this.data = meta.build(connection);
    }

    @Override
    public String data() { return data; }

}
