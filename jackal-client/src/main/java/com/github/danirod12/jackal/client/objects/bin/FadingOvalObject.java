package com.github.danirod12.jackal.client.objects.bin;

import com.github.danirod12.jackal.client.objects.RenderObject;

import java.awt.*;

public class FadingOvalObject extends RenderObject {

    private final int total_ticks, arc;
    private final Color color;
    private int ticks;

    /**
     * @param x     X location
     * @param y     Y location
     * @param ticks Ticks to fade
     * @param arc   Oval diameter
     * @param color Render color
     */
    public FadingOvalObject(int x, int y, int arc, int ticks, Color color) {
        super(x, y);
        this.total_ticks = this.ticks = ticks;
        this.color = color;
        this.arc = arc;
    }

    @Override
    public void tick() {
        ticks--;
        if (ticks <= 0) destroy();
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(new Color(color.getRed(), color.getBlue(), color.getGreen(), (ticks * 255 / total_ticks)));
        graphics.fillOval(getX() - arc / 2, getY() - arc / 2, arc, arc);
    }

}
