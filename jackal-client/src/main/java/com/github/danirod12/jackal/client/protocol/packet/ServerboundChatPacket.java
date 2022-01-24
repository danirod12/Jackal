package com.github.danirod12.jackal.client.protocol.packet;

public class ServerboundChatPacket extends NamedPacket {

    private final String data;

    public ServerboundChatPacket(String message) {
        super(1);
        this.data = message;
    }

    @Override
    public String data() {
        return data;
    }

}
