package com.github.danirod12.jackal.server.game;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.game.generator.MapGenerator;
import com.github.danirod12.jackal.server.game.item.GameObject;
import com.github.danirod12.jackal.server.game.item.PlayerEntity;
import com.github.danirod12.jackal.server.game.item.TeamBoat;
import com.github.danirod12.jackal.server.game.move.DirectionManager;
import com.github.danirod12.jackal.server.game.move.MoveDirection;
import com.github.danirod12.jackal.server.game.tile.ArrowTile;
import com.github.danirod12.jackal.server.game.tile.GameTile;
import com.github.danirod12.jackal.server.game.tile.TileType;
import com.github.danirod12.jackal.server.game.tile.VoidTile;
import com.github.danirod12.jackal.server.protocol.ServerSideConnection;
import com.github.danirod12.jackal.server.protocol.packet.*;
import com.github.danirod12.jackal.server.util.GameColor;
import com.github.danirod12.jackal.server.util.MetaValue;
import com.github.danirod12.jackal.server.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GameSession implements Runnable {

    public static final String PREFIX = "&c[&4<>&c] &0";

    private final Thread thread;
    private boolean running = true;
    private long timer = 0;

    private GameStatus status = GameStatus.WAITING;

    // GameTile[y][x]
    private GameTile[][] board;
    private HashMap<GameColor, TeamBoat> boats;

    public static final long ACTION_AWAIT_DEFAULT = 200L; // 10 seconds

    private long action_await = ACTION_AWAIT_DEFAULT;
    private ServerSideConnection current = null;

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
        action_await--;
        if(action_await < 0)
            nextTurn();

    }

    private void nextTurn() {

        action_await = ACTION_AWAIT_DEFAULT;

        List<ServerSideConnection> connections = Server.getInstance().getConnections();
        if(connections.size() < 2) return;

        int index = connections.indexOf(this.current) + 1;

        this.current = connections.get(index >= connections.size() ? 0 : index);
        Server.getInstance().broadcast(new ClientboundTurnChangePacket(this.current, ACTION_AWAIT_DEFAULT, -1L));

        ClientboundChatPacket packet = new ClientboundChatPacket(PREFIX + "Next turn - &" + current.getColor().getColorCode() + current.getName());
        for (ServerSideConnection connection : Server.getInstance().getConnections())
            connection.sendPacket(connection.getName().equalsIgnoreCase(current.getName()) ? new ClientboundChatPacket(PREFIX + "Your turn &c[!]") : packet);

    }

    public void forceStart() {

        if(status == GameStatus.INGAME) {
            System.out.println("Game already started");
            return;
        }
        status = GameStatus.INGAME;
        timer = 0;
        action_await = 60L;

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
        this.boats = MapGenerator.connectBoats(this.board, connections.stream().map(ServerSideConnection::getColor).collect(Collectors.toList()));

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
        for(int y = 0; y < board.length; ++y) {
            for(int x = 0; x < board[0].length; ++x) {
                GameTile tile = board[y][x];
                Server.getInstance().broadcast(new ClientboundTileCreatePacket(y, x, tile.getType()));

                if(tile instanceof VoidTile) {

                    TeamBoat boat = ((VoidTile) tile).getBoat();
                    if(boat == null) continue;

                    // Send team boat
                    Server.getInstance().broadcast(boat.getUpdatePacket(0, y, x));

                    // Send default team players
                    for(PlayerEntity object : boat.getPlayers())
                        Server.getInstance().broadcast(object.getUpdatePacket(0, y, x));

                }

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

    public List<String> getAvailableActions(UUID uuid, GameColor filter) {

        for(int y = 0; y < board.length; ++y) {

            for(int x = 0; x < board[0].length; ++x) {

                GameTile tile = board[y][x];
                for(GameObject object : tile.getItems()) {

                    if(!object.getUuid().equals(uuid)) continue;
                    if(filter != null && object.getColor() != filter) continue;

                    if(object instanceof PlayerEntity)
                        return DirectionManager.getAvailableMovements(board, y, x, object.getColor());
                    else if(object instanceof TeamBoat)
                        return DirectionManager.getAvailableBoatMovements(board, y, x);
                    else return null;

                }

            }

        }
        return null;

    }

    public void onPlayerAction(GameColor color, String uuid, int moveY, int moveX) {

        final boolean boat_move = boats.get(color).getUuid().toString().equalsIgnoreCase(uuid);
        for(int y = 0; y < board.length; ++y) {

            for(int x = 0; x < board[0].length; ++x) {

                GameTile tile = board[y][x];
                for(GameObject object : tile.getItems()) {

                    if(!object.getUuid().toString().equalsIgnoreCase(uuid)) continue;
                    if(object.getColor() != color) continue;

                    if(boat_move) {

                        for(String path : DirectionManager.getAvailableBoatMovements(board, y, x)) {

                            if(path.endsWith("." + moveY + ":" + moveX)) {
                                // Move boat

                                tile.removeItem(object);
                                GameTile moveTile = board[moveY][moveX];
                                moveTile.addItem(object);
                                Server.getInstance().broadcast(object.getUpdatePacket(0, moveY, moveX));

                                String[] steps = path.split("\\.");
                                for (String s : steps) {

                                    GameTile step = board[Integer.parseInt(s.split(":")[0])][Integer.parseInt(s.split(":")[1])];
                                    // Kill player entity
                                    for (GameObject item : new ArrayList<>(step.getItems())) {

                                        if (item instanceof PlayerEntity) {

                                            step.removeItem(item);
                                            Pair<Integer, Integer> pair = boats.get(item.getColor()).getRelatedTileYX(board);
                                            board[pair.getA()][pair.getB()].addItem(item);
                                            Server.getInstance().broadcast(item.getUpdatePacket(0, pair.getA(), pair.getB()));

                                        } else if(item != object) {

                                            System.out.println("WARNING: Item " + item.getClass().getSimpleName() + " found, " +
                                                    "but should be only " + PlayerEntity.class.getSimpleName());

                                        }

                                    }

                                }
                                nextTurn();
                                return;

                            }

                        }

                    } else {

                        for(String path : DirectionManager.getAvailableMovements(board, y, x, object.getColor())) {

                            if(path.endsWith("." + moveY + ":" + moveX)) {
                                // Move player

                                tile.removeItem(object);
                                GameTile moveTile = board[moveY][moveX];

                                for(GameObject item : new ArrayList<>(moveTile.getItems())) {

                                    // Kill enemy
                                    if(item instanceof PlayerEntity) {

                                        PlayerEntity exists = ((PlayerEntity) item);
                                        if(exists.getColor() != color) {

                                            Pair<Integer, Integer> boat = boats.get(exists.getColor()).getRelatedTileYX(board);
                                            moveTile.removeItem(item);
                                            board[boat.getKey()][boat.getValue()].addItem(item);
                                            Server.getInstance().broadcast(item.getUpdatePacket(0, boat.getKey(), boat.getValue()));

                                        }
                                        //continue;

                                    }

                                }

                                moveTile.addItem(object);

                                Server.getInstance().broadcast(object.getUpdatePacket(0, moveY, moveX));
                                nextTurn();

                                return;
                            }

                        }

                    }
                    throw new UnsupportedOperationException("Cannot move " + uuid + " for "
                            + color.name() + " to (y: " + moveY + ", x: " + moveX + ") - Path not exists");

                }

            }

        }
        throw new UnsupportedOperationException("Cannot move " + uuid + " for "
                + color.name() + " to (y: " + moveY + ", x: " + moveX + ") - Entity not exists");

    }

}
