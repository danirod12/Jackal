package com.github.danirod12.jackal.client.handler;

public enum RenderLayer {

    NOT_RENDER(0),

    LOWEST(1),

    LOBBY_SETTINGS(10),

    CHAT(800),

    PLAYERS_PANEL(810),

    HIGHEST(1000);

    private final int layer;

    RenderLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() { return layer; }

}
