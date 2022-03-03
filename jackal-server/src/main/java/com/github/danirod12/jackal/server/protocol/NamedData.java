package com.github.danirod12.jackal.server.protocol;

public class NamedData {

    private final int id;

    private final String data;

    public int getID() { return id; }

    public String getData() { return data; }

    public NamedData(int id, String data) {
        this.id = id;
        this.data = data;
    }

}
