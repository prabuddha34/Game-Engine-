package com.mygame.engine;

public abstract  class AbstractGame {
    public abstract void update(gamecontainer gc,float dt);
    public abstract void render(gamecontainer gc,Renderer r);


}
