package com.github.danirod12.jackal.client.util;

import java.awt.*;

public class ColorBuilder implements Cloneable {

    private int red;
    private int green;
    private int blue;
    private int alpha;

    @Override
    public ColorBuilder clone() {
        return new ColorBuilder(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return "ColorBuilder{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", alpha=" + alpha +
                '}';
    }

    public int getRed() {
        return red;
    }

    public ColorBuilder setRed(int red) {
        checkValue(red, "red");
        this.red = red;
        return this;
    }

    public int getGreen() {
        return green;
    }

    public ColorBuilder setGreen(int green) {
        checkValue(green, "green");
        this.green = green;
        return this;
    }

    public int getBlue() {
        return blue;
    }

    public ColorBuilder setBlue(int blue) {
        checkValue(blue, "blue");
        this.blue = blue;
        return this;
    }

    public int getAlpha() {
        return alpha;
    }

    public ColorBuilder setAlpha(int alpha) {
        checkValue(alpha, "alpha");
        this.alpha = alpha;
        return this;
    }

    public ColorBuilder() {
        this(0, 0, 0, 255);
    }

    public ColorBuilder(Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public ColorBuilder(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public ColorBuilder set(int red, int green, int blue, int alpha) {

        checkValue(red, "red");
        checkValue(green, "green");
        checkValue(blue, "blue");
        checkValue(alpha, "alpha");

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;

        return this;

    }

    public ColorBuilder set(int red, int green, int blue) {

        return set(red, green, blue, alpha);

    }

    public ColorBuilder(int red, int green, int blue, int alpha) {

        checkValue(red, "red");
        checkValue(green, "green");
        checkValue(blue, "blue");
        checkValue(alpha, "alpha");

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;

    }

    public void checkValue(int data, String name) {
        if(data < 0 || data > 255) throw new IllegalArgumentException("Incorrect " + name + " value, should be in 0-255 range");
    }

    public Color build() {
        return new Color(red, green, blue, alpha);
    }

    public Color build(int alpha) {
        return this.clone().setAlpha(alpha).build();
    }

}
