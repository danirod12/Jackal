package com.github.danirod12.jackal.client.util;

public class Triplet<A, B, C> {

    private A a;
    private B b;
    private C c;

    public Triplet(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    public Pair<A, C> pairAC() {
        return new Pair<A, C>(a, c);
    }

    public Pair<A, B> pairAB() {
        return new Pair<A, B>(a, b);
    }

    public Pair<B, C> pairBC() {
        return new Pair<B, C>(b, c);
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }

}
