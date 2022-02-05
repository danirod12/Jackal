package com.github.danirod12.jackal.client.objects.item;

import com.github.danirod12.jackal.client.util.GameColor;

import java.util.UUID;

public class TeamBoat extends GameObject {

    private final GameColor color;

    public TeamBoat(UUID uuid, GameColor color, int y, int x) {

        super(uuid, y, x);
        this.color = color;

    }

    public GameColor getColor() {
        return color;
    }

}
