package com.fly.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fly.game.screen.PlayScreen;

public class SuperFly extends Game {
    public static final int HEIGHT = 1280;
    public static final int WIDTH = 720;

    public static final float PPM = 100;

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
