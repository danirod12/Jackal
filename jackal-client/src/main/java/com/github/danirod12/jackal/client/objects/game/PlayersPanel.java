package com.github.danirod12.jackal.client.objects.game;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.objects.RenderObject;
import com.github.danirod12.jackal.client.render.FrameRender;
import com.github.danirod12.jackal.client.render.GameLoop;
import com.github.danirod12.jackal.client.render.ImageLoader;
import com.github.danirod12.jackal.client.util.ColorTheme;
import com.github.danirod12.jackal.client.util.Misc;
import com.github.danirod12.jackal.client.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayersPanel extends RenderObject {

    // Current GameLoop session
    private final GameLoop loop = Jackal.getGameLoop();

    private final int arc;
    private final Font font;

    // List URL, will be updated from another class
    private final List<Player> players;
    private Pair<Integer, Integer> text_data;

    /**
     * Generate RenderObject for players frame
     *
     * @param players      Current players list ( [!] Not copy )
     * @param x            X location
     * @param y            Y location
     * @param font         Font for text
     */
    public PlayersPanel(List<Player> players, int x, int y, int arc, Font font) {
        super(x, y);
        this.arc = arc;
        this.font = font;
        this.players = players;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics2D graphics) {

        if(text_data == null) {
            text_data = Misc.getStringParams("Ag]", font, graphics);
        }

        FrameRender render = loop.getFrameRender();
        int frameCenter = render.getWidth() / 2;
        int current_x;
        int current_y = 20;
        int width = 300;
        int height = 30;

        List<Player> players = new ArrayList<>(this.players);

        graphics.setFont(font);

        current_x = players.size() % 2 == 0 ? frameCenter - 5 - width : frameCenter - width / 2;

        for (Player player : players) {

            int nameLength = graphics.getFontMetrics().stringWidth(player.getName());

            if (player.isWaitingForMove()) {

                final long cooldown = player.getTurnData().getA() * 50; // convert from ticks to ms
                int fillPercentage = Misc.percentageOf(System.currentTimeMillis() - player.getTurnData().getB() + cooldown, cooldown);

                if(fillPercentage >= 100) {

                    player.setTurnData(null);

                } else {

                    if(fillPercentage <= 0) fillPercentage = 0;

                    graphics.setColor(ColorTheme.NOT_ACTIVATED_FRAME);
                    graphics.fillRoundRect(current_x, current_y + 28, width, 6, arc, arc);

                    int calculatedWidth = Misc.percentage(width, 100 - fillPercentage);

                    graphics.setColor(Color.RED);
                    graphics.fillRoundRect(current_x, current_y + 28, calculatedWidth, 6, arc, arc);

                    graphics.setColor(ColorTheme.NOT_ACTIVATED_BOUND);
                    graphics.drawRoundRect(current_x, current_y + 28, width, 6, arc, arc);

                }

            }

            graphics.setColor(ColorTheme.NOT_ACTIVATED_FRAME);
            graphics.fillRoundRect(current_x, current_y, width, height, arc, arc);

            graphics.setColor(ColorTheme.NOT_ACTIVATED_BOUND);
            graphics.drawRoundRect(current_x, current_y, width, height, arc, arc);

            int gLineX = current_x + width - width / 8;

            graphics.drawLine(gLineX, current_y, gLineX, current_y + height);

            graphics.setColor(player.getColor().asRenderColor());
            graphics.drawString(player.getName(), current_x + (width - nameLength) / 2, current_y + 22);

            graphics.drawImage(ImageLoader.COIN_16, gLineX + 4, current_y + 8, null);
            graphics.setColor(ColorTheme.MONEY);
            graphics.drawString(String.valueOf(player.getMoney()), gLineX + 24, current_y + 23);

            if (player.isWaitingForMove())
                graphics.drawImage(ImageLoader.COGWHEEL_32, current_x, current_y, null);

            if (players.size() % 2 == 0) {
                current_x = current_x != frameCenter + 15 + width ? current_x + 10 + width : frameCenter - 15 - 2 * width;
            } else {
                current_x = current_x < frameCenter + 10 + width / 2 ? current_x + 10 + width : frameCenter - 10 - width - width / 2;
            }

        }

    }

}
