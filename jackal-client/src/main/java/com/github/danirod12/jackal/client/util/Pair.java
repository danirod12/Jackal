package com.github.danirod12.jackal.client.util;

import java.util.Map;

public class Pair<A, B> implements Cloneable {

    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() { return a; }
    public B getB() { return b; }

    public A getKey() { return a; }
    public B getValue() { return b; }

    public void setKey(A a) { this.a = a; }
    public void setValue(B b) { this.b = b; }

    public void setA(A a) { this.a = a; }
    public void setB(B b) { this.b = b; }

    public static <A, B> Pair<A, B> from(Map.Entry<A, B> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    @Override
    public Pair<A, B> clone() {
        return new Pair<>(a, b);
    }

    @Override
    public String toString() { return "Pair{key:" + a.toString() + ",value:" + b.toString() + "}"; }

    @Override
    public boolean equals(Object object) {

        if(object instanceof Pair) {
            Pair<?, ?> pair = (Pair<?, ?>) object;
            return pair.getKey().equals(getKey()) && pair.getValue().equals(getValue());
        }
        return false;

    }

}