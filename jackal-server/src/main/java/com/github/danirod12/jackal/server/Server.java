package com.github.danirod12.jackal.server;

import com.github.danirod12.jackal.server.commands.CommandsHandler;
import com.github.danirod12.jackal.server.commands.ConsoleListener;
import com.github.danirod12.jackal.server.commands.sender.ConsoleSender;
import com.github.danirod12.jackal.server.game.GameSession;
import com.github.danirod12.jackal.server.game.GameStatus;
import com.github.danirod12.jackal.server.protocol.ServerSideConnection;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundChatPacket;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundDisconnectPacket;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundPlayerRemovePacket;
import com.github.danirod12.jackal.server.protocol.packet.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static Server instance;
    private final ServerSocket listener;
    private Thread mainThread;

    public static final ConsoleSender CONSOLE_SENDER = new ConsoleSender();

    private final List<ServerSideConnection> connections = new ArrayList<>();
    private GameSession session;

    public static Server getInstance() { return instance; }

    public static void main(String[] args) throws IOException {

        int port = 2143;
        try {
            port = Integer.parseInt(args[0]);
        } catch(Exception ignored) {}

        instance = new Server(port);
        CommandsHandler.registerDefaultCommands();

        new ConsoleListener();
        instance.restartGameSession();

    }

    private void restartGameSession() {

        session = new GameSession();
        if(mainThread != null) {
            Thread thread = mainThread;
            mainThread = null;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mainThread = new Thread(startServer());
        mainThread.start();

    }

    public Server(int port) throws IOException {
        this.listener = new ServerSocket(port);
    }

    public Runnable startServer() {

        return () -> {

            try {

                while(mainThread != null && !listener.isClosed()) {

                    Socket socket = listener.accept();
                    System.out.println("A new connection from " + socket.getInetAddress().toString());

                    ServerSideConnection connection = new ServerSideConnection(socket);

                    if(getConnections().size() >= 4) {
                        connection.sendPacket(new ClientboundDisconnectPacket("Server is full"));
                        connection.close();
                        continue;
                    }

                    if(session.getGameStatus() == GameStatus.INGAME) {
                        connection.sendPacket(new ClientboundDisconnectPacket("Server already in game"));
                        connection.close();
                        continue;
                    }

                    Thread thread = new Thread(connection);
                    thread.start();

                    connections.add(connection);

                }

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        };

    }

    public GameSession getGameSession() { return session; }

    public List<ServerSideConnection> getConnections() {
        return new ArrayList<>(connections);
    }

    public void removeConnection(ServerSideConnection connection) {

        connections.remove(connection);
        if(connection.isAuthorized())
            this.broadcast(new ClientboundPlayerRemovePacket(connection));

        session.onPlayerRemove(connection);

    }

    public void broadcastMessage(String message) {
        broadcast(new ClientboundChatPacket(message));
    }

    public void broadcast(Packet packet) {
        for (ServerSideConnection connection : getConnections()) {
            if (!connection.isAuthorized()) continue;
            connection.sendPacket(packet);
        }
    }

    public void restart() {

        restartGameSession();

        ClientboundDisconnectPacket packet = new ClientboundDisconnectPacket("Server is restarting");

        connections.forEach(n -> n.sendPacket(packet));
        connections.clear();

    }

}
