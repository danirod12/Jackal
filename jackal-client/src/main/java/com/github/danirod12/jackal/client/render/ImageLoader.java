package com.github.danirod12.jackal.client.render;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.util.ColorTheme;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.concurrent.ThreadLocalRandom;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageLoader {

    public static BufferedImage COIN_16;
    public static BufferedImage COGWHEEL_32;

    public static BufferedImage GRASS, SAND, ROCK;
    public static BufferedImage OPEN_GRASS, OPEN_SAND, OPEN_ROCK;

    public static void reload() {

        BufferedImage tileset = loadImage("background_tileset");

        SAND = getTile(tileset, 64, 64, 0, 1);
        GRASS = getTile(tileset, 64, 64, 1, 1);
        ROCK = getTile(tileset, 64, 64, 2, 1);

        OPEN_SAND = getTile(tileset, 64, 64, 0, 0);
        OPEN_GRASS = getTile(tileset, 64, 64, 1, 0);
        OPEN_ROCK = getTile(tileset, 64, 64, 1, 0);

        COIN_16 = loadImage("coin16");
        COGWHEEL_32 = multiply(loadImage("cogwheel16"), 2);

    }

    public static BufferedImage repeatImage(BufferedImage origin, int width, int height) {

        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        int x = 0;
        int y = 0;
        while (y < height) {
            while (x < width) {
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

        if (!name.contains(".")) name += ".png";

        try {
            return ImageIO.read(Jackal.class.getResource("/images/" + name));
        } catch (Exception e) {
            e.printStackTrace();
            return generateCorruptedImage(32, 32);
        }

    }

    public static BufferedImage generateCorruptedImage(int width, int height) {

        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Not require ColorTheme. It is a corrupter image. But could be provided in the future
        graphics.setColor(Color.MAGENTA);

        graphics.fillRect(0, 0, width, height);
        graphics.dispose();

        graphics.setColor(ColorTheme.DEBUG_LIGHT);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (random.nextBoolean())
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
            return origin.getSubimage(tileWidth * x, tileHeight * y, tileWidth, tileHeight);
        } catch (RasterFormatException exception) {
            exception.printStackTrace();
            System.out.println("Cannot crop image [(" + tileWidth * x + "," + tileHeight * y + ")," + tileWidth + "," + tileHeight
                    + "]. Image size " + origin.getWidth() + "x" + origin.getHeight());
            return generateCorruptedImage(tileWidth, tileHeight);
        }
    }

    public static BufferedImage[] repeatImage(int width, int height, BufferedImage... tiles) {

        final int tile_height = tiles[0].getHeight();
        final int tile_width = tiles[0].getWidth();

        BufferedImage[] images = new BufferedImage[tiles.length];
        for (int i = 0; i < tiles.length; i++) {

            BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            int x = 0;
            int y = 0;
            int tile = i;
            while (y < height) {
                while (x < width) {
                    graphics.drawImage(tiles[tile], x, y, null);
                    tile++;
                    if (tile >= tiles.length)
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

    /**
     * This method rotates an inputted image a desired amount of times by 90 degrees clockwise.
     *
     * @param origin     a {@link BufferedImage} object, only square images work
     * @param multiplier an {@link Integer}, describes how many times to rotate the image 90 degrees
     * @return a rotated object of {@link BufferedImage} class
     */
    public static BufferedImage rotateImage(BufferedImage origin, int multiplier) {
        if (multiplier <= 0 || origin.getHeight() != origin.getWidth()) return origin;

        BufferedImage result = origin;

        for (int i = 0; i < multiplier; i++) {
            result = rotateImage(result);
        }
        return result;
    }

    /**
     * This method rotates an inputted image 90 degrees clockwise.
     *
     * @param origin a {@link BufferedImage} object to be rotated, only square images work
     * @return a rotated 90 degrees object of {@link BufferedImage} class
     */
    public static BufferedImage rotateImage(BufferedImage origin) {
        int size = origin.getHeight() - 1;

        BufferedImage result = new BufferedImage(origin.getWidth(), origin.getHeight(), TYPE_INT_RGB);

        for (int x = 0; x <= size; x++) {
            for (int y = 0; y <= size; y++) {
                result.setRGB(size - y, x, origin.getRGB(x, y));
            }
        }
        return result;
    }

    /**
     * This method mirrors an inputted image horizontally.
     *
     * @param origin an object of {@link BufferedImage} class
     * @return a mirrored object of {@link BufferedImage} class
     */
    public static BufferedImage mirrorImageHorizontal(BufferedImage origin) {
        int height = origin.getHeight();
        int width = origin.getWidth();

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(width - x, y, origin.getRGB(x, y));
            }
        }
        return result;
    }

    /**
     * This method mirrors an inputted image vertically.
     *
     * @param origin an object of {@link BufferedImage} class
     * @return a mirrored object of {@link BufferedImage} class
     */
    public static BufferedImage mirrorImageVertical(BufferedImage origin) {
        int height = origin.getHeight();
        int width = origin.getWidth();

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, height - y, origin.getRGB(x, y));
            }
        }
        return result;
    }

}
