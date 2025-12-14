package com.ray3k.castleevania.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.AnimationStateData;
import com.github.tommyettinger.textra.FWSkin;
import dev.lyze.gdxUnBox2d.UnBox;

import static com.ray3k.castleevania.Main.*;
import static com.ray3k.castleevania.Utils.*;

public class Screen1 extends ScreenAdapter {
    private Stage stage;
    private FWSkin skin;
    private FitViewport viewport;

    @Override
    public void show() {
        viewport = new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT);
        stage = new Stage(viewport, batch);
        skin = new FWSkin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);

        var root = new Table();
        root.setFillParent(true);
        root.setTouchable(Touchable.enabled);
        stage.setKeyboardFocus(root);
        stage.addActor(root);

        var skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("scene1.json"));
        var animationData = new AnimationStateData(skeletonData);
        var spineImage = new com.ray3k.castleevania.SpineImage(skeletonRenderer, skeletonData, animationData);
        spineImage.setCrop(0, 0, 1920, 1080);
        spineImage.setTouchable(Touchable.disabled);
        root.add(spineImage);

        var skeleton = spineImage.getSkeleton();
        var animation = spineImage.getAnimationState();

        animation.setAnimation(0, "flash", true);

        onAnyKeyDown(root, this::nextScreen);
        onClick(root, this::nextScreen);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void nextScreen() {
        
    }
}
