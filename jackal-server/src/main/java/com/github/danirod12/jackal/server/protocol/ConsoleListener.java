package com.github.danirod12.jackal.server.protocol;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.game.GameSession;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundChatPacket;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleListener implements Runnable {

    private final Server server;
    private final Thread thread;
    private final Scanner scanner;
    private boolean running = true;

    public ConsoleListener(Server server) {

        this.server = server;
        this.scanner = new Scanner(System.in);

        thread = new Thread(this);

        thread.start();
    }

    @Override
    public void run() {

        while (running) {

            String input = scanner.nextLine();
            String command = input.split(" ")[0].toLowerCase();
            String args;

            switch (command) {
                case "forcestart": {
                    try {
                        this.server.getGameSession().forceStart();
                    } catch (Exception e) {
                        System.out.println("An error occured. Try again");
                    }
                } break;
                case "broadcast": {
                    args = input.substring(input.indexOf(' '));
                    this.server.broadcast(new ClientboundChatPacket("[SERVER]" + args));
                }
//                case "stop": this.server.stop(); break;
            }
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
}
