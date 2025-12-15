package com.ray3k.castleevania.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.AnimationStateData;
import com.github.tommyettinger.textra.FWSkin;
import com.ray3k.castleevania.SpineImage;

import static com.ray3k.castleevania.Main.*;

public class Screen2 extends ScreenAdapter {
    private Stage stage;
    private FWSkin skin;
    private FitViewport viewport;
    private SpineImage playerImage;
    private SpineImage backgroundImage;
    private int mode = 0;
    private float amount;
    private Music crankSound;

    @Override
    public void show() {
        crankSound = Gdx.audio.newMusic(Gdx.files.internal("sfx/drawbridge.ogg"));

        viewport = new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT);
        stage = new Stage(viewport, batch);
        skin = new FWSkin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);

        var root = new Table();
        root.setFillParent(true);
        root.setTouchable(Touchable.enabled);
        stage.setKeyboardFocus(root);
        stage.addActor(root);

        var stack = new Stack();
        root.add(stack).grow();

        var skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("scene2.json"));
        var animationData = new AnimationStateData(skeletonData);
        backgroundImage = new SpineImage(skeletonRenderer, skeletonData, animationData);
        backgroundImage.setCrop(0, 0, 1920, 1080);
        backgroundImage.setTouchable(Touchable.disabled);
        stack.add(backgroundImage);

        var skeleton = backgroundImage.getSkeleton();
        var animation = backgroundImage.getAnimationState();

        animation.setAnimation(0, "hold", true);
        animation.setAnimation(1, "fade-in", false);

        skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("dmitrii.json"));
        animationData = new AnimationStateData(skeletonData);
        playerImage = new SpineImage(skeletonRenderer, skeletonData, animationData);
        playerImage.spineDrawable.drawWithoutScale = true;
        playerImage.clip = false;
        stage.addActor(playerImage);
        playerImage.setY(250);

        skeleton = playerImage.getSkeleton();
        skeleton.setScale(.5f, .5f);
        animation = playerImage.getAnimationState();
        animation.getData().setDefaultMix(.5f);

        animation.setAnimation(0, "stand", true);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("x + \" \" + y = " + x + " " + y);
                return true;
            }
        });
    }

    private static Vector2 temp = new Vector2();

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        var skeleton = playerImage.getSkeleton();
        var animation = playerImage.getAnimationState();

        if (mode == 0) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                var anim = animation.getCurrent(0);
                if (!anim.getAnimation().getName().equals("walk")) animation.setAnimation(0, "walk", true);
                skeleton.setScaleX(-.5f);
                playerImage.setX(playerImage.getX() + 100 * delta);
                if (playerImage.getX() >= 700) {
                    playerImage.setX(700);
                    playerImage.getAnimationState().setAnimation(0, "crank", true);
                    mode++;
                    backgroundImage.getAnimationState().setAnimation(0, "bridge", false);
                    backgroundImage.getAnimationState().getCurrent(0).setTimeScale(0);
                    backgroundImage.getAnimationState().setAnimation(1, "space", true);
                }
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                var anim = animation.getCurrent(0);
                if (!anim.getAnimation().getName().equals("walk")) animation.setAnimation(0, "walk", true);
                skeleton.setScaleX(.5f);
                playerImage.setX(playerImage.getX() - 100 * delta);
            } else {
                var anim = animation.getCurrent(0);
                if (!anim.getAnimation().getName().equals("stand")) animation.setAnimation(0, "stand", true);
            }
        } else if (mode == 1) {
            if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                amount += .05f;
                crankSound.play();
                crankSound.setPosition(0);
            }
            amount -= .01f * delta;
            if (amount < 0) amount = 0;
            backgroundImage.getAnimationState().getCurrent(0).setTrackTime(amount);
            if (amount >= 1) {
                amount = 1;
                mode++;
                playerImage.getAnimationState().setAnimation(0, "stand", true);
                backgroundImage.getAnimationState().setAnimation(1, "hold", false);
            }
        } else if (mode == 2) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                var anim = animation.getCurrent(0);
                if (!anim.getAnimation().getName().equals("walk")) animation.setAnimation(0, "walk", true);
                skeleton.setScaleX(-.5f);
                playerImage.setX(playerImage.getX() + 100 * delta);
                if (playerImage.getX() >= 1800) {
                    playerImage.setX(1800);
                    playerImage.getAnimationState().setAnimation(0, "fade-out", false);
                    mode++;
                    backgroundImage.getAnimationState().setAnimation(1, "fade-out", false);
                    backgroundImage.getAnimationState().addListener(new AnimationStateAdapter() {
                        @Override
                        public void complete(TrackEntry entry) {
                            nextScreen();
                        }
                    });
                }
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                var anim = animation.getCurrent(0);
                if (!anim.getAnimation().getName().equals("walk")) animation.setAnimation(0, "walk", true);
                skeleton.setScaleX(.5f);
                playerImage.setX(playerImage.getX() - 100 * delta);
            } else {
                var anim = animation.getCurrent(0);
                if (!anim.getAnimation().getName().equals("stand")) animation.setAnimation(0, "stand", true);
            }
        }

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
