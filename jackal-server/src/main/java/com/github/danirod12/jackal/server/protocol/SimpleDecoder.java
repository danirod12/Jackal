package com.github.danirod12.jackal.server.protocol;

public class SimpleDecoder {

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

}
