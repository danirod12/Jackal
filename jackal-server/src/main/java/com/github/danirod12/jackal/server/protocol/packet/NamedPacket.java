package com.github.danirod12.jackal.server.protocol.packet;

public abstract class NamedPacket implements Packet {

    private final int id;

    /**
     * @param id Packet ID
     */
    public NamedPacket(int id) {
        this.id = id;
    }

    /**
     * Build data be sent to client
     */
    @Override
    public String build() {
        return "{\"id\":\"" + id + "\",\"data\":\"" + data() + "\"}";
    }

    /**
     * Data from packet without packet index
     */
    public abstract String data();

}
