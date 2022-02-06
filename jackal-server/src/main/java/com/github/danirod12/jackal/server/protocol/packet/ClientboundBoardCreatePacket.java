package com.github.danirod12.jackal.server.protocol.packet;

public class ClientboundBoardCreatePacket extends NamedPacket {

    private final String data;

    /**
     * Send board for first time
     */
    public ClientboundBoardCreatePacket(int height, int width) {
        super(20);
        this.data = height + ":" + width;
    }

    @Override
    public String data() { return data; }

}
