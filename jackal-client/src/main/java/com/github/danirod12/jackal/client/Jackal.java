package com.github.danirod12.jackal.client;

import com.github.danirod12.jackal.client.render.GameLoop;
import com.github.danirod12.jackal.client.render.FrameRender;
import com.github.danirod12.jackal.client.render.ImageLoader;

public class Jackal {

    public static final int DEFAULT_PORT = 2143;

    private static Jackal instance;
    private GameLoop game_loop;

    public static void main(String[] args) {

        ImageLoader.reload();

        instance = new Jackal();
        instance.createGameLoop();

    }

    private void createGameLoop() {
        this.game_loop = new GameLoop(new FrameRender("Jackal", 1280, 720));
        this.game_loop.reset();
    }

    public static Jackal getInstance() { return instance; }
    public static GameLoop getGameLoop() { return instance.game_loop; }

}
