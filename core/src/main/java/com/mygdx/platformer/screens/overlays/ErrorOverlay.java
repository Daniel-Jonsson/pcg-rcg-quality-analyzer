package com.mygdx.platformer.screens.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.utilities.AppConfig;

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

    public static void show(String title, String description) {
        if (stage == null) {
            initialize();
        }

        previousInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(stage);

        if (currentDialog != null) {
            currentDialog.remove();
        }

        currentDialog = new Dialog(title, skin) {
            @Override
            protected void result(Object object) {
                ErrorOverlay.hide();
            }
        };

        TextButton okButton = new TextButton(AppConfig.OK_BUTTON_TEXT, skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        Label descriptionLabel = new Label(description, skin);
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        currentDialog.getContentTable().add(descriptionLabel).width(AppConfig.ERROR_DIALOG_WIDTH).padTop(AppConfig.ERROR_DIALOG_PADDING);

        currentDialog.button(okButton);

        currentDialog.setPosition(
            (stage.getWidth() - currentDialog.getWidth()) / 2,
            (stage.getHeight() - currentDialog.getHeight()) / 2);

        stage.addActor(currentDialog);

        isActive = true;
    }

    public static void show(String description) {
        show("Error", description);
    }

    /**
     * Hides the error dialog if it's currently shown.
     */
    public static void hide() {
        if (isActive && currentDialog != null) {
            currentDialog.hide();
            currentDialog.remove();
            currentDialog = null;
            isActive = false;
            if (previousInputProcessor != null) {
                Gdx.input.setInputProcessor(previousInputProcessor);
            }
        }
    }

    public static void render() {
        if (isActive) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public static void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }

        if (currentDialog != null && isActive) {
            currentDialog.setPosition(
                (stage.getWidth() - currentDialog.getWidth()) / 2,
                (stage.getHeight() - currentDialog.getHeight()) / 2
            );
        }
    }
        
}
