package com.github.danirod12.jackal.client.render;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.KeyboardHandler;
import com.github.danirod12.jackal.client.controllers.MouseHandler;
import com.github.danirod12.jackal.client.handler.ObjectsHandler;
import com.github.danirod12.jackal.client.handler.RenderLayer;
import com.github.danirod12.jackal.client.objects.bar.TextAlignedObject;
import com.github.danirod12.jackal.client.objects.bin.ImagesCollection;
import com.github.danirod12.jackal.client.objects.game.PlayersPanel;
import com.github.danirod12.jackal.client.objects.input.ButtonObject;
import com.github.danirod12.jackal.client.objects.input.ChatObject;
import com.github.danirod12.jackal.client.objects.input.TextInputBlobObject;
import com.github.danirod12.jackal.client.util.ColorTheme;
import com.github.danirod12.jackal.client.util.Misc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FrameRender extends Canvas {

    public final static int OFFSET_NORMAL_W = 16, OFFSET_NORMAL_H = 39;

    private final JFrame frame;
    private final int width, height;
    private final Jackal instance;

    private final ImagesCollection background;

    public FrameRender(Jackal instance, String title, int w, int h) {

        this.instance = instance;

        this.width = w;
        this.height = h < 0 ? w * 9 / 12 : h;

        if(this.width < 10 || this.height < 10) throw new IllegalArgumentException("Frame is too small");
        System.out.println("Loading frame with dimension (w: " + width + ", h:" + height + ")");

        this.frame = new JFrame();
        frame.setTitle(title);

        Dimension dimension = new Dimension(width, height);
        frame.setPreferredSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setMinimumSize(dimension);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);

        BufferedImage water = ImageLoader.loadImage("water_animation");
        background = new ImagesCollection(30, true,
                ImageLoader.repeatImage(width - OFFSET_NORMAL_W, height - OFFSET_NORMAL_H,
                        ImageLoader.multiply(ImageLoader.getTile(water, 32, 32, 0, 0), 2),
                        ImageLoader.multiply(ImageLoader.getTile(water, 32, 32, 1, 0), 2),
                        ImageLoader.multiply(ImageLoader.getTile(water, 32, 32, 2, 0), 2),
                        ImageLoader.multiply(ImageLoader.getTile(water, 32, 32, 3, 0), 2),
                        ImageLoader.multiply(ImageLoader.getTile(water, 32, 32, 4, 0), 2),
                        ImageLoader.multiply(ImageLoader.getTile(water, 32, 32, 5, 0), 2)));

    }

    public void registerControllers(GameLoop loop) {

        final MouseHandler executor = new MouseHandler(loop);
        this.addMouseListener(executor);
        this.addMouseMotionListener(executor);
        this.addKeyListener(new KeyboardHandler(loop));

    }

    public int getWidth() { return width - OFFSET_NORMAL_W; }

    public int getHeight() { return height - OFFSET_NORMAL_H; }

    public int getWidth(int size) {
        return Misc.percentage(getWidth(), size);
    }

    public int getHeight(int size) {
        return Misc.percentage(getHeight(), size);
    }

    public boolean displayFps() { return true; }

    // Background
    public void render(Graphics2D graphics) {

        graphics.drawImage(background.getImage(), 0, 0, null);

    }

    public void createLobby(ObjectsHandler handler) {

        if(Jackal.getGameLoop().getConnection() != null) {
            Jackal.getGameLoop().getConnection().close();
        }

        final TextInputBlobObject name, server;

        Font small_text = new Font("TimesRoman", Font.BOLD, 30);

        handler.add(RenderLayer.LOBBY_SETTINGS, new TextAlignedObject(width / 2, height / 2 - 140, true, true, "Your ingame name",
                small_text, ColorTheme.INPUT_TEXT));

        handler.add(RenderLayer.LOBBY_SETTINGS, name = new TextInputBlobObject(width / 2 - 300, height / 2 - 135, 600, 60, 40,
                ColorTheme.NOT_ACTIVATED_FRAME, ColorTheme.NOT_ACTIVATED_BOUND, ColorTheme.ACTIVATED_FRAME, ColorTheme.ACTIVATED_BOUND,
                new Font("TimesRoman", Font.BOLD, 35), 16, "Enter name", null));

        handler.add(RenderLayer.LOBBY_SETTINGS, new TextAlignedObject(width / 2, height / 2 - 5, true, true, "Game server IP:port",
                small_text, ColorTheme.INPUT_TEXT));

        handler.add(RenderLayer.LOBBY_SETTINGS, server = new TextInputBlobObject(width / 2 - 300, height / 2, 600, 60, 40,
                ColorTheme.NOT_ACTIVATED_FRAME, ColorTheme.NOT_ACTIVATED_BOUND, ColorTheme.ACTIVATED_FRAME, ColorTheme.ACTIVATED_BOUND,
                new Font("TimesRoman", Font.BOLD, 35), 30, "Enter server IP", null));

        handler.add(RenderLayer.LOBBY_SETTINGS, new ButtonObject(width / 2 - 150, height / 2 + 150, 300, 50, 40,
                ColorTheme.NOT_ACTIVATED_BUTTON, ColorTheme.NOT_ACTIVATED_BOUND, ColorTheme.ACTIVATED_BUTTON, ColorTheme.ACTIVATED_BOUND, "Connect", small_text,
                    () -> Jackal.getGameLoop().connect(name.getValue(), server.getValue())));

    }

    public void createGame(ObjectsHandler handler) {

        handler.add(RenderLayer.CHAT, new ChatObject(getHeight(), width / 3, new Font("TimesRoman", Font.PLAIN, 30)));

        handler.add(RenderLayer.PLAYERS_PANEL, new PlayersPanel(Jackal.getGameLoop().getConnection().getPlayers(),
                getWidth() - 270, 20, 250, 20, 4, new Font("TimesRoman", Font.BOLD, 20)));

    }

    public void tick() {
        background.tick();
    }

}
