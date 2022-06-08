package com.github.danirod12.jackal.server.protocol;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.commands.CommandsHandler;
import com.github.danirod12.jackal.server.commands.sender.CommandSender;
import com.github.danirod12.jackal.server.game.GameSession;
import com.github.danirod12.jackal.server.game.GameStatus;
import com.github.danirod12.jackal.server.protocol.packet.*;
import com.github.danirod12.jackal.server.util.GameColor;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.UUID;

public class ServerSideConnection implements Runnable, CommandSender {

    private final Gson gson = new Gson();

    private final Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private String name;
    private GameColor color = GameColor.UNKNOWN;
    private int money = 0;

    public ServerSideConnection(Socket socket) {

        this.socket = socket;
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException exception) {
            close();
        }

    }

    public void run() {

        while (socket.isConnected() && !socket.isClosed()) {

            try {

                final String line = reader.readLine();
                System.out.println("INC " + name + ": " + line);
                if (line == null) {
                    System.out.println("Client \"" + (name == null ? "Unknown" : name) + "\" lost. [" + socket + "] (Null packet)");
                    close();
                    return;
                }

                try {

                    String[] data = SimpleDecoder.split(line, ":", 2);
                    onDataReceive(new NamedData(Integer.parseInt(data[0]), data[1]));

                } catch (NumberFormatException exception) {
                    System.out.println("Cannot fetch data from " + socket.getInetAddress().toString() + ": " + line);
                    exception.printStackTrace();
                } catch (Throwable throwable) {
                    System.out.println("An error occurred while executing data from " + socket.getInetAddress().toString() + ": " + line);
                    throwable.printStackTrace();
                }

            } catch (SocketException exception) {
                System.out.println("Client \"" + (name == null ? "Unknown" : name) + "\" lost. [" + socket + "] (" + exception.getLocalizedMessage() + ")");
                close();
                break;
            } catch (Throwable exception) {
                exception.printStackTrace();
                close();
                break;
            }

        }

    }

    @Override
    public void sendMessage(String message) {
        sendPacket(new ClientboundChatPacket(message));
    }

    public void sendPacket(Packet packet) {

        try {
            String data = packet.build();
            System.out.println("OUT " + name + ": " + data);
            writer.write(data);
            writer.newLine();
            writer.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
            close();
        }

        if (packet instanceof ClientboundDisconnectPacket) close();

    }

    public boolean isAuthorized() {
        return name != null;
    }

    public String getName() {
        return name;
    }

    public void onDataReceive(NamedData data) {

        switch (data.getID()) {

            // Login packet
            case 0: {

                if (name == null) {

                    for (ServerSideConnection connection : Server.getInstance().getConnections()) {
                        if (connection == this) continue;
                        if (connection.isAuthorized() && connection.getName().equalsIgnoreCase(name)) {
                            sendPacket(new ClientboundDisconnectPacket("This name already exists (" + name + ")"));
                            return;
                        }
                    }

                    this.name = data.getData();
                    Server.getInstance().getGameSession().onPlayerJoin(this);

                    System.out.println("Client " + socket.getInetAddress().toString() + " authorized as " + name);
                    Server.getInstance().broadcast(new ClientboundPlayerAddPacket(this));
                    Server.getInstance().broadcast(new ClientboundChatPacket(name + " joined the game"));

                    for (ServerSideConnection connection : Server.getInstance().getConnections()) {
                        if (connection == this) continue;
                        sendPacket(new ClientboundPlayerAddPacket(connection));
                        // TODO metadata packet ?
                    }

                } else {
                    sendPacket(new ClientboundDisconnectPacket("Client already authorized as " + name));
                }
                return;

            }

            // Chat packet
            case 1: {

                if (data.getData().startsWith("/")) {
                    System.out.println("COMMAND: [" + name + "] " + data.getData());
                    CommandsHandler.onCommand(this, data.getData());
                    return;
                }
                System.out.println("CHAT: [" + name + "] " + data.getData());
                if (data.getData().length() > 0 && data.getData().replaceAll("&[a-fA-F0-9]*", "").length() != 0) {
                    Server.getInstance().broadcast(new ClientboundChatPacket("[&" + color.getColorCode() + name + "&0] " + data.getData()));
                }
                return;

            }

            // Request actions packet
            case 10: {

                GameSession session = Server.getInstance().getGameSession();
                if (session.getGameStatus() != GameStatus.INGAME) return;

                UUID uuid = UUID.fromString(data.getData());
                List<String> actions = session.getAvailableActions(uuid, color);
                if (actions == null) return;

                sendPacket(new ClientboundActionsPacket(uuid, actions));
                return;

            }

            // Select action packet
            case 11: {

                GameSession session = Server.getInstance().getGameSession();
                if (session.getGameStatus() != GameStatus.INGAME) return;

                String[] parsed = data.getData().split(":");
                session.onPlayerAction(color, parsed[0], Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
                return;

            }

            default:
                throw new IllegalArgumentException("Unknown packet ID - " + data.getID());

        }

    }

    public void close() {

        try {

            if (writer != null) writer.close();
            if (reader != null) reader.close();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Server.getInstance().removeConnection(this);

    }

    public GameColor getColor() {
        return this.color;
    }

    public void setColor(GameColor color) {
        this.color = color;
    }

    public int getMoney() {
        return money;
    }

}
