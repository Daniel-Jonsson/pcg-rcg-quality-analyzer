package com.mygdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameButton extends TextButton {

    private boolean hover;
    private Skin skin;

    public GameButton(String text, Skin skin) {
        super(text, skin);
        this.skin = skin;
        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hover = true;
                Gdx.graphics.setSystemCursor(SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hover = false;
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }
        });
    }
    
    @Override
    public Color getColor() {
        return hover ? skin.getColor("red") : super.getColor();
    }
    
}
