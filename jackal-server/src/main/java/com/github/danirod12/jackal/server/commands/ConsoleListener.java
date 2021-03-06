package com.github.danirod12.jackal.server.commands;

import com.github.danirod12.jackal.server.Server;

import java.util.Scanner;

public class ConsoleListener implements Runnable {

    private final Thread thread;
    private final Scanner scanner;
    private boolean running = true;

    public ConsoleListener() {

        this.scanner = new Scanner(System.in);

        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {

        while (running) {
            CommandsHandler.onCommand(Server.CONSOLE_SENDER, scanner.nextLine());
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
