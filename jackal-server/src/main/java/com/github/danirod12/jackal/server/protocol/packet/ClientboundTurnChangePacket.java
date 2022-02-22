package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

import java.util.UUID;

public class ClientboundTurnChangePacket extends NamedPacket {

    private final String data;

    /**
     * Send turn change for players.
     * @param connection Next turn target
     * @param cooldown in ticks (1 second = 20 ticks)
     * @param cooldown_ends is optional
     * @param filter {@code unprotected} filter for actions
     */
    public ClientboundTurnChangePacket(ServerSideConnection connection, long cooldown, long cooldown_ends, UUID... filter) {
        super(40);

        String compiled = null;
        for(UUID uuid : filter)
            compiled = compiled == null ? uuid.toString() : compiled + "," + uuid.toString();

        this.data = connection.getName() + ":" + cooldown + ":"
                + (cooldown_ends < 0 ? System.currentTimeMillis() + cooldown * 50 : cooldown_ends)
                + (compiled == null ? "" : ":" + compiled);

    }

    @Override
    public String data() {
        return data;
    }

}
