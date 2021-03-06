package com.github.danirod12.jackal.client.render;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.KeyboardExecutor;
import com.github.danirod12.jackal.client.controllers.MouseExecutor;
import com.github.danirod12.jackal.client.controllers.SelectableObject;
import com.github.danirod12.jackal.client.handler.ObjectsHandler;
import com.github.danirod12.jackal.client.handler.RenderLayer;
import com.github.danirod12.jackal.client.objects.AppObject;
import com.github.danirod12.jackal.client.objects.bin.FadingOvalObject;
import com.github.danirod12.jackal.client.objects.input.ChatObject;
import com.github.danirod12.jackal.client.protocol.ClientSideConnection;
import com.github.danirod12.jackal.client.util.ColorTheme;
import com.github.danirod12.jackal.client.util.Misc;
import com.github.danirod12.jackal.client.util.SimpleScheduler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameLoop implements Runnable {

    private final double tps = 60.0D;
    private final Font system_font = new Font("Dialog", Font.PLAIN, 12);
    private final Thread thread;
    private final FrameRender render;
    private final ObjectsHandler handler;
    private final SimpleScheduler<Boolean> boolean_ticker = new SimpleScheduler<>((int) (tps / 2));
    private ClientSideConnection connection = null;
    private String name;
    private SelectableObject selected;
    private int fps = 0;
    private boolean running = true;
    private ErrFrame errLogger;
    public GameLoop(FrameRender render) {

        boolean_ticker.set(false);

        this.render = render;
        this.render.registerControllers(this);
        this.handler = new ObjectsHandler();

        thread = new Thread(this);
        thread.start();

    }

    public void destroyConnection() {
        connection = null;
        handler.removeAll();
        render.createLobby(handler);
    }

    public ClientSideConnection getConnection() {
        return connection;
    }

    public boolean isInGame() {
        return connection != null;
    }

    public boolean getScheduledBoolean() {
        return boolean_ticker.get();
    }

    public ObjectsHandler getObjectsHandler() {
        return handler;
    }

    public FrameRender getFrameRender() {
        return render;
    }

    public Font getSystemFont() {
        return system_font;
    }

    public int getFps() {
        return fps;
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double ns = 1000000000 / tps;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                frames = 0;
            }
        }

        stop();

    }

    private void render() {

        try {

            BufferStrategy bs = render.getBufferStrategy();
            if (bs == null) {
                render.createBufferStrategy(3);
                return;
            }

            Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            this.render.render(graphics);

            // Game
            if (this.connection != null && this.connection.getBoard() != null)
                this.connection.getBoard().render(graphics);

            // Foreground
            this.handler.getRenderObjects().forEach(n -> n.render(graphics));

            if (render.displayFps()) {
                graphics.setColor(ColorTheme.DEBUG_LIGHT);
                graphics.setFont(system_font);
                graphics.drawString("FPS: " + fps, 4, 15);
            }

            graphics.dispose();
            bs.show();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log(throwable);
        }

    }

    private void tick() {

        try {

            if (boolean_ticker.tick()) boolean_ticker.set(!boolean_ticker.get());
            render.tick();
            handler.forEach(AppObject::tick);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log(throwable);
        }

    }

    public synchronized void stop() {

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        running = false;

    }

    public void reset() {

        this.handler.removeAll();
        this.render.createLobby(handler);

    }

    public MouseExecutor onMouseClick(int x, int y) {

        for (MouseExecutor executor : handler.getMouseExecutors()) {

            if (executor.onMouseClick(x, y)) {
                if (executor instanceof SelectableObject)
                    selected = (SelectableObject) executor;
                return executor;
            } else if (selected == executor) selected = null;

        }
        if (connection != null && connection.getBoard() != null) {
            if (connection.getBoard().onMouseClick(x, y))
                return connection.getBoard();
        }
        return null;

    }

    public void onMouseMove(int x, int y) {

        // TODO remove debug
        handler.add(RenderLayer.HIGHEST, new FadingOvalObject(x, y, 4, (int) (tps * .2D), ColorTheme.DEBUG_DARK));

        handler.getMouseExecutors().forEach(n -> n.onMouseMove(x, y));
        if (connection != null && connection.getBoard() != null)
            connection.getBoard().onMouseMove(x, y);

    }

    public void onKeyTyped(KeyEvent key) {

        final SelectableObject current_selected = selected;

        // Modify TextInputBlobObject, add Runnable that will be performed on Enter press
//        if (key.getKeyChar() == KeyEvent.VK_ENTER && selected instanceof TextInputBlobObject) {
//            // this.connect(name, server);
//
//            // TODO make a text inputs getter
//
//            return;
//        }

        if (key.getKeyChar() == '\u001B' && selected != null) {
            unselectObject();
            return;
        }

        if ((key.getKeyChar() == '\n' || key.getKeyChar() == 't') && selected == null) {

            ChatObject chat = handler.getChat();
            if (chat != null) {
                selected = chat;
                chat.open();
                return;
            }

        }

        for (KeyboardExecutor keyboardExecutor : handler.getKeyboardExecutors()) {
            if (selected != current_selected) break;
            keyboardExecutor.onKeyTyped(key);
        }

    }

    public SelectableObject getSelectedObject() {
        return selected;
    }

    public int getTPS() {
        return (int) tps;
    }

    public void connect(String name, String server) {

        if (connection != null) connection.close();

        if (name.length() < 3 || name.length() > 16 || !Misc.PATTERN.matcher(name).matches()) {
            System.out.println("incorrect name");
            // TODO incorrect name
            // handler.add(10, );
            return;
        }

        this.name = name;

        String[] data = server.split(":");

        int port = -1;
        try {
            port = Integer.parseInt(data[1]);
        } catch (Exception ignored) {
        }
        if (port < 0) port = Jackal.DEFAULT_PORT;

        System.out.println("Connecting to \"" + data[0] + ":" + port + "\" using name \"" + this.name + "\"...");

        try {

            Socket socket = new Socket(data[0], port);

            this.connection = new ClientSideConnection(socket, this.name);

        } catch (UnknownHostException exception) {
            // TODO server not found
            exception.printStackTrace();
            log(exception);
            return;
        } catch (Throwable throwable) {
            // TODO other exception
            throwable.printStackTrace();
            log(throwable);
            return;
        }

        getObjectsHandler().removeAll();
        render.createGame(getObjectsHandler());

    }

    public void unselectObject() {
        if (selected instanceof ChatObject)
            ((ChatObject) selected).close(false, false);
        selected = null;
    }

    public void selectObject(SelectableObject object) {
        unselectObject();
        selected = object;
    }

    public void log(Throwable throwable) {

        if (errLogger == null || errLogger.isClosed()) errLogger = new ErrFrame();

        errLogger.log(throwable.getClass().getName() + " - " + throwable.getLocalizedMessage());
        int last_printed = -1;
        StackTraceElement[] elements = throwable.getStackTrace();

        for (int i = 0; i < elements.length; ++i) {

            String message = elements[i].toString();
            if (message.startsWith("com.github.danirod12.jackal")) {
                while (last_printed <= i) {
                    last_printed++;
                    errLogger.log("   at  " + elements[last_printed].toString());
                }
            }

        }
        for (int i = last_printed; i < elements.length && i < last_printed + 3; i++)
            errLogger.log("   at  " + elements[i].toString());

        if (elements.length - last_printed - 4 > 0)
            errLogger.log("      And " + (elements.length - last_printed - 4) + " more...");

    }

}
