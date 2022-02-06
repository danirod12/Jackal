package com.github.danirod12.jackal.server.protocol.packet;

public class ClientboundTileMetadataPacket extends NamedPacket {

    private final String data;

    /**
     * @param id Packet ID
     */
    public ClientboundTileMetadataPacket(int y, int x, int id, String metadata) {
        super(22);
        this.data = y + ":" + x + ":" + id + ":" + (metadata == null ? "" : metadata);
    }

    @Override
    public String data() {
        return data;
    }

}
