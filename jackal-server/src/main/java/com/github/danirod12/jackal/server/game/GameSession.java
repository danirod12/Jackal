package com.github.danirod12.jackal.server.game;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.game.generator.MapGenerator;
import com.github.danirod12.jackal.server.game.tile.GameTile;
import com.github.danirod12.jackal.server.protocol.ServerSideConnection;
import com.github.danirod12.jackal.server.protocol.packet.*;
import com.github.danirod12.jackal.server.util.GameColor;
import com.github.danirod12.jackal.server.util.MetaValue;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameSession implements Runnable {

    public static final String PREFIX = "&c[&4<>&c] &0";

    private final Thread thread;
    private boolean running = true;
    private long timer = 0;

    private GameStatus status = GameStatus.WAITING;
    private GameTile[][] board;

    public GameSession() {

        this.thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();

        final double ns = 1000000000D / 20D;
        double delta = 0;

        long last = System.currentTimeMillis();
        while(running) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1.0D) {
                tick();
                delta--;
            }

        }

    }

    /**
     * Game tick method
     * 1 second == 20 ticks
     */
    private void tick() {

        if(timer > 0) {

            if(timer <= 200L && timer % 20L == 0 || timer % 200L == 0) {
                Server.getInstance().broadcastMessage(PREFIX + "Game starts in " + (timer / 20L) + " seconds");
            }

            timer--;
            if(timer == 0) {
                forceStart();
                return;
            }

        }

        if(status != GameStatus.INGAME) return;
        // TODO ingame logic

    }

    public void forceStart() {

        if(status == GameStatus.INGAME) {
            System.out.println("Game already started");
            return;
        }
        status = GameStatus.INGAME;
        timer = 0;

        List<ServerSideConnection> connections = Server.getInstance().getConnections();

        if(connections.size() > 4)
            System.out.println("Players amount more than 4. Issue occurred?");

        // Teams assigner
        for(GameColor color : GameColor.values()) {

            if(color == GameColor.UNKNOWN || connections.size() == 0) continue;

            int random = ThreadLocalRandom.current().nextInt(connections.size());
            connections.get(random).setColor(color);
            connections.remove(random);

        }

        if(connections.size() > 0) {
            connections.forEach(n -> {
                n.sendPacket(new ClientboundDisconnectPacket(PREFIX + "Sorry, there is an issue occurred while teams assigning, you were kicked"));
                n.close();
            });
        }

        connections = Server.getInstance().getConnections();

        // Generate map
        this.board = MapGenerator.generateMap(6, 6, connections.size());
        // TODO insert boats

        // [PACKETS] Send init packets
        for(ServerSideConnection connection : connections) {

            // Player team
            Server.getInstance().broadcast(new ClientboundPlayerMetadataPacket(MetaValue.COLOR, connection));

            // Inform player about his team
            connection.sendPacket(new ClientboundChatPacket(PREFIX + "Your team is &" + connection.getColor().getColorCode() + connection.getColor().name()));

        }

        // Create game board
        Server.getInstance().broadcast(new ClientboundBoardCreatePacket(board.length, board[0].length));

        // Send map
        for(int x = 0; x < board.length; ++x) {
            for(int y = 0; y < board[0].length; ++y) {
                Server.getInstance().broadcast(new ClientboundTileCreatePacket(x, y, board[x][y].getType()));
            }
        }
        // [PACKETS] Send init packets

        Server.getInstance().broadcastMessage(PREFIX + "Game started. &6Good luck!");
        // TODO Send in chat short rules / game aim

    }

    public void onPlayerRemove(ServerSideConnection connection) {

        // TODO ingame logic
        if(status == GameStatus.WAITING) {

            if(Server.getInstance().getConnections().size() < 2 && timer > 0) {

                timer = 0;
                Server.getInstance().broadcastMessage(PREFIX + "Not enough players! Timer stopped");

            }

        }

    }

    public void onPlayerJoin(ServerSideConnection connection) {

        int players = Server.getInstance().getConnections().size();
        if(players >= 4 && (timer > 200L || timer < 0L)) {

            timer = 200L;

        } else if(players >= 2 && (timer > 1200L || timer <= 0L)) {

            // TODO timer = 1200L
            timer = 600L;

        }

    }

    public void destroy() {
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.running = false;
    }

    public GameStatus getGameStatus() {
        return status;
    }

}
