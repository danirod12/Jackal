package com.github.danirod12.jackal.client.util;

import java.util.List;

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

        for(SubTile tile : values())
            if(!subtiles.contains(tile)) return tile;
        throw new RuntimeException("No empty tiles");

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
    
    public static SubTile parse(int id) {
        for(SubTile tile : values())
            if(tile.id == id)
                return tile; return null;
    }

}
