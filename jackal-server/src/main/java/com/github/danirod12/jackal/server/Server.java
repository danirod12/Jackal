package com.github.danirod12.jackal.server;

import com.github.danirod12.jackal.server.protocol.ServerSideConnection;
import com.github.danirod12.jackal.server.protocol.packet.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static Server instance;
    private final ServerSocket listener;
    private final List<ServerSideConnection> connections = new ArrayList<>();

    public static Server getInstance() { return instance; }

    public static void main(String[] args) throws IOException {

        int port = 2143;
        try {
            port = Integer.parseInt(args[0]);
        } catch(Exception ignored) {}

        instance = new Server(port);
        instance.startServer();

    }

    public Server(int port) throws IOException {
        this.listener = new ServerSocket(port);
    }

    public void startServer() {

        try {

            while(!listener.isClosed()) {

                Socket socket = listener.accept();
                System.out.println("A new connection from " + socket.getInetAddress().toString());

                ServerSideConnection connection = new ServerSideConnection(socket);
                Thread thread = new Thread(connection);
                thread.start();

                connections.add(connection);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<ServerSideConnection> getConnections() {
        return new ArrayList<>(connections);
    }

    public void removeConnection(ServerSideConnection connection) {
        connections.remove(connection);
    }

    public void broadcast(Packet packet) {
        for (ServerSideConnection elem : getConnections()) {
            if (!elem.isAuthorized()) continue;
            elem.sendPacket(packet);
        }
    }
}
