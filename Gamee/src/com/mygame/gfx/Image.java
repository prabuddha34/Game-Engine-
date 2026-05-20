package com.mygame.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    private int width;
    private int height;
    private int[] p;

    public Image(String path) throws IOException {

        BufferedImage image = ImageIO.read(new File(path));

        width = image.getWidth();
        height = image.getHeight();

        p = new int[width * height];

        image.getRGB(0, 0, width, height, p, 0, width);

        image.flush();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getP() {
        return p;
    }
}