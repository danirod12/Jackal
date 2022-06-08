package com.github.danirod12.jackal.client.objects.input;

public class ChatMessage {

    private final long time;
    private final String message;

    public ChatMessage(String message) {
        this.time = System.currentTimeMillis();
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public boolean render() {
        return System.currentTimeMillis() - time <= 10000L;
    }

    public int getAlpha() {
        long time = System.currentTimeMillis() - this.time;
        if (time >= 10000) return 0;
        if (time >= 8000) {
            return (int) (10000 - time) * 255 / 2000;
        }
        if (time <= 1000) {
            return (int) time * 255 / 1000;
        }
        return 255;
    }

}
