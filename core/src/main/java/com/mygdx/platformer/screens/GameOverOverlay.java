package com.mygdx.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.PlatformerGame;

public class GameOverOverlay {
    private Stage stage;
    private boolean isActive = false;

    public GameOverOverlay(final PlatformerGame game,
                           float survivalTime) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // UI Components
        Label titleLabel = new Label("Game Over", skin);
        Label timeLabel = new Label("Survival Time: " + String.format("%.2f seconds", survivalTime), skin);

        TextButton restartButton = new TextButton("Main Menu", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new StartScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(20).row();
        table.add(timeLabel).padBottom(30).row();
        table.add(restartButton).width(200).height(50).padBottom(10).row();
        table.add(quitButton).width(200).height(50);

        stage.addActor(table);
    }

    public void show() {
        isActive = true;
    }

    public void hide() {
        isActive = false;
    }

    public void render() {
        if (isActive) {
            stage.act();
            stage.draw();
        }
    }

    public void dispose() {
        stage.dispose();
    }
}
