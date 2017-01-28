package com.fly.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fly.game.SuperFly;
import com.fly.game.TextureMapObjectRenderer;
import com.fly.game.WorldContactListener;
import com.fly.game.screen.object.Cloud;
import com.fly.game.screen.object.Coin;

import java.util.ArrayList;
import java.util.List;

import static com.fly.game.SuperFly.HEIGHT;
import static com.fly.game.SuperFly.PPM;
import static com.fly.game.SuperFly.WIDTH;

public class PlayScreen extends ScreenAdapter {

    public static final float MAX_HERO_MOVE_SPEED = 440 / PPM;
    public static final float HERO_VELOCITY = 45 / PPM;
    public static final float HERO_RELEASE = 12 / PPM;
    public static final float HERO_SPEED = 630f / PPM;

    private SuperFly game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private final Texture bg;

    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;

    private com.fly.game.screen.object.Hero hero;

    private boolean play = false;
    private List<Coin> coins = new ArrayList<Coin>();
    private List<Cloud> clouds = new ArrayList<Cloud>();

    public PlayScreen(SuperFly game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WIDTH / PPM, HEIGHT / PPM, camera);
        camera.position.set(viewport.getWorldWidth() / 2, 0, 0);

        bg = new Texture("bg.png");

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        loadLevel();
        hero = new com.fly.game.screen.object.Hero(world);
        world.setContactListener(new WorldContactListener());
        Gdx.input.setInputProcessor(new com.fly.game.MyGestureListener(hero, camera));
    }

    private void loadLevel() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("firstLevel.tmx");
        mapRenderer = new TextureMapObjectRenderer(map, 1 / PPM);
        createB2Object(map);
    }


    private void createB2Object(TiledMap tiled) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        for (MapObject object : tiled.getLayers().get("wall").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);
            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);
        }

        for (MapObject object : tiled.getLayers().get("dodj").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);
            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef).setUserData("dodj");
        }

       /* for (MapObject object : tiled.getLayers().get("line").getObjects().getByType(PolylineMapObject.class)) {
            Polyline polyline = ((PolylineMapObject) object).getPolyline();
            ChainShape chainShape = new ChainShape();
            float[] vertices = new float[polyline.getVertices().length];
            for (int i = 0; i < polyline.getVertices().length; i++) {
                vertices[i] = polyline.getVertices()[i] / PPM;
            }
            chainShape.createChain(vertices);

            FixtureDef fixtureDef2 = new FixtureDef();
            fixtureDef2.shape = chainShape;

            BodyDef bodyDef2 = new BodyDef();
            bodyDef2.active = false;
            bodyDef2.type = BodyDef.BodyType.StaticBody;
            bodyDef2.position.set(polyline.getX() / PPM, polyline.getY() / PPM);

            Body body = world.createBody(bodyDef2);
            body.createFixture(fixtureDef2);
        }*/

        createCoins(tiled);
        createCloud(tiled);
    }

    private void createCoins(TiledMap tiled) {
        for (MapObject object : tiled.getLayers().get("coins").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            coins.add(new Coin(world, rect));
        }
    }


    private void createCloud(TiledMap tiled) {
        for (MapObject object : tiled.getLayers().get("storm").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            clouds.add(new Cloud(world, rect));
        }
    }

    @Override
    public void render(float delta) {
        // System.out.println("FPS: " + (1 / delta));
        hero.update();
        if (!hero.isDead) {
            handleInput(delta);
            camera.position.x = hero.getX() + 80 / PPM;
            camera.position.y = hero.getY() + 540 / PPM;
            camera.update();
        } else {
            play = false;
        }

        mapRenderer.setView(camera);

        world.step(delta, 4, 4);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(bg, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.draw(bg, 0, viewport.getWorldHeight());
        game.batch.draw(bg, viewport.getWorldWidth(), 0);
        game.batch.draw(bg, 0, 0);

        hero.draw(game.batch);

        for (Coin coin : coins) {
            coin.update(game.batch);
        }

        for (Cloud cloud : clouds) {
            cloud.update(game.batch);
        }

        game.batch.end();
        mapRenderer.render();

        b2dr.render(world, camera.combined);

        if (hero.isDead && hero.b2body.getPosition().y < camera.position.y / 2) {
            world.destroyBody(hero.b2body);
            hero = new com.fly.game.screen.object.Hero(world);
            Gdx.input.setInputProcessor(new com.fly.game.MyGestureListener(hero, camera));
            for (Coin coin : coins) {
                coin.collect();
            }
            createCoins(map);
        }
    }

    private void handleInput(float dl) {
        if (Gdx.input.justTouched() && !play) {
            play = true;
            hero.run();

            for (Cloud cloud : clouds) {
                cloud.b2body.setLinearVelocity(new Vector2(MathUtils.random(-0.5f, 0.5f), MathUtils.random(-0.5f, 0.5f)));
            }
        }

        right(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || hero.right);
        left(Gdx.input.isKeyPressed(Input.Keys.LEFT) || hero.left);
    }

    private void left(boolean keyPressed) {
        if (keyPressed && hero.b2body.getLinearVelocity().x >= -MAX_HERO_MOVE_SPEED) {
            hero.b2body.applyLinearImpulse(new Vector2(-HERO_VELOCITY, 0), hero.b2body.getWorldCenter(), true);
        } else if (hero.b2body.getLinearVelocity().x < 0) {
            if (hero.b2body.getLinearVelocity().x < HERO_RELEASE) {
                hero.b2body.applyLinearImpulse(new Vector2(HERO_RELEASE, 0), hero.b2body.getWorldCenter(), true);
            } else {
                hero.b2body.applyLinearImpulse(new Vector2(hero.b2body.getLinearVelocity().x / 2, 0), hero.b2body.getWorldCenter(), true);
            }
        }
    }

    private void right(boolean keyPressed) {
        if (keyPressed && hero.b2body.getLinearVelocity().x <= MAX_HERO_MOVE_SPEED) {
            hero.b2body.applyLinearImpulse(new Vector2(HERO_VELOCITY, 0), hero.b2body.getWorldCenter(), true);
        } else if (hero.b2body.getLinearVelocity().x > 0) {
            if (hero.b2body.getLinearVelocity().x > HERO_RELEASE) {
                hero.b2body.applyLinearImpulse(new Vector2(-HERO_RELEASE, 0), hero.b2body.getWorldCenter(), true);
            } else {
                hero.b2body.applyLinearImpulse(new Vector2(-hero.b2body.getLinearVelocity().x / 2, 0), hero.b2body.getWorldCenter(), true);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        bg.dispose();
        game.dispose();
    }
}
