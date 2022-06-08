package com.github.danirod12.jackal.server.protocol.packet;

import java.util.List;
import java.util.UUID;

public class ClientboundActionsPacket extends NamedPacket {

    private final String data;

    /**
     * Send available player actions
     */
    public ClientboundActionsPacket(UUID uuid, List<String> actions) {
        super(41);

        StringBuilder data = new StringBuilder(uuid.toString());
        for (String action : actions)
            data.append(";").append(action);
        this.data = data.toString();

    }

    @Override
    public String data() {
        return data;
    }

}
