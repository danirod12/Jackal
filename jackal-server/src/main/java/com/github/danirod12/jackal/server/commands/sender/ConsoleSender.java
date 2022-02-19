package com.github.danirod12.jackal.server.commands.sender;

public class ConsoleSender implements CommandSender {

    @Override
    public void sendMessage(String message) {
        System.out.println(message.replaceAll("&[a-f0-9]", ""));
    }

}
