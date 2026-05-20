package com.mygame.game;

import com.mygame.engine.AbstractGame;
import com.mygame.engine.Renderer;
import com.mygame.engine.gamecontainer;

import java.awt.event.KeyEvent;

public class gamemanager extends AbstractGame {

    // ---------------- PLAYER ----------------

    private float playerX = 160;
    private float playerY = 240;
    private int playerSize = 10;

    private float speed = 100f;

    // ---------------- LIGHT ----------------

    // Fixed lamp position
    private int lightX = 160;
    private int lightY = 240;

    // ---------------- GAME STATE ----------------

    private boolean gameOver = false;
    private boolean won = false;

    // ---------------- TILE ----------------

    private int tileSize = 16;

    // ---------------- MAZE ----------------

    private int[][] maze = {

            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1},
            {1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,1},
            {1,0,1,1,1,1,1,1,0,1,1,1,0,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1},
            {1,1,1,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,1},
            {1,0,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,0,1},
            {1,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    // ---------------- UPDATE ----------------

    @Override
    public void update(gamecontainer gc, float dt) {

        if(gameOver || won) {
            return;
        }

        float newX = playerX;
        float newY = playerY;

        // ---------------- MOVEMENT ----------------

        if(gc.getInput().isKey(KeyEvent.VK_W)) {
            newY -= speed * dt;
        }

        if(gc.getInput().isKey(KeyEvent.VK_S)) {
            newY += speed * dt;
        }

        if(gc.getInput().isKey(KeyEvent.VK_A)) {
            newX -= speed * dt;
        }

        if(gc.getInput().isKey(KeyEvent.VK_D)) {
            newX += speed * dt;
        }

        // ---------------- COLLISION ----------------

        int tileX =
                (int)(newX / tileSize);

        int tileY =
                (int)(newY / tileSize);

        // WALL CHECK
        if(maze[tileY][tileX] != 1) {

            playerX = newX;
            playerY = newY;
        }

        // LIGHT FOLLOWS PLAYER
        lightX = (int)playerX + playerSize / 2;
        lightY = (int)playerY + playerSize / 2;

        // WIN CHECK
        if(maze[tileY][tileX] == 2) {
            won = true;
        }
    }

    // ---------------- RENDER ----------------

    @Override
    public void render(gamecontainer gc, Renderer r) {

        // ---------------- BRIGHT BACKGROUND ----------------

        r.drawFillRect(
                0,
                0,
                gc.getWidth(),
                gc.getHeight(),
                0xff353535
        );

        // ---------------- RETRO GRID ----------------

        for(int y = 0; y < gc.getHeight(); y += 16) {

            for(int x = 0; x < gc.getWidth(); x++) {

                r.setPixel(x, y, 0xff4a4a4a);
            }
        }

        for(int x = 0; x < gc.getWidth(); x += 16) {

            for(int y = 0; y < gc.getHeight(); y++) {

                r.setPixel(x, y, 0xff4a4a4a);
            }
        }

        // ---------------- DRAW MAZE ----------------

        for(int y = 0; y < maze.length; y++) {

            for(int x = 0; x < maze[0].length; x++) {

                // WALL
                if(maze[y][x] == 1) {

                    r.drawFillRect(
                            x * tileSize,
                            y * tileSize,
                            tileSize,
                            tileSize,
                            0xff0066ff
                    );
                }

                // FINISH
                if(maze[y][x] == 2) {

                    r.drawFillRect(
                            x * tileSize,
                            y * tileSize,
                            tileSize,
                            tileSize,
                            0xff00ff00
                    );
                }
            }
        }

        // ---------------- DARKNESS ----------------

        r.drawFillRect(
                0,
                0,
                gc.getWidth(),
                gc.getHeight(),
                0xaa000000
        );

        // ---------------- FIXED LIGHT ----------------

        r.drawLight(
                lightX,
                lightY,
                35
        );

        // ---------------- PLAYER ----------------

        r.drawFillRect(
                (int)playerX,
                (int)playerY,
                playerSize,
                playerSize,
                0xffffffff
        );

        // ---------------- UI ----------------

        r.drawText(
                "ESCAPE",
                10,
                10,
                0xffffffff
        );

        // ---------------- GAME OVER ----------------

        if(gameOver) {
            r.drawText(
                    "DEAD",
                    120,
                    140,
                    0xffff0000
            );

        }

        // ---------------- WIN ----------------

        if(won) {

            r.drawText(
                    "YOU ESCAPED!",
                    90,
                    140,
                    0xff00ff00
            );
        }
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args) {

        gamecontainer gc =
                new gamecontainer(new gamemanager());

        gc.setWidth(320);
        gc.setHeight(320);
        gc.setScale(3f);

        gc.start();
    }
}