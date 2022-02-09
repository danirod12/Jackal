package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.util.GameColor;
import com.github.danirod12.jackal.client.util.Pair;

public class Player {

    private final String name;

    private GameColor color = GameColor.UNKNOWN;
    private int money = 0;
    private Pair<Long, Long> turnData;

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

    public void setColor(GameColor color) {
        this.color = color;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isWaitingForMove() {
        return this.turnData != null;
    }

    public Pair<Long, Long> getTurnData() {
        return turnData;
    }

    public void setTurnData(Pair<Long, Long> turnData) {
        this.turnData = turnData;
    }

}
