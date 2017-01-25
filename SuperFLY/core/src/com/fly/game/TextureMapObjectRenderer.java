package com.fly.game;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import static com.fly.game.SuperFly.PPM;

public class TextureMapObjectRenderer extends OrthogonalTiledMapRenderer {
    public TextureMapObjectRenderer(TiledMap map) {
        super(map);
    }

    public TextureMapObjectRenderer(TiledMap map, Batch batch) {
        super(map, batch);
    }

    public TextureMapObjectRenderer(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    public TextureMapObjectRenderer(TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale, batch);
    }

    @Override
    public void renderObjects(MapLayer layer) {
        for (MapObject object : layer.getObjects()) {
            renderObject(object);
        }
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject && object.isVisible()) {
            TextureMapObject textureObject = (TextureMapObject) object;
            batch.draw(
                    textureObject.getTextureRegion(),
                    textureObject.getX() / PPM,
                    textureObject.getY() / PPM,
                    textureObject.getTextureRegion().getRegionWidth() / PPM,
                    textureObject.getTextureRegion().getRegionHeight() / PPM);
        }
    }
}
