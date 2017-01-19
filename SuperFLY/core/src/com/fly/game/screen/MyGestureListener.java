package com.fly.game.screen;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class MyGestureListener extends InputAdapter {
    private Hero hero;
    private OrthographicCamera camera;

    public MyGestureListener(Hero hero, OrthographicCamera camera) {
        this.hero = hero;
        this.camera = camera;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 input = new Vector3(screenX, screenY, 0);
        camera.unproject(input);

        if (input.x < camera.position.x) {
            hero.left = true;
        }
        if (input.x > camera.position.x) {
            hero.right = true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        hero.left = false;
        hero.right = false;
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
