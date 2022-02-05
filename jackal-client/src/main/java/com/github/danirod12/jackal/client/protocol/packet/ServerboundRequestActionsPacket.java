package com.github.danirod12.jackal.client.protocol.packet;

import java.util.UUID;

public class ServerboundRequestActionsPacket extends NamedPacket {

    private final String data;

    public ServerboundRequestActionsPacket(UUID uuid) {

        super(10);
        this.data = uuid.toString();

    }

    @Override
    public String data() {
        return data;
    }

}
