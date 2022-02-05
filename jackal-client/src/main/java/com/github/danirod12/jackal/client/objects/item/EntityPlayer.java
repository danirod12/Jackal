package com.github.danirod12.jackal.client.objects.item;

import com.github.danirod12.jackal.client.util.GameColor;
import com.github.danirod12.jackal.client.util.SubTile;

import java.util.UUID;

public class EntityPlayer extends GameObject {

    private final GameColor color;
    private SubTile subTile;

    public EntityPlayer(UUID uuid, GameColor color) {
        super(uuid, 0, 0);
        this.color = color;
    }

    public GameColor getColor() {
        return color;
    }

    public SubTile getSubTile() {
        return subTile;
    }

    public void updateSubTile(SubTile subTile) {
        this.subTile = subTile;
    }

}
