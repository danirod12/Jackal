package com.github.danirod12.jackal.server.util;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

import java.util.function.Function;

public enum MetaValue {

    COLOR(0, n -> String.valueOf(n.getColor().getId())),
    MONEY(1, n -> String.valueOf(n.getMoney()));

    private final int id;
    private final Function<ServerSideConnection, String> function;

    MetaValue(int id, Function<ServerSideConnection, String> function) {
        this.id = id;
        this.function = function;
    }

    public int getId() { return id; }

    public String getData(ServerSideConnection connection) { return function.apply(connection); }

    public String build(ServerSideConnection connection) {
        return id + ":" + connection.getName() + ":" + getData(connection);
    }

}
