package com.github.danirod12.jackal.client.objects.input;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.KeyboardExecutor;
import com.github.danirod12.jackal.client.controllers.SelectableObject;
import com.github.danirod12.jackal.client.objects.RenderObject;
import com.github.danirod12.jackal.client.protocol.packet.ServerboundChatPacket;
import com.github.danirod12.jackal.client.render.GameLoop;
import com.github.danirod12.jackal.client.util.Misc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ChatObject extends RenderObject implements KeyboardExecutor, SelectableObject {

    private final GameLoop loop = Jackal.getGameLoop();

    private final int width;
    private final Font font;
    private List<ChatMessage> messages = new ArrayList<>();
    private List<String> queue = new ArrayList<>();

    private int height = -1;

    public ChatObject(int frame_height, int width, Font font) {
        super(0, frame_height);
        this.width = width;
        this.font = font;
    }

    private boolean input = false;
    public boolean isInput() { return input; }

    private String value = "";

    public void open() {

        input = true;

    }

    public void close(boolean send, boolean unselect) {

        input = false;

        if(value.length() > 0) {

            if(send) loop.getConnection().sendPacket(new ServerboundChatPacket(value));
            value = "";

        }
        if(unselect) loop.unselectObject();

    }

    @Override
    public void render(Graphics2D graphics) {

        // Chat height math code
        if(height < 0) height = Misc.getStringParams("A[g", font, graphics).getValue();

        // Queue perform code
        if(queue.size() > 0) {

            List<String> queue = new ArrayList<>(this.queue);
            this.queue.clear();

            for(String message : queue) {

                String element = null;
                for(String word : message.split(" ")) {
                    if(element == null) {
                        element = word;
                        while(graphics.getFontMetrics().stringWidth(element) > width) {
                            int crop = 1;
                            while(graphics.getFontMetrics().stringWidth(element.substring(0, element.length() - crop)) > width)
                                crop++;
                            messages.add(new ChatMessage(element.substring(0, crop)));
                            element = element.substring(crop);
                        }
                    } else {
                        if(graphics.getFontMetrics().stringWidth(element + " " + word) > width) {
                            messages.add(new ChatMessage(element));
                            element = word;
                        } else {
                            element += " " + word;
                        }
                    }
                }
                if(element != null && !element.isEmpty()) {
                    while(graphics.getFontMetrics().stringWidth(element) > width) {
                        int crop = 1;
                        while(graphics.getFontMetrics().stringWidth(element.substring(0, element.length() - crop)) > width)
                            crop++;
                        messages.add(new ChatMessage(element.substring(0, crop)));
                        element = element.substring(crop);
                    }
                    if(!element.isEmpty()) messages.add(new ChatMessage(element));
                }

            }

        }

        // Chat render

        graphics.setFont(font);

        if(input) {

            graphics.setColor(Color.BLACK);
            graphics.drawString(value + (loop.getScheduledBoolean() ? "|" : ""), 2, getY() - 2);

        }

        int current_y = getY() - (input ? height + 14 : 10);

        for (int i = messages.size() - 1; i >= 0; --i) {

            final ChatMessage message = messages.get(i);

            if(!message.render() && !input || current_y < 50) break;

            graphics.setColor(new Color(0, 0, 0, input ? 255 : message.getAlpha()));
            graphics.drawString(message.getMessage(), 2, current_y);

            current_y -= 2 + height;

        }

    }

    @Override
    public void tick() { }

    public void addMessage(String message) {
        queue.add(message);
    }

    @Override
    public void onKeyTyped(KeyEvent key) {

        if(!input) return;

        if(key.getKeyChar() == '\n') {
            close(true, true);
            return;
        }

        if(key.getKeyChar() == '\b') {
            if(value.length() > 0)
                value = value.substring(0, value.length() - 1);
            return;
        }

        if(key.getKeyChar() == '\u007F') {
            if(value.length() > 0)
                value = Misc.substringToSpace(value);
            return;
        }
        value += key.getKeyChar();

    }

}
