package com.ray3k.castleevania.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.github.tommyettinger.textra.FWSkin;
import com.github.tommyettinger.textra.TypingLabel;
import com.ray3k.castleevania.SpineImage;

import static com.ray3k.castleevania.Main.*;

public class Screen3 extends ScreenAdapter {
    private Stage stage;
    private FWSkin skin;
    private FitViewport viewport;
    private SpineImage playerImage;
    private SpineImage backgroundImage;
    private int mode = 0;
    private float amount;
    private Music slapSound;
    private TypingLabel label;

    @Override
    public void show() {
        slapSound = Gdx.audio.newMusic(Gdx.files.internal("sfx/slap.ogg"));

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

        var skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("scene3.json"));
        var animationData = new AnimationStateData(skeletonData);
        backgroundImage = new SpineImage(skeletonRenderer, skeletonData, animationData);
        backgroundImage.setCrop(0, 0, 1920, 1080);
        backgroundImage.setTouchable(Touchable.disabled);
        stack.add(backgroundImage);

        var skeleton = backgroundImage.getSkeleton();
        var animation = backgroundImage.getAnimationState();

        var container = new Container<>();
        stack.add(container);

        label = new TypingLabel("", skin);
        label.setWrap(true);
        container.setActor(label);
        container.padLeft(439);
        container.padRight(500);
        container.padTop(63);
        container.padBottom(594);
        container.top().left();
        container.fill();

        animation.setAnimation(0, "hold", true);
        animation.setAnimation(1, "fade-in", false);
        animation.addListener(new AnimationStateAdapter() {
            @Override
            public void event(TrackEntry entry, Event event) {
                if (event.getData().getAudioPath() != null) {
                    var music = Gdx.audio.newMusic(Gdx.files.internal(event.getData().getAudioPath()));
                    music.play();
                } else if (event.getString() != null) {
                    label.restart(event.getString().replace("\\n", "\n"));
                }
            }
        });

        skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("dmitrii.json"));
        animationData = new AnimationStateData(skeletonData);
        playerImage = new SpineImage(skeletonRenderer, skeletonData, animationData);
        playerImage.spineDrawable.drawWithoutScale = true;
        playerImage.clip = false;
        stage.addActor(playerImage);
        playerImage.setPosition(1890, 117);

        skeleton = playerImage.getSkeleton();
        skeleton.setScale(.5f, .5f);
        animation = playerImage.getAnimationState();
        animation.getData().setDefaultMix(.5f);

        animation.setAnimation(0, "walk", true);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("x + \" \" + y = " + x + " " + y);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        var skeleton = playerImage.getSkeleton();
        var animation = playerImage.getAnimationState();

        if (mode == 0) {
            playerImage.setX(playerImage.getX() - 100 * delta);
            if (playerImage.getX() < 1467) {
                mode++;
                playerImage.setX(1467);
                playerImage.getAnimationState().setAnimation(0, "stand", true);

                backgroundImage.getAnimationState().setAnimation(0, "speech", false);
                backgroundImage.getAnimationState().addListener(new AnimationStateAdapter() {
                    @Override
                    public void complete(TrackEntry entry) {
                        if (mode == 1) {
                            mode++;
                            backgroundImage.getAnimationState().setAnimation(0, "fight-start", false);
                            backgroundImage.getAnimationState().addAnimation(0, "fight-loop", true, 0);

                            playerImage.remove();
                            label.setVisible(false);
                        }
                    }
                });
                bgm.setVolume(.2f);
            }
        } else if (mode == 1) {
            //do nothing
        } else if (mode == 2) {
            if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                amount += .05f;
                slapSound.play();
                slapSound.setPosition(0);
            }
            amount -= .01f * delta;
            if (amount < 0) amount = 0;
            if (amount >= 1) {
                amount = 1;
                mode++;
                backgroundImage.getAnimationState().setAnimation(0, "fight-death", false);
                backgroundImage.getAnimationState().addAnimation(0, "skeleton", false, 2);
                slapSound.stop();
                label.setVisible(true);
                label.setText("");

                backgroundImage.getAnimationState().addListener(new AnimationStateAdapter() {
                    @Override
                    public void complete(TrackEntry entry) {
                        if (entry.getAnimation().getName().equals("skeleton")) nextScreen();
                    }
                });
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
        main.setScreen(new Screen1());
    }
}
