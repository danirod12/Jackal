package com.github.danirod12.jackal.client.protocol.packet;

import java.util.UUID;

public class ServerboundSelectActionPacket extends NamedPacket {

    private final String data;

    public ServerboundSelectActionPacket(UUID uuid, String action) {
        super(11);
        this.data = uuid.toString() + ":" + action;
    }

    @Override
    public String data() {
        return data;
    }

}
