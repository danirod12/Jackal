package com.github.danirod12.jackal.client.protocol;

import com.github.danirod12.jackal.client.objects.tile.TileType;
import com.github.danirod12.jackal.client.util.Pair;
import com.github.danirod12.jackal.client.util.Triplet;

public class SimpleDecoder {

    public static Pair<Integer, String> parseIdentifiedData(String string, String separator) {

        String[] data = string.split(separator);
        return new Pair<>(Integer.parseInt(data[0]), join(data, 1, separator));

    }

    public static Triplet<Integer, String, String> parseIdentifiedMarkedData(String string, String separator) {

        String[] data = string.split(separator);
        return new Triplet<>(Integer.parseInt(data[0]), data[1], join(data, 2, separator));

    }

    public static String join(String[] array, int i, String separator) {

        String data = null;
        for(; i < array.length; ++i)
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
