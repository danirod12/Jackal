package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.util.GameColor;

public class Player {

    private final String name;

    private GameColor color = GameColor.UNKNOWN;
    private int money = 0;

    public Player(String name) {
        this.name = name;
    }

    public GameColor getColor() {
        return color;
    }

    public int getMoney() {
        return money;
    }

    public String getName() { return name; }

}
