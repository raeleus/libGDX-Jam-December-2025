package com.ray3k.castleevania.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ray3k.castleevania.Main;

import static com.ray3k.castleevania.Main.WINDOW_HEIGHT;
import static com.ray3k.castleevania.Main.batch;

public class Screen1 extends ScreenAdapter {
    private FitViewport viewport;
    private Stage stage;

    @Override
    public void show() {
        viewport = new FitViewport(Main.WINDOW_WIDTH, WINDOW_HEIGHT);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        var root = new Table();
        root.setFillParent(true);
        root.setTouchable(Touchable.enabled);
        stage.addActor(root);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                System.out.println("keycode = " + keycode);
                return true;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
