package com.github.danirod12.jackal.server.protocol;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundChatPacket;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundDisconnectPacket;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundPlayerAddPacket;
import com.github.danirod12.jackal.server.protocol.packet.Packet;
import com.github.danirod12.jackal.server.util.GameColor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerSideConnection implements Runnable {

    private final Gson gson = new Gson();

    private final Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private String name;
    private GameColor color = GameColor.UNKNOWN;

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

        while(socket.isConnected() && !socket.isClosed()) {

            try {

                final String line = reader.readLine();
                if(line == null) {
                    System.out.println("Client \"" + (name == null ? "Unknown" : name) + "\" lost. [" + socket + "]");
                    close();
                    return;
                }

                try {

                    NamedData data = gson.fromJson(line, NamedData.class);
                    onDataReceive(data);

                } catch(JsonSyntaxException exception) {
                    System.out.println("Cannot fetch data from " + socket.getInetAddress().toString() + ": " + line);
                    exception.printStackTrace();
                } catch (Throwable throwable) {
                    System.out.println("An error occurred while executing data from " + socket.getInetAddress().toString() + ": " + line);
                    throwable.printStackTrace();
                }

            } catch(SocketException exception) {
                System.out.println("Client \"" + (name == null ? "Unknown" : name) + "\" lost. [" + socket + "] (" + exception.getLocalizedMessage() + ")");
                close();
                break;
            } catch (IOException exception) {
                exception.printStackTrace();
                close();
                break;
            }

        }

    }

    public void sendPacket(Packet packet) {

        try {
            writer.write(packet.build());
            writer.newLine();
            writer.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
            close();
        }

        if(packet instanceof ClientboundDisconnectPacket) close();

    }

    public boolean isAuthorized() { return name != null; }

    public String getName() { return name; }

    public void onDataReceive(NamedData data) {

        switch(data.getID()) {

            case 0: {

                if(name == null) {

                    this.name = data.getData();
                    for (ServerSideConnection connection : Server.getInstance().getConnections()) {
                        if(connection == this) continue;
                        if(connection.isAuthorized() && connection.getName().equalsIgnoreCase(name)) {
                            sendPacket(new ClientboundDisconnectPacket("This name already exists (" + name + ")"));
                            return;
                        }
                    }
                    System.out.println("Client " + socket.getInetAddress().toString() + " authorized as " + name);
                    Server.getInstance().broadcast(new ClientboundPlayerAddPacket(this));
                    Server.getInstance().broadcast(new ClientboundChatPacket(name + " joined the game"));

                    for(ServerSideConnection connection : Server.getInstance().getConnections()) {
                        if(connection == this) continue;
                        sendPacket(new ClientboundPlayerAddPacket(connection));
                        // TODO metadata packet ?
                    }

                } else {
                    sendPacket(new ClientboundDisconnectPacket("Client already authorized as " + name));
                }
                return;

            }

            case 1: {

                System.out.println("CHAT: [" + name + "] " + data.getData());
                if(data.getData().length() > 0) {
                    Server.getInstance().broadcast(new ClientboundChatPacket("[&" + color.getColorCode() + name + "&0] " + data.getData()));
                }
                return;

            }

            default: throw new IllegalArgumentException("Unknown packet ID - " + data.getID());

        }

    }

    private void close() {

        try {

            if(writer != null) writer.close();
            if(reader != null) reader.close();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(name != null) Server.getInstance().broadcast(new ClientboundChatPacket(name + " left the game"));
        Server.getInstance().removeConnection(this);

    }

}
