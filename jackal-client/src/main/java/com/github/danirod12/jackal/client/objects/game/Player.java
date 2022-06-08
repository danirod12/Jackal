package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.util.GameColor;
import com.github.danirod12.jackal.client.util.Triplet;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Player {

    private final String name;

    private GameColor color = GameColor.UNKNOWN;
    private int money = 0;
    private Triplet<Long, Long, List<UUID>> turnData;

    public Player(String name) {
        this.name = name;
    }

    public GameColor getColor() {
        return color;
    }

    public void setColor(GameColor color) {
        this.color = color;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public boolean isWaitingForMove() {
        return this.turnData != null;
    }

    public Triplet<Long, Long, List<UUID>> getTurnData() {
        return turnData;
    }

    /**
     * Update turn data
     *
     * @param turnData {@link Triplet} with arguments
     *
     *                 <p> A - Long (Cooldown), </p>
     *                 <p> B - Long (Cooldown end time), </p>
     *                 <p> C - String (Nullable, filter available actions uuid,uuid,uuid) </p>
     */
    public void setTurnData(Triplet<Long, Long, String> turnData) {

        this.turnData =
                turnData == null // If turn data is null set null
                        ?
                        null
                        :
                        // Else compile a new Triplet with filter
                        new Triplet<>(turnData.getA(), turnData.getB(),
                                turnData.getC() == null // If filter is null set null filter
                                        ?
                                        null
                                        :
                                        // Else convert String"string,string,string" to List[uuid, uuid, uuid]
                                        Stream.of(turnData.getC().split(",")).map(UUID::fromString).collect(Collectors.toList())
                        );

    }

    /**
     * Checks if it is a player turn, player have a filter and filer contains only this uuid
     */
    public boolean isOnlyFilter(UUID uuid) {
        return turnData != null && turnData.getC() != null && turnData.getC().size() == 1 && turnData.getC().contains(uuid);
    }

    /**
     * Checks if is a player turn, player have a filter and filer contains this uuid
     */
    public boolean isFilter(UUID uuid) {
        return turnData != null && turnData.getC() != null && turnData.getC().contains(uuid);
    }

}
