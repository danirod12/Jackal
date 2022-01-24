package com.github.danirod12.jackal.client.objects;

import java.awt.*;

public abstract class RenderObject implements AppObject {

    private int x, y;

    public int getX() { return x; }
    public int getY() { return y; }

    public RenderObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract void render(Graphics2D graphics);

}
