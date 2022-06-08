package com.github.danirod12.jackal.server.protocol.packet;

public interface Packet {

    /*

    0 - disconnect
    1 - add player
    2 - chat
    3 - remove player
    4 - metadata player

    10 - item packet

    20 - board create
    21 - tile create
    22 - tile metadata

    40 - next turn
    41 - available action

     */

    /**
     * Build packet data for socket
     *
     * @return packet data for client
     */
    String build();

}
