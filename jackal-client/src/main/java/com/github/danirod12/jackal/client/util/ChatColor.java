package com.github.danirod12.jackal.client.util;

public enum ChatColor {


    BLACK('0', new ColorBuilder(0, 0, 0)),
    DARK_BLUE('1', new ColorBuilder(3, 37, 126)),
    DARK_GREEN('2', new ColorBuilder(21, 71, 52)),
    DARK_AQUA('3', new ColorBuilder(0, 124, 128)),
    DARK_RED('4', new ColorBuilder(161, 0, 14)),
    DARK_PURPLE('5', new ColorBuilder(128, 49, 167)),
    GOLD('6', new ColorBuilder(255, 184, 28)),
    GRAY('7', new ColorBuilder(137, 142, 140)),
    DARK_GRAY('8', new ColorBuilder(83, 86, 91)),
    BLUE('9', new ColorBuilder(0, 80, 181)),
    GREEN('a', new ColorBuilder(8, 255, 8)),
    AQUA('b', new ColorBuilder(172, 55, 238)),
    RED('c', new ColorBuilder(237, 29, 36)),
    LIGHT_PURPLE('d', new ColorBuilder(172, 79, 198)),
    YELLOW('e', new ColorBuilder(255, 233, 0)),
    WHITE('f', new ColorBuilder(255, 255, 255));

    private final char key;
    private final ColorBuilder builder;

    ChatColor (char key, ColorBuilder builder) {
        this.key = key;
        this.builder = builder;
    }

    public static ChatColor parseColor(char c) {
        for (ChatColor value : values()) {
            if (value.key == c) {
                return value;
            }
        }
        return null;
    }

    public static ChatColor getDefaultColor() {
        return ChatColor.BLACK;
    }

    public ColorBuilder getBuilder() {
        return builder;
    }

}
