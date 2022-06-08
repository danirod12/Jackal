package com.github.danirod12.jackal.client.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SubTile {

    L1R(0, 2, 2),
    L1L(1, 34, 2),
    L2R(2, 2, 34),
    L2L(3, 34, 34);

    private final int id;
    private final int offsetX;
    private final int offsetY;

    SubTile(int id, int offsetX, int offsetY) {
        this.id = id;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public static SubTile findEmpty(List<SubTile> subtiles) {

        List<SubTile> tiles = Stream.of(values()).filter(n -> !subtiles.contains(n)).collect(Collectors.toList());
        if (tiles.size() <= 0) throw new RuntimeException("No empty tiles");
        return tiles.get(ThreadLocalRandom.current().nextInt(tiles.size()));

    }

    public static SubTile parse(int id) {
        for (SubTile tile : values())
            if (tile.id == id)
                return tile;
        return null;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getId() {
        return id;
    }

    public int getOffsetY() {
        return offsetY;
    }

}
