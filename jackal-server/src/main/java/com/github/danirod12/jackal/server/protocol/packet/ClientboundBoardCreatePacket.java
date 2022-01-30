package com.github.danirod12.jackal.server.protocol.packet;

public class ClientboundBoardCreatePacket extends NamedPacket {

    private final String data;

    public ClientboundBoardCreatePacket(int width, int height) {
        super(20);
        this.data = width + ":" + height;
    }

    @Override
    public String data() { return data; }

}
