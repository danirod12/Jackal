package com.github.danirod12.jackal.server.commands;

import com.github.danirod12.jackal.server.commands.game.BroadcastCommand;
import com.github.danirod12.jackal.server.commands.game.ForceStartCommand;
import com.github.danirod12.jackal.server.commands.sender.CommandSender;
import com.github.danirod12.jackal.server.protocol.ServerSideConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandsHandler {

    private static final List<ServerCommand> commands = new ArrayList<>();

    /**
     *
     * @return All registered commands
     */
    public static List<ServerCommand> getCommands() {
        return new ArrayList<>(commands);
    }

    /**
     * Register a new command
     * @param command {@link ServerCommand} built command
     */
    public static void registerCommand(ServerCommand command) {
        for(String alias : command.getAliases())
            unregisterCommand(alias);
        commands.add(command);
    }

    /**
     * Unregister command by alias
     * @param name Command name (alias)
     */
    public static void unregisterCommand(String... name) {

        List<String> filter = Stream.of(name).map(String::toLowerCase).distinct().collect(Collectors.toList());
        commands:
        for(ServerCommand command : getCommands()) {

            for(String cmd : filter) {

                if(command.getAliases().contains(cmd)) {

                    unregisterCommand(command);
                    filter.remove(cmd);
                    break commands;

                }

            }

        }

    }

    /**
     * Unregister command by instance
     * @param command {@link ServerCommand} instance
     */
    public static void unregisterCommand(ServerCommand command) {
        commands.remove(command);
    }

    /**
     * Parse command and execute it
     * @param sender {@link CommandSender} sender (Console, ServerSideConnection, etc.)
     * @param message Unparsed command string
     */
    public static void onCommand(CommandSender sender, String message) {

        String[] data = message.split(" ");
        if(data[0].startsWith("/")) data[0] = data[0].substring(1);

        for(ServerCommand command : getCommands()) {

            if(command.getAliases().contains(data[0].toLowerCase())) {

                if(command.isOnlyServer() && sender instanceof ServerSideConnection) {
                    sender.sendMessage("&cSorry, but you do not have permission to execute this command");
                    return;
                }
                command.perform(sender, data[0], Arrays.copyOfRange(data, 1, data.length));
                return;

            }

        }
        sender.sendMessage("&cCommand &e" + data[0] + "&c not found :(");

    }

    /**
     * Register default commands
     */
    public static void registerDefaultCommands() {

        registerCommand(new ForceStartCommand());
        registerCommand(new BroadcastCommand());

    }

}
