package com.github.danirod12.jackal.server.protocol.packet;

public abstract class NamedPacket implements Packet {

    private final int id;

    public NamedPacket(int id) {
        this.id = id;
    }

    @Override
    public String build() {
        return "{\"id\":\"" + id + "\",\"data\":\"" + data() + "\"}";
    }

    public abstract String data();

}
