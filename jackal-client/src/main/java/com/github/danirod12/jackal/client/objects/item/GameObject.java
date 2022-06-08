package com.github.danirod12.jackal.client.objects.item;

import java.util.UUID;

public abstract class GameObject {

    private final UUID uuid;
    private int y;
    private int x;

    /*

    0 - team boat
    1 - entity player

     */

    public GameObject(UUID uuid, int y, int x) {
        this.uuid = uuid;
        this.y = y;
        this.x = x;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

}
