package com.mygame.engine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private gamecontainer gc;

    private final int NUM_KEYS = 512;
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];

    private static final int NUM_BUTTONS = 5;
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;
    private int scroll;

    public Input(gamecontainer gc) {
        this.gc = gc;

        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        gc.getWindow().getCanvas().addKeyListener(this);
        gc.getWindow().getCanvas().addMouseListener(this);
        gc.getWindow().getCanvas().addMouseMotionListener(this);
        gc.getWindow().getCanvas().addMouseWheelListener(this);
    }

    public void update() {
        scroll = 0;

        for (int i = 0; i < NUM_KEYS; i++) {
            keysLast[i] = keys[i];
        }

        for (int i = 0; i < NUM_BUTTONS; i++) {
            buttonsLast[i] = buttons[i];
        }
    }

    public boolean isKey(int keyCode) {
        return keyCode < NUM_KEYS && keys[keyCode];
    }

    public boolean isKeyDown(int keyCode) {
        return keyCode < NUM_KEYS && keys[keyCode] && !keysLast[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        return keyCode < NUM_KEYS && !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isButton(int button) {
        return button > 0 && button <= NUM_BUTTONS && buttons[button - 1];
    }

    public boolean isButtonDown(int button) {
        return button > 0 && button <= NUM_BUTTONS && buttons[button - 1] && !buttonsLast[button - 1];
    }

    public boolean isButtonUp(int button) {
        return button > 0 && button <= NUM_BUTTONS && !buttons[button - 1] && buttonsLast[button - 1];
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int b = e.getButton() - 1;
        if (b >= 0 && b < NUM_BUTTONS) {
            buttons[b] = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int b = e.getButton() - 1;
        if (b >= 0 && b < NUM_BUTTONS) {
            buttons[b] = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll += e.getWheelRotation();
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getScroll() {
        return scroll;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key < NUM_KEYS) {
            keys[key] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key < NUM_KEYS) {
            keys[key] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
