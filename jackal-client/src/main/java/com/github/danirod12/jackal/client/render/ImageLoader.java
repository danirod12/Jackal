package com.github.danirod12.jackal.client.render;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.util.ColorTheme;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.nio.Buffer;
import java.util.concurrent.ThreadLocalRandom;

public class ImageLoader {

    public final static BufferedImage COIN_16;
    public final static BufferedImage COGWHEEL_32;

    static {

        COIN_16 = loadImage("coin16");
        COGWHEEL_32 = multiply(loadImage("cogwheel16"), 2);

    }

    public static BufferedImage repeatImage(BufferedImage origin, int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        int x = 0;
        int y = 0;
        while(y < height) {
            while(x < width) {
                graphics.drawImage(origin, x, y, null);
                x += origin.getWidth();
            }
            x = 0;
            y += origin.getHeight();
        }
        graphics.dispose();
        return image;

    }

    public static BufferedImage multiply(BufferedImage origin, int amount) {

        BufferedImage image = new BufferedImage(origin.getWidth() * amount, origin.getHeight() * amount, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                image.setRGB(i, j, origin.getRGB(i / amount, j / amount));
            }
        }
        return image;

    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public static BufferedImage loadImage(String name) {

        if(!name.contains(".")) name += ".png";

        try {
            return ImageIO.read(Jackal.class.getResource("/images/" + name));
        } catch (Exception e) {
            e.printStackTrace();
            return generateCorruptedImage(32, 32);
        }

    }

    public static BufferedImage generateCorruptedImage(int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Not require ColorTheme. It is a corrupter image. But could be provided in the future
        graphics.setColor(Color.MAGENTA);

        graphics.fillRect(0, 0, width, height);
        graphics.dispose();

        graphics.setColor(ColorTheme.DEBUG_LIGHT);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(random.nextBoolean())
                    image.setRGB(i, j, 0xFFFFFF);
            }
        }

        graphics = image.createGraphics();
        graphics.setColor(ColorTheme.DEBUG_DARK);
        graphics.drawLine(0, 0, width, height);
        graphics.drawLine(0, height, width, 0);
        graphics.dispose();

        return image;

    }

    public static BufferedImage generateImage(int width, int height, Color color) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
        return image;

    }

    public static BufferedImage getTile(BufferedImage origin, int tileWidth, int tileHeight, int x, int y) {
        try {
            return origin.getSubimage((tileWidth - 1) * x, (tileHeight - 1) * y, tileWidth, tileHeight);
        } catch(RasterFormatException exception) {
            exception.printStackTrace();
            System.out.println("Cannot crop image [(" + (tileWidth - 1) * x + "," + (tileHeight - 1) * y + ")," + tileWidth + "," + tileHeight
                    + "]. Image size " + origin.getWidth() + "x" + origin.getHeight());
            return generateCorruptedImage(tileWidth, tileHeight);
        }
    }

    public static BufferedImage[] repeatImage(int width, int height, BufferedImage... tiles) {

        final int tile_height = tiles[0].getHeight();
        final int tile_width = tiles[0].getWidth();

        BufferedImage[] images = new BufferedImage[tiles.length];
        for (int i = 0; i < tiles.length; i++) {

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            int x = 0;
            int y = 0;
            int tile = i;
            while(y < height) {
                while(x < width) {
                    graphics.drawImage(tiles[tile], x, y, null);
                    tile++;
                    if(tile >= tiles.length)
                        tile = 0;
                    x += tile_width;
                }
                tile = i;
                x = 0;
                y += tile_height;
            }
            graphics.dispose();
            images[i] = image;

        }

        return images;

    }

    public static BufferedImage drawBound(BufferedImage origin, Color color, int size) {

        Graphics2D graphics = origin.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, origin.getWidth(), size);
        graphics.fillRect(origin.getWidth() - size, size, size, origin.getHeight() - size);
        graphics.fillRect(0, origin.getHeight() - size, origin.getWidth() - size, origin.getHeight());
        graphics.fillRect(0, size, size, origin.getHeight() - size);
        graphics.dispose();
        return origin;

    }

}
