package com.github.danirod12.jackal.client.objects.input;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.KeyboardExecutor;
import com.github.danirod12.jackal.client.controllers.MouseExecutor;
import com.github.danirod12.jackal.client.controllers.SelectableObject;
import com.github.danirod12.jackal.client.handler.ObjectsHandler;
import com.github.danirod12.jackal.client.handler.RenderLayer;
import com.github.danirod12.jackal.client.objects.RenderObject;
import com.github.danirod12.jackal.client.render.GameLoop;
import com.github.danirod12.jackal.client.util.Misc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TextInputBlobObject extends RenderObject implements MouseExecutor, KeyboardExecutor, SelectableObject {

    private final GameLoop loop = Jackal.getGameLoop();
    private final ObjectsHandler handler = Jackal.getGameLoop().getObjectsHandler();

    private final int width, height, arc, limit;
    private final Color main, bound, selected, selected_bound;
    private final Consumer<String> action;
    private final Font font;

    private final String default_text;

    private boolean moved = false;
    private int text_offset = -1;

    private String typed = "";

    public String getValue() { return typed; }

    public TextInputBlobObject(int x, int y, int width, int height, int arc, Color main, Color bound, Color selected,
                               Color selected_bound, Font font, int limit, String default_text, Consumer<String> accept) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.arc = arc;
        this.limit = limit;
        this.main = main;
        this.bound = bound;
        this.selected = selected == null ? main : selected;
        this.selected_bound = selected_bound;
        this.action = accept;
        this.font = font;
        this.default_text = default_text;

    }

    @Override
    public boolean onMouseClick(int x, int y) {
        return Misc.isInsideRoundedRect(x - getX(), y - getY(), width, height, arc / 2);
    }

    @Override
    public boolean onMouseMove(int x, int y) {
        return moved = Misc.isInsideRoundedRect(x - getX(), y - getY(), width, height, arc / 2);
    }

    @Override
    public void tick() { }

    @Override
    public void render(Graphics2D graphics) {

        graphics.setColor(Jackal.getGameLoop().getSelectedObject() == this ? selected : main);
        graphics.fillRoundRect(getX(), getY(), width, height, arc, arc);

        graphics.setColor(moved || Jackal.getGameLoop().getSelectedObject() == this ? selected_bound : bound);
        graphics.setStroke(new BasicStroke(5));
        graphics.drawRoundRect(getX(), getY(), width, height, arc, arc);
        graphics.setStroke(new BasicStroke(1));

        // Draw text

        if(text_offset < 0) {
            int text_height = (int)font.createGlyphVector(graphics.getFontRenderContext(), "A").getPixelBounds(null, 0, 0).getHeight();
            this.text_offset = (height + text_height) / 2;
        }

        graphics.setColor(bound);
        graphics.setFont(font);

        final boolean selected = Jackal.getGameLoop().getSelectedObject() == this;
        final String text = typed.equalsIgnoreCase("") && !selected ? default_text : typed;
        graphics.drawString(text + (selected && loop.getScheduledBoolean() ? "|" : ""),
                getX() + (width - graphics.getFontMetrics().stringWidth(text)) / 2, getY() + text_offset);

        // Draw text enter-validator
        
    }

    public void accept(boolean clear) {
        action.accept(typed);
        if(clear) typed = "";
    }

    @Override
    public void onKeyTyped(KeyEvent key) {

        if(this != Jackal.getGameLoop().getSelectedObject()) return;

        if(key.getKeyChar() == '\n') {

            if (action != null)
                action.accept(typed);

        } else if(key.getKeyChar() == '\b') {

            if (typed.length() > 0)
                typed = typed.substring(0, typed.length() - 1);

        } else if(key.getKeyChar() == '\u007F') {

            if (typed.length() > 0)
                typed = Misc.substringToSpace(typed);

        } else if (key.getKeyChar() == KeyEvent.VK_TAB) {

            List<SelectableObject> textObjects = handler.getLayerCopy(RenderLayer.LOBBY_SETTINGS).stream().filter(TextInputBlobObject.class::isInstance)
                    .map(SelectableObject.class::cast).collect(Collectors.toList());

            if (textObjects.size() <= 1) return;

            int index = textObjects.indexOf(this) + 1;
            loop.selectObject(textObjects.get(index == 0 || index == textObjects.size() ? 0 : index));

        } else {

            if (typed.length() + 1 > limit) return;
            typed += key.getKeyChar();

        }

    }
}
