package com.github.danirod12.jackal.server.commands;

import com.github.danirod12.jackal.server.commands.sender.CommandSender;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ServerCommand {

    public static final Pattern PATTERN = Pattern.compile("[a-z][a-z0-9]*");

    private final boolean onlyServer;
    private final List<String> aliases;

    /**
     * @param onlyServer Only server will be able to use this command
     * @param aliases    Commands list (origin, aliases)
     */
    public ServerCommand(boolean onlyServer, String... aliases) {
        this.onlyServer = onlyServer;
        this.aliases = Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toList());
        for (String command : aliases) {
            if (!PATTERN.matcher(command).matches())
                throw new IllegalArgumentException("Command " + command + " not matches pattern " + PATTERN.pattern());
        }
    }

    /**
     * @return Get all command aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * @return Checks if command available only for server
     */
    public boolean isOnlyServer() {
        return onlyServer;
    }

    /**
     * System method to perform command
     */
    public void perform(CommandSender sender, String alias, String[] args) {

        try {
            onCommand(sender, alias, args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            sender.sendMessage("&cAn exception occurred while executing command");
        }

    }

    /**
     * @param sender Command sender (ConsoleSender or ServerSideConnection)
     * @param alias  Alias used
     * @param args   Command args
     */
    public abstract void onCommand(CommandSender sender, String alias, String[] args);

}
