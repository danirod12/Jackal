package com.github.danirod12.jackal.server.protocol;

import com.google.gson.annotations.SerializedName;

public class NamedData {

    @SerializedName(value = "id")
    private int id;

    @SerializedName(value = "data")
    private String data;

    public int getID() { return id; }

    public String getData() { return data; }

}
