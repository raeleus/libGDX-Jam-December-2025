package com.ray3k.castleevania;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ray3k.castleevania.screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final float WINDOW_WIDTH = 1920;
    public static final float WINDOW_HEIGHT = 1080;
    public static TwoColorPolygonBatch batch;
    public static TextureAtlas textureAtlas;
    public static SkeletonRenderer skeletonRenderer;
    public static SkeletonJson skeletonJson;
    public static Main main;

    public static Music bgm;

    @Override
    public void create() {
        main = this;

        batch = new TwoColorPolygonBatch(32767);
        textureAtlas = new TextureAtlas(Gdx.files.internal("textures.atlas"));

        bgm = Gdx.audio.newMusic(Gdx.files.internal("bgm.ogg"));
        bgm.setLooping(true);
        bgm.setVolume(.6f);

        skeletonRenderer = new SkeletonRenderer();
        skeletonJson = new SkeletonJson(textureAtlas);
        setScreen(new SplashScreen());
    }

    @Override
    public void resize(int width, int height) {
        if (width * height == 0) return;
        super.resize(width, height);
    }
}
