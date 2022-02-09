package com.github.danirod12.jackal.client.util;

import java.awt.*;
import java.util.regex.Pattern;

public class Misc {

    public static final Pattern PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9]");

    public static int percentage(int origin, int percent) {

        if(percent < 0 || percent > 100) throw new IllegalArgumentException(percent + " not in [0, 100] range");
        return origin * percent / 100;

    }

    public static int percentageOf(int firstNum, int secondNum) {
        return (firstNum * 100) / secondNum;
    }

    public static int percentageOf(long firstNum, long secondNum) {
        return (int)((firstNum * 100L) / secondNum);
    }

    public static boolean isInsideRoundedRect(int pos1, int pos2, int width, int height, int arc) {

        // bound
        if(pos1 < 0 || pos2 < 0 || pos1 > width || pos2 > height) return false;

        // not arced zone
        if(pos1 >= arc && pos1 <= width - arc || pos2 >= arc && pos2 <= height - arc) return true;

        // arced zone
        return getDistance(pos1, pos2, arc, arc) <= arc || getDistance(pos1, pos2, width - arc, arc) <= arc ||
                getDistance(pos1, pos2, width - arc, height - arc) <= arc || getDistance(pos1, pos2, arc, height - arc) <= arc;

    }

    public static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static String substringToSpace(String string) {

        if(string.endsWith(" ")) {

            while(string.endsWith(" "))
                string = string.substring(0, string.length() - 1);
            return string;

        }

        String out = " ";
        String[] split = string.split(" ");
        for(int i = 0; i < split.length - 1; ++i)
            out += split[i] + " ";

        return out.length() == 1 ? "" : out;

    }

    public static Pair<Integer, Integer> getStringParams(String string, Font font, Graphics2D graphics) {
        return new Pair<>(graphics.getFontMetrics().stringWidth(string),
                (int)font.createGlyphVector(graphics.getFontRenderContext(), string).getPixelBounds(null, 0, 0).getHeight());
    }

}
