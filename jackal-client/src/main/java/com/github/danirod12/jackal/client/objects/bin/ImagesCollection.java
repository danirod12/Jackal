package com.github.danirod12.jackal.client.objects.bin;

import com.github.danirod12.jackal.client.render.ImageLoader;

import java.awt.image.BufferedImage;

public class ImagesCollection {

    private final int delay;
    private final boolean reversed;
    private int current_delay;
    private int current_index = 0;
    private BufferedImage[] images;

    public ImagesCollection(int ticks, boolean reversed, BufferedImage... images) {
        this.delay = current_delay = ticks;
        this.reversed = reversed;
        this.images = images == null ? new BufferedImage[]{ImageLoader.generateCorruptedImage(32, 32)} : images;
    }

    public void set(BufferedImage[] bufferedImages) {
        if (bufferedImages.length == 0) throw new IllegalArgumentException();
        this.images = bufferedImages;
        current_index = 0;
    }

    public void add(BufferedImage... bufferedImages) {

        BufferedImage[] array = new BufferedImage[bufferedImages.length + images.length];

        int index = 0;

        while (index < images.length) {
            array[index] = images[index];
            index++;
        }

        for (BufferedImage bufferedImage : bufferedImages) {
            array[index] = bufferedImage;
            index++;
        }

        this.images = array;

    }

    public void tick() {

        current_delay--;

        if (current_delay == 0) {

            if (reversed) {
                current_index--;
                if (current_index < 0)
                    current_index = images.length - 1;
            } else {
                current_index++;
                if (current_index >= images.length)
                    current_index = 0;
            }
            current_delay = delay;

        }

    }

    public BufferedImage getImage() {
        return images[current_index];
    }

}
