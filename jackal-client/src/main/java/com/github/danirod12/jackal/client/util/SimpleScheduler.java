package com.github.danirod12.jackal.client.util;

public class SimpleScheduler<T> {

    private final int ticks;
    private int current;
    private T value;

    public SimpleScheduler(int ticks) {
        this.ticks = ticks;
        this.current = ticks;
    }

    public void set(T t) {
        this.value = t;
    }

    public T get() { return value; }

    public boolean tick() {
        if(current-- < 0) {
            current = ticks;
            return true;
        }
        return false;
    }

}
