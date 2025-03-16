package com.mygdx.platformer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.platformer.sound.AudioManager;
import com.mygdx.platformer.sound.SoundType;

public class GameButton extends TextButton {

    private boolean hover;

    public GameButton(String text, Skin skin) {
        super(text, skin);
        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hover = true;
                Gdx.graphics.setSystemCursor(SystemCursor.Hand);
                AudioManager.playSound(SoundType.BUTTONHOVER);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hover = false;
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                AudioManager.playSound(SoundType.BUTTONCLICK);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }
        });
    }

    @Override
    public Color getColor() {
        return hover ? Color.RED : super.getColor();
    }

}
