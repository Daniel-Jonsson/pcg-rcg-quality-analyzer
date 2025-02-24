package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.utilities.AppConfig;

public class GameTimer {
    private float elapsedTime;
    private Stage stage;
    private Label timerLabel;

    public GameTimer() {
        elapsedTime = 0;

        // Create the UI Stage
        stage = new Stage(new ScreenViewport());

        // Load default skin
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        timerLabel = new Label("Time: 00:00", skin);


        Table table = new Table();
        table.top().right();
        table.setFillParent(true);
        table.add(timerLabel).pad(AppConfig.TIMER_PADDING);

        stage.addActor(table);
    }

    public void update(float delta) {
        elapsedTime += delta;

        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        String timeString = String.format("Time: %02d:%02d", minutes, seconds);

        timerLabel.setText(timeString);
    }

    public void render() {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
