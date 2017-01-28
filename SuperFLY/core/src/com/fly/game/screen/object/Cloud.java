package com.fly.game.screen.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.fly.game.SuperFly.PPM;

public class Cloud extends Sprite {
    public Body b2body;
    private World word;


    public Cloud(World world, Rectangle rect) {
        super(new Texture("storm.png"));
        this.word = world;
        setBounds(0, 0, getWidth() * 2 / PPM, getHeight() * 2 / PPM);
        define(rect);
    }

    public void update(Batch batch) {
        draw(batch);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    private void define(Rectangle rect) {
        BodyDef bdef = new BodyDef();
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = word.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 1;
        fdef.density = 20;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(40 / PPM, 40 / PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }
}
