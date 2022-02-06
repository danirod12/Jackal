package com.github.danirod12.jackal.server.game.item;

import com.github.danirod12.jackal.server.util.GameColor;

public class PlayerEntity extends GameObject {

    private final GameColor color;

    public PlayerEntity(GameColor color) {
        super(1);
        this.color = color;
    }

    @Override
    public GameColor getColor() {
        return color;
    }

    @Override
    public String getMetadata() {
        return String.valueOf(color.getId());
    }

}
