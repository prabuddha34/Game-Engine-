package com.mygame.engine;

import com.mygame.game.gamemanager;

import java.awt.event.KeyEvent;

public class gamecontainer implements Runnable {

    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private AbstractGame game;


    private boolean running = false;
    private final double UPDATE_CAP = 1.0 / 60.0;

    private int width = 320;
    private int height = 240;
    private float scale = 4f;
    private String title = "Welcome to my Game Engine";

    public String getTitle() {
        return title;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public gamecontainer(gamemanager gamemanager) {
       this.game=gamemanager;

    }

    public Input getInput() {
        return input;
    }

    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);

        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        running = true;

        double firstTime;
        double lastTime = System.nanoTime() / 1_000_000_000.0;
        double passedTime;
        double unProcessedTime = 0;

        double frameTime = 0;
        int frames = 0;

        while (running) {

            firstTime = System.nanoTime() / 1_000_000_000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unProcessedTime += passedTime;
            frameTime += passedTime;

            boolean render = false;

            while (unProcessedTime >= UPDATE_CAP) {
                unProcessedTime -= UPDATE_CAP;
                render = true;

                game.update(this,(float)UPDATE_CAP);
                System.out.println("x:"+" "+ input.getMouseX()+" "+"y:"+" "+input.getMouseY());


                input.update();

                if (input.isKey(KeyEvent.VK_E)) {
                    System.out.println("E is pressed");
                }

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    frames = 0;
                }
            }

            if (render) {
                renderer.clear();
                game.render(this,renderer);
                renderer.drawText("FPS: "+frames,10,10,0xff00ffff);

                window.update();
                frames++;
            }
        }

        dispose();
    }

    public void stop() {

    }

    private void dispose() {

    }

    public Window getWindow() {
        return window;
    }
}