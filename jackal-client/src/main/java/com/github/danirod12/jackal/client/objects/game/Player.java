package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.util.GameColor;
import com.github.danirod12.jackal.client.util.Triplet;

public class Player {

    private final String name;

    private GameColor color = GameColor.UNKNOWN;
    private int money = 0;
    private boolean isWaitingForMove = false;
    private Triplet<String, Integer, Long> moveData;

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
        return this.isWaitingForMove;
    }

    public void setWaitingForMove(boolean waitingForMove) {
        this.isWaitingForMove = waitingForMove;
    }

    public void setMoveData(Triplet<String, Integer, Long> moveData) {
        this.moveData = moveData;
    }

    public Triplet<String, Integer, Long> getMoveData() {
        return moveData;
    }
}
