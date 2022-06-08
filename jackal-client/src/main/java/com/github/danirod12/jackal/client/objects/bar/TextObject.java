package com.github.danirod12.jackal.client.objects.bar;

import com.github.danirod12.jackal.client.objects.RenderObject;

import java.awt.*;

public class TextObject extends RenderObject {

    private final Font font;
    private final String string;
    private final Color color;

    public TextObject(int x, int y, String text, Font font, Color color) {
        super(x, y);
        this.font = font;
        this.string = text;
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public String getString() {
        return string;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(string, getX(), getY());
    }

}
