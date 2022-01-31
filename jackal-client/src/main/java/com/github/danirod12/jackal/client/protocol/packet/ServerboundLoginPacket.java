package com.github.danirod12.jackal.client.protocol.packet;

public class ServerboundLoginPacket extends NamedPacket {

    private final String name;

    /**
     * Authorize client with name. This packet should be sent only one time
     * after Socket initialization.
     */
    public ServerboundLoginPacket(String name) {
        super(0);
        this.name = name;
    }

    @Override
    public String data() {
        return name;
    }

}
