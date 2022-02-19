package com.github.danirod12.jackal.server.commands.game;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.commands.ServerCommand;
import com.github.danirod12.jackal.server.commands.sender.CommandSender;
import com.github.danirod12.jackal.server.protocol.packet.ClientboundChatPacket;

public class BroadcastCommand extends ServerCommand {

    public BroadcastCommand() {
        super(true, "broadcast", "bc", "say");
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {

        String message = null;
        for(String arg : args)
            message = message == null ? arg : message + " " + arg;

        if(message == null) {
            sender.sendMessage("Enter a message");
            return;
        }

        Server.getInstance().broadcast(new ClientboundChatPacket("[Server] " + message));

    }

}
