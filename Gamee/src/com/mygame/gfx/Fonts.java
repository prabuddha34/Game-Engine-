package com.mygame.gfx;

public class Fonts {

    public static final Fonts STANDARD = new Fonts();

    private Image fontImage;

    private int[] widths;

    public Fonts() {

        // load font image
        try {
            fontImage = new Image("res/font.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔥 IMPORTANT: initialize array BEFORE using it
        widths = new int[96]; // ASCII 32–127 range

        for (int i = 0; i < widths.length; i++) {
            widths[i] = 8; // fixed width font (safe default)
        }
    }

    public Image getFontImage() {
        return fontImage;
    }

    public int[] getWidth() {
        return widths;
    }
}