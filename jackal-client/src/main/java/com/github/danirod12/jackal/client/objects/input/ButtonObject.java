package com.github.danirod12.jackal.client.objects.input;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.MouseExecutor;
import com.github.danirod12.jackal.client.objects.RenderObject;
import com.github.danirod12.jackal.client.render.GameLoop;
import com.github.danirod12.jackal.client.util.Misc;
import com.github.danirod12.jackal.client.util.Pair;

import java.awt.*;

public class ButtonObject extends RenderObject implements MouseExecutor {

    private final GameLoop loop = Jackal.getGameLoop();

    private final int width;
    private final int height;
    private final int arc;
    private final Color main, bound, activated, activated_bound;
    private final String text;
    private final Font font;
    private final Runnable action;
    private Pair<Integer, Integer> alignedTextLocation = null;

    boolean selected = false;

    public ButtonObject(int x, int y, int width, int height, int arc, Color main, Color bound, Color activated, Color activated_bound,
                        String text, Font font, Runnable action) {

        super(x, y);
        this.width = width;
        this.height = height;
        this.arc = arc;
        this.main = main;
        this.bound = bound;
        this.activated = activated == null ? main : activated;
        this.activated_bound = activated_bound == null ? bound : activated_bound;
        this.text = text;
        this.font = font;
        this.action = action;

    }

    @Override
    public boolean onMouseClick(int x, int y) {
        if(Misc.isInsideRoundedRect(x - getX(), y - getY(), width, height, arc / 2)) {
            action.run();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseMove(int x, int y) {
        return selected = Misc.isInsideRoundedRect(x - getX(), y - getY(), width, height, arc / 2);
    }

    @Override
    public void tick() { }

    @Override
    public void render(Graphics2D graphics) {

        graphics.setColor(selected && loop.getScheduledBoolean() ? activated : main);
        graphics.fillRoundRect(getX(), getY(), width, height, arc, arc);

        graphics.setColor(selected && loop.getScheduledBoolean() ? activated_bound : bound);
        graphics.drawRoundRect(getX(), getY(), width, height, arc, arc);

        if(text == null) return;

        graphics.setColor(bound);
        if(alignedTextLocation == null) {

            Pair<Integer, Integer> pair = Misc.getStringParams(text, font, graphics);
            this.alignedTextLocation = new Pair<>((width - pair.getKey()) / 2 + getX(), (height + pair.getValue()) / 2 + getY());

        }

        graphics.setFont(font);
        graphics.drawString(text, alignedTextLocation.getKey(), alignedTextLocation.getValue());

    }

}
