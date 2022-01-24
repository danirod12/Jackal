package com.github.danirod12.jackal.client.protocol;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.game.Player;
import com.github.danirod12.jackal.client.objects.input.ChatObject;
import com.github.danirod12.jackal.client.protocol.packet.Packet;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ClientSideConnection {

    private final Gson gson = new Gson();

    private final Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private List<Player> players = new ArrayList<>();

    public ClientSideConnection(Socket socket) {

        this.socket = socket;
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException exception) {
            close();
        }
        listenForPackets();

    }

    public void sendPacket(Packet packet) {

        try {
            System.out.println("Sending packet " + packet.getClass().getSimpleName() + packet.build());
            writer.write(packet.build());
            writer.newLine();
            writer.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
            close();
        }

    }

    public void listenForPackets() {

        new Thread(() -> {

            while(socket.isConnected() && !socket.isClosed()) {

                try {

                    final String line = reader.readLine();

                    if(line == null) {
                        System.out.println("Connection is reset by server");
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
                    exception.printStackTrace();
                    close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    close();
                }

            }

        }).start();

    }

    private void onDataReceive(NamedData data) throws Throwable {

        switch (data.getID()) {

            case 0: {

                System.out.println("Disconnected: " + data.getData());
                return;

            }

            case 1: {

                System.out.println("Player joined the game: " + data.getData());
                return;

            }

            case 2: {

                ChatObject chat = Jackal.getGameLoop().getObjectsHandler().getChat();
                System.out.println("CHAT: " + data.getData());
                if(chat != null) chat.addMessage(data.getData());
                return;

            }

            default: throw new IllegalArgumentException("Unknown packet ID - " + data.getID());

        }

    }

    public void close() {

        try {

            if(writer != null) writer.close();
            if(reader != null) reader.close();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Jackal.getGameLoop().destroyConnection();

    }

}
