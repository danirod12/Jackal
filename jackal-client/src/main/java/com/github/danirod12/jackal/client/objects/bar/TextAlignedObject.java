package com.github.danirod12.jackal.client.objects.bar;

import com.github.danirod12.jackal.client.util.Misc;
import com.github.danirod12.jackal.client.util.Pair;

import java.awt.*;

public class TextAlignedObject extends TextObject {

    private final boolean alignX, alignY;
    private int string_width;
    private int string_height = -1;

    public TextAlignedObject(int x, int y, boolean alignX, boolean alignY, String text, Font font, Color color) {
        super(x, y, text, font, color);
        this.alignX = alignX;
        this.alignY = alignY;
    }

    public int getStringWidth() {
        return string_width * 2;
    }

    public int getStringHeight() {
        return string_height * 2;
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(super.getColor());
        graphics.setFont(super.getFont());

        if (string_height < 0) {

            Pair<Integer, Integer> pair = Misc.getStringParams(super.getString(), super.getFont(), graphics);
            string_width = pair.getKey() / 2;
            string_height = pair.getValue() / 2;

        }

        graphics.drawString(super.getString(), alignX ? getX() - string_width : getX(), alignY ? getY() - string_height : getY());

    }

}
