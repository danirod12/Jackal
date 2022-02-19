package com.github.danirod12.jackal.server.commands.game;

import com.github.danirod12.jackal.server.Server;
import com.github.danirod12.jackal.server.commands.ServerCommand;
import com.github.danirod12.jackal.server.commands.sender.CommandSender;
import com.github.danirod12.jackal.server.game.GameSession;
import com.github.danirod12.jackal.server.game.GameStatus;

public class ForceStartCommand extends ServerCommand {

    public ForceStartCommand() {
        super(true, "forcestart", "start");
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {

        GameSession session = Server.getInstance().getGameSession();
        if(session.getGameStatus() == GameStatus.INGAME) {
            sender.sendMessage("Game already started");
            return;
        }

        if(Server.getInstance().getConnections().size() < 2) {
            sender.sendMessage("Not enough player to start the game");
            return;
        }
        session.forceStart();
        sender.sendMessage("Game force-started");

    }

}
