package com.github.danirod12.jackal.server.protocol.packet;

public class ClientboundDisconnectPacket extends NamedPacket{

    private final String reason;

    public ClientboundDisconnectPacket(String reason) {
        super(0);
        this.reason = reason;
    }

    @Override
    public String data() {
        return reason;
    }

}
