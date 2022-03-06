package com.github.danirod12.jackal.server.commands.game;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.commands.ServerCommand;
import com.github.danirod12.jackal.server.commands.sender.CommandSender;

public class RestartCommand extends ServerCommand {

    public RestartCommand() {
        super(true, "restart", "reload");
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {

        Server.getInstance().restart();
        sender.sendMessage("Server have been " + alias + "ed");

    }

}
