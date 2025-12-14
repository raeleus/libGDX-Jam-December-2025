package com.ray3k.castleevania.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.tommyettinger.textra.FWSkin;

import static com.ray3k.castleevania.Main.*;

public class Screen1 extends ScreenAdapter {
    private Stage stage;
    private FWSkin skin;
    private FitViewport viewport;

    @Override
    public void show() {
        viewport = new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT);
        stage = new Stage(viewport, batch);
        skin = new FWSkin(Gdx.files.internal("skin.json"));

        var root = new Table();
        root.setFillParent(true);
        root.setTouchable(Touchable.enabled);
        stage.addActor(root);
        com.ray3k.castleevania.Utils.onClick(root, () -> {
            main.setScreen(new Screen1());
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GREEN);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
