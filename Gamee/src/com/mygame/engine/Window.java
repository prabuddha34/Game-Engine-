package com.mygame.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private JFrame frame;
    private BufferedImage image;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;

    public Window(gamecontainer gc) {

        image = new BufferedImage(
                gc.getWidth(),
                gc.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        canvas = new Canvas();

        Dimension dimension = new Dimension(
                (int) (gc.getWidth() * gc.getScale()),
                (int) (gc.getHeight() * gc.getScale())
        );

        canvas.setPreferredSize(dimension);
        canvas.setMaximumSize(dimension);
        canvas.setMinimumSize(dimension);

        canvas.setFocusable(true);
        canvas.requestFocus();

        frame = new JFrame(gc.getTitle());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // IMPORTANT: ensure focus works after frame is visible
        canvas.requestFocusInWindow();

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
    }

    public BufferedImage getImage() {
        return image;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void update() {
        BufferStrategy bs = canvas.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();

        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);

        g.dispose();
        bs.show();
    }
}