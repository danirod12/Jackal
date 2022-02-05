package com.github.danirod12.jackal.server.protocol.packet;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

public class ClientboundTurnChangePacket extends NamedPacket {

    private final String data;

    /**
     * Send turn change for players.
     * @param cooldown in ticks (1 second = 20 ticks)
     * @param cooldown_ends is optional
     */
    public ClientboundTurnChangePacket(ServerSideConnection connection, long cooldown, long cooldown_ends) {
        super(40);
        this.data = connection.getName() + ":" + cooldown + ":" + (cooldown_ends < 0 ? System.currentTimeMillis() + cooldown * 50 : cooldown_ends);
    }

    @Override
    public String data() {
        return data;
    }

}
