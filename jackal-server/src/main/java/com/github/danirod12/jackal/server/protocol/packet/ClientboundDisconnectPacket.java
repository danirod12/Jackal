package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

public class ClientboundDisconnectPacket extends NamedPacket {

    private final String reason;

    /**
     * Display client a kick message
     * <p>
     * Calling {@link ServerSideConnection#sendPacket} will close connection
     */
    public ClientboundDisconnectPacket(String reason) {
        super(0);
        this.reason = reason;
    }

    @Override
    public String data() {
        return reason;
    }

}
