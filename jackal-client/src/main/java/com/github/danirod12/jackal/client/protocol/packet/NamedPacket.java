package com.github.danirod12.jackal.client.protocol.packet;

public abstract class NamedPacket implements Packet {

    private final int id;

    public NamedPacket(int id) {
        this.id = id;
    }

    @Override
    public String build() {
        return id + ":" + data();
    }

    public abstract String data();

}
