package com.github.danirod12.jackal.client.protocol.packet;

public interface Packet {

    /*

    0 - login packet
    1 - chat packet
    10 - request actions packet
    11 - select action packet

     */

    public String build();

}
