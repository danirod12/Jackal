package com.github.danirod12.jackal.server.protocol.packet;

public class ClientboundGameObjectPacket extends NamedPacket {

    private final String data;

    /**
     * Create, update or destroy GameObject
     */
    public ClientboundGameObjectPacket(int action, String uuid, int id, int y, int x, String metadata) {
        super(10);
        this.data = action + ":" + uuid + ":" + id + ":" + y + ":" + x + ":" + (metadata == null ? "" : metadata);
    }

    @Override
    public String data() {
        return data;
    }

}
