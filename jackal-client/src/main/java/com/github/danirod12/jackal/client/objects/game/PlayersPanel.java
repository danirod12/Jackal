package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.objects.RenderObject;
import com.github.danirod12.jackal.client.render.GameLoop;
import com.github.danirod12.jackal.client.render.ImageLoader;
import com.github.danirod12.jackal.client.util.ColorTheme;
import com.github.danirod12.jackal.client.util.Misc;
import com.github.danirod12.jackal.client.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PlayersPanel extends RenderObject {

    // Current GameLoop session
    private final GameLoop loop = Jackal.getGameLoop();

    private final int BLOB_INFO = 16;

    private final int width, arc, bound_offset;
    private final Font font;

    // List URL, will be updated from another class
    private final List<Player> players;
    private Pair<Integer, Integer> text_data;

    /**
     * Generate RenderObject for players frame
     * @param players Current players list ( [!] Not copy )
     * @param x X location
     * @param y Y location
     * @param width Frame width
     * @param bound_offset Bound offsets
     * @param font Font for text
     */
    public PlayersPanel(List<Player> players, int x, int y, int width, int arc, int bound_offset, Font font) {
        super(x, y);
        this.width = width;
        this.arc = arc;
        this.bound_offset = bound_offset;
        this.font = font;
        this.players = players;
    }

    @Override
    public void tick() { }

    @Override
    public void render(Graphics2D graphics) {

        if(text_data == null) {
            text_data = Misc.getStringParams("Ag]", font, graphics);
        }

        int height = players.size() * (text_data.getValue() + bound_offset * 4 + BLOB_INFO) + bound_offset;

        graphics.setColor(ColorTheme.NOT_ACTIVATED_FRAME);

        List<Player> players = new ArrayList<>(this.players);

        graphics.fillRoundRect(super.getX(), super.getY(), width, height, arc, arc);

        graphics.setColor(ColorTheme.NOT_ACTIVATED_BOUND);
        graphics.drawRoundRect(super.getX(), super.getY(), width, height, arc, arc);

        height = super.getY() + bound_offset;

        graphics.setFont(font);
        for(Player player : players) {

            graphics.setColor(ColorTheme.NOT_ACTIVATED_BOUND);
            graphics.drawRoundRect(super.getX() + bound_offset, height, width - 2 * bound_offset, text_data.getValue() + bound_offset * 3 + BLOB_INFO, arc, arc);

            height += text_data.getValue();

            graphics.setColor(player.getColor().asRenderColor());
            graphics.drawString(player.getName(), super.getX() + (width - graphics.getFontMetrics().stringWidth(player.getName())) / 2, height);

            height += bound_offset * 2;
            graphics.drawImage(ImageLoader.COIN_16, super.getX() + width / 4, height, null);
            graphics.setColor(ColorTheme.MONEY);

            // TODO custom font                                                                     TODO center text
            graphics.drawString(String.valueOf(player.getMoney()), super.getX() + width / 4 + 20, height + 14);

            height += bound_offset * 2 + BLOB_INFO;

        }

    }

}
