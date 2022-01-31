package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;
import com.github.danirod12.jackal.server.util.MetaValue;

public class ClientboundPlayerMetadataPacket extends NamedPacket {

    private final String data;

    /**
     * Update clientside player data by name from connection
     * <p>
     * If player was not added to client this packet will be ignored
     */
    public ClientboundPlayerMetadataPacket(MetaValue meta, ServerSideConnection connection) {
        super(4);
        this.data = meta.build(connection);
    }

    @Override
    public String data() { return data; }

}
