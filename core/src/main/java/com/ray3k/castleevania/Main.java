package com.ray3k.castleevania;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;import com.badlogic.gdx.audio.Music;import com.badlogic.gdx.graphics.Color;import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;import com.esotericsoftware.spine.SkeletonJson;import com.esotericsoftware.spine.SkeletonRenderer;import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public TwoColorPolygonBatch batch;
    public SkeletonRenderer skeletonRenderer;
    public SkeletonJson skeletonJson;

    public Music bgm;

    @Override
    public void create() {
        batch = new TwoColorPolygonBatch(32767);
        bgm = Gdx.audio.newMusic(Gdx.files.internal(""));
        skeletonRenderer = new SkeletonRenderer();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
@Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
