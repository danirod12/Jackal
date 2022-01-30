package com.github.danirod12.jackal.client.protocol;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.objects.game.GameBoard;
import com.github.danirod12.jackal.client.objects.game.Player;
import com.github.danirod12.jackal.client.objects.input.ChatObject;
import com.github.danirod12.jackal.client.objects.tile.TileType;
import com.github.danirod12.jackal.client.protocol.packet.Packet;
import com.github.danirod12.jackal.client.util.GameColor;
import com.github.danirod12.jackal.client.util.Pair;
import com.github.danirod12.jackal.client.util.Triplet;
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

    private final List<Player> players = new ArrayList<>();
    private GameBoard board;

    public GameBoard getBoard() {
        return board;
    }

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
                } catch (Throwable exception) {
                    exception.printStackTrace();
                    close();
                }

            }

        }).start();

    }

    private void onDataReceive(NamedData data) {

        switch (data.getID()) {

            // Disconnect packet
            case 0: {

                System.out.println("Disconnected: " + data.getData());
                return;

            }

            // Add player packet
            case 1: {

                players.add(new Player(data.getData()));
                return;

            }

            // Chat packet
            case 2: {

                ChatObject chat = Jackal.getGameLoop().getObjectsHandler().getChat();
                if(chat != null) chat.addMessage(data.getData());
                else System.out.println("CHAT: " + data.getData());
                return;

            }

            // Remove player packet
            case 3: {

                players.removeIf(player -> player.getName().equalsIgnoreCase(data.getData()));
                return;

            }

            // Player metadata packet
            case 4: {

                Triplet<Integer, String, String> parsed = SimpleDecoder.parseIdentifiedMarkedData(data.getData(), ":");
                Player player = getPlayer(parsed.getB());
                if(player == null) return;
                switch (parsed.getA()) {
                    // Update player color
                    case 0: {
                        player.setColor(GameColor.parseColor(parsed.getC()));
                        return;
                    }
                    // Update player balance
                    case 1: {
                        player.setMoney(Integer.parseInt(parsed.getC()));
                        return;
                    }
                    // Unknown metadata id
                    default: throw new IllegalArgumentException("Unknown metadata ID - " + parsed.getA());
                }

            }

            // Create board packet
            case 20: {

                if(board != null) throw new UnsupportedOperationException("Board already exists");
                board = new GameBoard(Integer.parseInt(data.getData().split(":")[0]), Integer.parseInt(data.getData().split(":")[1]));
                return;

            }

            // Create tile packet
            case 21: {

                Pair<Pair<Integer, Integer>, TileType> parsed = SimpleDecoder.parseLocatedTileType(data.getData());
                board.createTile(parsed.getKey().getA(), parsed.getKey().getB(), parsed.getValue());
                return;

            }

            // Unknown packet id
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

        players.clear();
        Jackal.getGameLoop().destroyConnection();

    }

    public Player getPlayer(String name) {

        for(Player player : getPlayers())
            if(player.getName().equalsIgnoreCase(name))
                return player; return null;

    }

    public List<Player> getPlayers() { return players; }

}
