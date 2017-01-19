package com.fly.game.screen.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.fly.game.SuperFly.PPM;

public class Coin extends Sprite {
    public Body b2body;
    private World word;
    public boolean collected = false;
    private boolean destroyed = false;


    public Coin(World world, Rectangle rect) {
        super(new Texture("coin.png"));
        this.word = world;
        setBounds(0, 0, getWidth() * 2 / PPM, getHeight() * 2 / PPM);
        define(rect);
    }

    public void update(Batch batch) {
        if (collected) {
            if (!destroyed) {
                word.destroyBody(b2body);
                destroyed = true;
            }
        } else {
            draw(batch);
        }
    }

    private void define(Rectangle rect) {
        BodyDef bdef = new BodyDef();
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = word.createBody(bdef);
        /*MassData data = new MassData();
        b2body.setMassData(data);
*/
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void collect() {
        collected = true;

        Filter filter = new Filter();
        filter.maskBits = 0;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }
}
