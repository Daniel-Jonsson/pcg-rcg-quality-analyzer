package com.mygdx.platformer.screens.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * A static utility class for displaying error messages in the game.
 * This overlay can be shown from anywhere in the game by calling the static methods.
 * 
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class ErrorOverlay {
    private static Stage stage;
    private static Dialog currentDialog;
    private static Skin skin;
    private static boolean isActive = false;
    private static InputProcessor previousInputProcessor;

    /**
     * Initializes the error overlay. This should be called once during the game initialization.
     */
    public static void initialize() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }
}
