package com.github.danirod12.jackal.server.protocol.packet;

public interface Packet {

    /*

    0 - disconnect
    1 - add player
    2 - chat
    3 - remove player
    4 - metadata player

     */

    /**
     * Build packet data for socket
     * @return packet data for client
     */
    String build();

}
