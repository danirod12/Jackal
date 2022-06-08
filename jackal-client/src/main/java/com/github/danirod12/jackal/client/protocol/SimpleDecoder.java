package com.github.danirod12.jackal.client.protocol;

import com.github.danirod12.jackal.client.objects.tile.TileType;
import com.github.danirod12.jackal.client.util.Pair;
import com.github.danirod12.jackal.client.util.Triplet;

public class SimpleDecoder {

    public static Pair<Integer, String> parseIdentifiedData(String string, String separator) {

        String[] data = split(string, separator, 2);
        return new Pair<>(Integer.parseInt(data[0]), data[1]);

    }

    public static Triplet<Integer, String, String> parseIdentifiedMarkedData(String string, String separator) {

        String[] data = split(string, separator, 3);
        return new Triplet<>(Integer.parseInt(data[0]), data[1], data[2]);

    }

    public static String[] split(String data, String separator, int args) {

        String[] parsed = new String[args];
        String last = null;
        int index = 0;
        for (String string : data.split(separator)) {

            if (index == parsed.length - 1) {
                last = last == null ? string : last + separator + string;
            } else {
                parsed[index] = string;
                index++;
            }

        }
        if (index != parsed.length - 1 || last == null)
            throw new IllegalArgumentException("Incorrect data \"" + data + "\" (Required " + args + " args)");
        parsed[index] = last;
        return parsed;

    }

    public static String join(String[] array, int i, String separator) {

        String data = null;
        for (; i < array.length; ++i)
            data = data == null ? array[i] : data + separator + array[i];
        return data;

    }

    public static Pair<Pair<Integer, Integer>, String> parseLocatedData(String data) {

        String[] parsed = data.split(":");
        return new Pair<>(new Pair<>(Integer.valueOf(parsed[0]), Integer.parseInt(parsed[1])), parsed[2]);

    }

    public static Pair<Pair<Integer, Integer>, TileType> parseLocatedTileType(String data) {

        String[] parsed = data.split(":");
        return new Pair<>(new Pair<>(Integer.valueOf(parsed[0]), Integer.parseInt(parsed[1])), TileType.parse(Integer.parseInt(parsed[2])));

    }

}
