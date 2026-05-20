package com.mygame;

import com.mygame.gfx.Image;

import java.io.IOException;

public class ImageTile extends Image {

    private int tileW;
    private int tileH;

    public ImageTile(String path, int tileW, int tileH) throws IOException {
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;
    }

    public int getTileW() {
        return tileW;
    }

    public int getTileH() {
        return tileH;
    }
}