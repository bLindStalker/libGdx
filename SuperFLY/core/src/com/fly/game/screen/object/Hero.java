package com.fly.game.screen.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.fly.game.SuperFly.PPM;
import static com.fly.game.SuperFly.WIDTH;
import static com.fly.game.screen.PlayScreen.HERO_SPEED;

public class Hero extends Sprite {
    public Body b2body;
    private World word;
    public boolean isDead = false;
    public boolean left;
    public boolean right;
    public boolean dodj = false;
    public boolean dodjed = false;
    public boolean needChange = true;

    public Texture normalHeroTexture;
    public Texture dodjHeroTexture;

    public Hero(World world) {
        super(new Texture("hero.png"));
        normalHeroTexture = new Texture("hero.png");
        dodjHeroTexture = new Texture("heroDodj.png");
        this.word = world;
        setBounds(0, 0, getWidth() / PPM, getHeight() / PPM);
        defineHero();
    }

    public void update() {
        if (dodjed) {
            if (needChange) {
                setTexture(dodjHeroTexture);
            }
            needChange = false;
        } else {
            if (!needChange) {
                setTexture(normalHeroTexture);
            }
            needChange = true;
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 40 / PPM);

        if (isDead) {
            b2body.applyLinearImpulse(new Vector2(0, -0.3f), b2body.getWorldCenter(), true);
        }
    }

    private void defineHero() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(WIDTH / PPM + 35 / PPM, 290 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = word.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2 - 40 / PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void kill() {
        isDead = true;

        Filter filter = new Filter();
        filter.maskBits = 0;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    public void run() {
        b2body.setLinearVelocity(0, HERO_SPEED);
    }
}
