package com.github.danirod12.jackal.server.protocol.packet;

public class ClientboundChatPacket extends NamedPacket {

    private final String data;

    public ClientboundChatPacket(String message) {
        super(2);
        this.data = message;
    }

    @Override
    public String data() {
        return data;
    }

}
