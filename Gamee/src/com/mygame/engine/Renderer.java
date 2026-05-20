package com.mygame.engine;

import com.mygame.ImageTile;
import com.mygame.gfx.Fonts;
import com.mygame.gfx.Image;

import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Renderer {

    private int pW, pH;
    private int[] p;

    private Fonts fonts = Fonts.STANDARD;

    public Renderer(gamecontainer gc) {

        pW = gc.getWidth();
        pH = gc.getHeight();

        p = ((DataBufferInt) gc.getWindow()
                .getImage()
                .getRaster()
                .getDataBuffer())
                .getData();
    }

    // ---------------- CLEAR SCREEN ----------------
    public void clear() {
        Arrays.fill(p, 0);
    }

    // ---------------- PIXEL DRAW WITH ALPHA ----------------
    public void setPixel(int x, int y, int value) {

        if (x < 0 || x >= pW || y < 0 || y >= pH) return;

        if (value == 0xFFFF00FF) return;

        int alpha = (value >> 24) & 0xff;

        if (alpha == 0) return;

        int index = x + y * pW;

        // Fully opaque
        if (alpha == 255) {
            p[index] = value;
            return;
        }

        int pixelColor = p[index];

        int oldRed = (pixelColor >> 16) & 0xff;
        int oldGreen = (pixelColor >> 8) & 0xff;
        int oldBlue = pixelColor & 0xff;

        int newRed = (value >> 16) & 0xff;
        int newGreen = (value >> 8) & 0xff;
        int newBlue = value & 0xff;

        float a = alpha / 255.0f;

        int finalRed = (int)(oldRed - (oldRed - newRed) * a);
        int finalGreen = (int)(oldGreen - (oldGreen - newGreen) * a);
        int finalBlue = (int)(oldBlue - (oldBlue - newBlue) * a);

        p[index] =
                (255 << 24) |
                        (finalRed << 16) |
                        (finalGreen << 8) |
                        finalBlue;
    }

    // ---------------- IMAGE DRAW ----------------
    public void drawImage(Image image, int offX, int offY) {

        int imgW = image.getWidth();
        int imgH = image.getHeight();

        if (offX >= pW || offY >= pH || offX + imgW <= 0 || offY + imgH <= 0)
            return;

        int startX = Math.max(0, -offX);
        int startY = Math.max(0, -offY);
        int endX = Math.min(imgW, pW - offX);
        int endY = Math.min(imgH, pH - offY);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {

                int pixel = image.getP()[x + y * imgW];

                setPixel(x + offX, y + offY, pixel);
            }
        }
    }

    // ---------------- TILE DRAW ----------------
    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {

        int tileW = image.getTileW();
        int tileH = image.getTileH();

        int imgW = image.getWidth();
        int imgH = image.getHeight();

        if (offX >= pW || offY >= pH || offX + tileW <= 0 || offY + tileH <= 0)
            return;

        int startX = Math.max(0, -offX);
        int startY = Math.max(0, -offY);
        int endX = Math.min(tileW, pW - offX);
        int endY = Math.min(tileH, pH - offY);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {

                int srcX = x + tileX * tileW;
                int srcY = y + tileY * tileH;

                if (srcX < 0 || srcY < 0 || srcX >= imgW || srcY >= imgH)
                    continue;

                int pixel = image.getP()[srcX + srcY * imgW];

                setPixel(x + offX, y + offY, pixel);
            }
        }
    }

    // ---------------- TEXT DRAW ----------------
    public void drawText(String text, int offX, int offY, int color) {

        Image fontImage = fonts.getFontImage();
        text = text.toUpperCase();

        int charW = fonts.getWidth()[0];
        int charH = fontImage.getHeight();

        int sheetWidth = fontImage.getWidth();
        int charsPerRow = sheetWidth / charW;

        int offsetX = 0;

        for (int i = 0; i < text.length(); i++) {

            int unicode = text.codePointAt(i) - 32;

            if (unicode < 0 || unicode >= fonts.getWidth().length)
                continue;

            int tileX = (unicode % charsPerRow) * charW;
            int tileY = (unicode / charsPerRow) * charH;

            for (int y = 0; y < charH; y++) {
                for (int x = 0; x < charW; x++) {

                    int srcIndex = (tileX + x) + (tileY + y) * sheetWidth;

                    if (srcIndex < 0 || srcIndex >= fontImage.getP().length)
                        continue;

                    int pixel = fontImage.getP()[srcIndex];

                    if (pixel != 0xFFFF00FF) {
                        setPixel(offX + offsetX + x, offY + y, color);
                    }
                }
            }

            offsetX += charW;
        }
    }

    // ---------------- RECT DRAW ----------------
    public void drawRect(int offX,int offY,int width,int height,int color){

        for(int y = 0; y <= height; y++) {

            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }

        for(int x = 0; x <= width; x++) {

            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
        }
    }

    // ---------------- FILLED RECT DRAW ----------------
    public void drawFillRect(int offX,int offY,int width,int height,int color) {

        for(int y = 0; y <= height; y++) {

            for(int x = 0; x <= width; x++) {

                setPixel(x + offX, y + offY, color);
            }
        }
    }

    // ---------------- LIGHT DRAW ----------------
    public void drawLight(int centerX, int centerY, int radius) {

        for(int y = -radius; y <= radius; y++) {

            for(int x = -radius; x <= radius; x++) {

                int dx = x;
                int dy = y;

                int distanceSquared = dx * dx + dy * dy;

                if(distanceSquared <= radius * radius) {

                    float distance =
                            (float)Math.sqrt(distanceSquared);

                    float brightness =
                            1.0f - (distance / radius);

                    int alpha =
                            (int)(brightness * 255);

                    int color =
                            (alpha << 24) |
                                    0xffffff;

                    setPixel(centerX + x,
                            centerY + y,
                            color);
                }
            }
        }
    }
}