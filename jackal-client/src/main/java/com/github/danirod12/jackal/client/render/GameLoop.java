package com.github.danirod12.jackal.client.render;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.MouseExecutor;
import com.github.danirod12.jackal.client.controllers.SelectableObject;
import com.github.danirod12.jackal.client.handler.ObjectsHandler;
import com.github.danirod12.jackal.client.handler.RenderLayer;
import com.github.danirod12.jackal.client.objects.AppObject;
import com.github.danirod12.jackal.client.objects.bin.FadingOvalObject;
import com.github.danirod12.jackal.client.objects.input.ChatObject;
import com.github.danirod12.jackal.client.protocol.ClientSideConnection;
import com.github.danirod12.jackal.client.protocol.packet.ServerboundLoginPacket;
import com.github.danirod12.jackal.client.util.ColorTheme;
import com.github.danirod12.jackal.client.util.Misc;
import com.github.danirod12.jackal.client.util.SimpleScheduler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameLoop implements Runnable {

    private ClientSideConnection connection = null;
    public void destroyConnection() {
        connection = null;
        handler.removeAll();
        render.createLobby(handler);
    }

    public ClientSideConnection getConnection() { return connection; }

    public boolean isInGame() { return connection != null; }

    private final double tps = 60.0D;
    private final Font system_font = new Font("Dialog", Font.PLAIN, 12);
    private final Thread thread;
    private final FrameRender render;
    private final ObjectsHandler handler;

    private final SimpleScheduler<Boolean> boolean_ticker = new SimpleScheduler<>((int)(tps / 2));
    public boolean getScheduledBoolean() { return boolean_ticker.get(); }

    private String name;
    private SelectableObject selected;

    public GameLoop(FrameRender render) {

        boolean_ticker.set(false);

        this.render = render;
        this.render.registerControllers(this);
        this.handler = new ObjectsHandler();

        thread = new Thread(this);
        thread.start();

    }

    public ObjectsHandler getObjectsHandler() { return handler; }

    public FrameRender getFrameRender() { return render; }

    public Font getSystemFont() { return system_font; }

    private int fps = 0;
    private boolean running = true;

    public int getFps() { return fps; }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double ns = 1000000000 / tps;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                try {
                    tick();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                delta--;
            }
            if(running) render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                frames = 0;
            }
        }

        stop();

    }

    private void render() {

        BufferStrategy bs = render.getBufferStrategy();
        if(bs == null) {
            render.createBufferStrategy(3);
            return;
        }

        Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.render.render(graphics);
        this.handler.getRenderObjects().forEach(n -> n.render(graphics));

        if(render.displayFps()) {
            graphics.setColor(ColorTheme.DEBUG_LIGHT);
            graphics.setFont(system_font);
            graphics.drawString("FPS: " + fps, 4, 15);
        }

        graphics.dispose();
        bs.show();

    }

    private void tick() {
        if(boolean_ticker.tick()) boolean_ticker.set(!boolean_ticker.get());
        render.tick();
        handler.forEach(AppObject::tick);
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

        for(MouseExecutor executor : handler.getMouseExecutors()) {

            if(executor.onMouseClick(x, y)) {
                if(executor instanceof SelectableObject)
                    selected = (SelectableObject) executor;
                return executor;
            } else if(selected == executor) selected = null;

        }
        return null;

    }

    public void onMouseMove(int x, int y) {

        // TODO remove debug
        handler.add(RenderLayer.HIGHEST, new FadingOvalObject(x, y, 4, (int)(tps * .2D), ColorTheme.DEBUG_DARK));

        handler.getMouseExecutors().forEach(n -> n.onMouseMove(x, y));

    }

    public void onKeyTyped(KeyEvent key) {

        if(key.getKeyChar() == '\u001B' && selected != null) {
            unselectObject();
            return;
        }

        if((key.getKeyChar() == '\n' || key.getKeyChar() == 't') && selected == null) {

            ChatObject chat = handler.getChat();
            if(chat != null) {
                selected = chat;
                chat.open();
                return;
            }

        }

        handler.getKeyboardExecutors().forEach(n -> n.onKeyTyped(key));

    }

    public SelectableObject getSelectedObject() { return selected; }

    public int getTPS() { return (int) tps; }

    public void connect(String name, String server) {

        if(connection != null) connection.close();

        if(name.length() < 3 || name.length() > 16 || !Misc.PATTERN.matcher(name).matches()) {
            System.out.println("incorrect name");
            // TODO incorrect name
            return;
        }

        this.name = name;

        String[] data = server.split(":");

        int port = -1;
        try {
            port = Integer.parseInt(data[1]);
        } catch (Exception exception) {}
        if(port < 0) port = Jackal.DEFAULT_PORT;

        System.out.println("Connecting to \"" + data[0] + ":" + port + "\" using name \"" + this.name + "\"...");

        try {

            Socket socket = new Socket(data[0], port);

            this.connection = new ClientSideConnection(socket);
            this.connection.sendPacket(new ServerboundLoginPacket(this.name));

        } catch (UnknownHostException exception) {
            // TODO server not found
            exception.printStackTrace();
            return;
        } catch(Exception exception) {
            // TODO other exception
            exception.printStackTrace();
            return;
        }

        getObjectsHandler().removeAll();
        render.createChat(getObjectsHandler());

    }

    public void unselectObject() {
        if(selected instanceof ChatObject)
            ((ChatObject)selected).close(false, false);
        selected = null;
    }

}
