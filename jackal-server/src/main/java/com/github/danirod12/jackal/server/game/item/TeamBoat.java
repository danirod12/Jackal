package com.github.danirod12.jackal.server.game.item;

import com.github.danirod12.jackal.server.util.GameColor;

import java.util.ArrayList;
import java.util.List;

public class TeamBoat extends GameObject {

    private final GameColor color;
    private final List<PlayerEntity> players = new ArrayList<>();

    public TeamBoat(GameColor color) {
        super(0);
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

    /**
     * Register a new team player
     */
    public PlayerEntity registerPlayer() {
        PlayerEntity player = new PlayerEntity(color);
        players.add(player);
        return player;
    }

    /**
     * Get team players copy
     */
    public List<PlayerEntity> getPlayers() {
        return new ArrayList<>(players);
    }

}
