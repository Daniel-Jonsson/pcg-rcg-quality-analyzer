package com.mygdx.platformer.screens.overlays;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.ui.GameButton;

/**
 * A static utility class for displaying dialog overlays in the game.
 * This class provides common functionality for creating and managing dialogs.
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class DialogOverlay {
    private static Stage stage;
    private static Dialog currentDialog;
    private static Skin skin;
    private static boolean isActive = false;
    private static InputProcessor previousInputProcessor;
    private static boolean isFatal = false;

    /**
     * Initializes the dialog overlay system.
     * This should be called before showing any dialogs.
     */
    public static void initialize() {
        if (stage == null) {
            stage = new Stage(new ScreenViewport());
            skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        }
    }

    /**
     * Shows a dialog with the specified title and description.
     *
     * @param title       The title of the dialog
     * @param description The message to display
     * @param buttonText  The text for the button
     * @param fatal       Whether the application should exit when the dialog is
     *                    dismissed
     */
    public static void showDialog(String title, String description, String buttonText, boolean fatal) {
        initialize();

        isFatal = fatal;
        previousInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(stage);

        if (currentDialog != null) {
            currentDialog.remove();
        }

        currentDialog = new Dialog(title, skin) {
            @Override
            protected void result(Object object) {
                hide();
                if (isFatal) {
                    Gdx.app.exit();
                }
            }
        };

        boolean isUserGuide = title != null && title.equals(AppConfig.USER_GUIDE_TITLE);

        currentDialog.setWidth(AppConfig.ERROR_DIALOG_WIDTH * 1.10f);
        currentDialog.setHeight(AppConfig.ERROR_DIALOG_HEIGHT);

        Label descriptionLabel = new Label(description, skin);
        descriptionLabel.setWrap(true);

        if (isUserGuide) {
            descriptionLabel.setAlignment(Align.left);

            Table contentTable = new Table();
            contentTable.add(descriptionLabel).width(AppConfig.ERROR_DIALOG_WIDTH * 1.10f).padLeft(30);

            com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scrollPane = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(
                    contentTable, skin);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setOverscroll(false, false);
            scrollPane.setScrollbarsVisible(true);

            currentDialog.getContentTable().add(scrollPane)
                    .width(AppConfig.ERROR_DIALOG_WIDTH * 1.10f)
                    .height(AppConfig.ERROR_DIALOG_HEIGHT * 0.7f)
                    .padTop(AppConfig.ERROR_DIALOG_PADDING);
        } else {
            descriptionLabel.setAlignment(Align.center);
            currentDialog.getContentTable().add(descriptionLabel)
                    .width(AppConfig.ERROR_DIALOG_WIDTH)
                    .padTop(AppConfig.ERROR_DIALOG_PADDING);
        }

        GameButton okButton = new GameButton(buttonText, skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                if (isFatal) {
                    Gdx.app.exit();
                }
            }
        });

        currentDialog.getButtonTable().add(okButton)
                .width(AppConfig.BUTTON_WIDTH)
                .height(AppConfig.BUTTON_HEIGHT)
                .padBottom(AppConfig.BUTTON_BOTTOM_PADDING);

        currentDialog.setPosition(
                (stage.getWidth() - currentDialog.getWidth()) / 2,
                (stage.getHeight() - currentDialog.getHeight()) / 2);

        stage.addActor(currentDialog);

        isActive = true;
    }

    /**
     * Shows a standard dialog with the specified title and description.
     *
     * @param title       The title of the dialog
     * @param description The message to display
     */
    public static void show(String title, String description) {
        showDialog(title, description, AppConfig.OK_BUTTON_TEXT, false);
    }

    /**
     * Shows an error dialog with the specified title and description.
     *
     * @param title       The title of the error dialog
     * @param description The error message to display
     * @param fatal       Whether the application should exit when the dialog is
     *                    dismissed
     */
    public static void showError(String title, String description, boolean fatal) {
        showDialog(title, description, AppConfig.OK_BUTTON_TEXT, fatal);
    }

    /**
     * Shows a fatal error dialog that will exit the application when dismissed.
     *
     * @param title       The title of the error dialog
     * @param description The error message to display
     */
    public static void showFatal(String title, String description) {
        showError(title, description, true);
    }

    /**
     * Hides the dialog if it's currently shown.
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

    /**
     * Renders the dialog if it is active.
     * This should be called in the render method of the screen.
     */
    public static void render() {
        if (isActive && stage != null) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    /**
     * Updates the stage viewport when the screen is resized.
     *
     * @param width  The new screen width
     * @param height The new screen height
     */
    public static void resize(int width, int height) {
        if (stage == null)
            return;

        stage.getViewport().update(width, height, true);

        if (currentDialog != null && isActive) {
            currentDialog.setPosition(
                    (stage.getWidth() - currentDialog.getWidth()) / 2,
                    (stage.getHeight() - currentDialog.getHeight()) / 2);
        }
    }

    /**
     * Disposes of resources when the dialog system is no longer needed.
     */
    public static void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }

        if (skin != null) {
            skin.dispose();
            skin = null;
        }
        currentDialog = null;
        isActive = false;
    }

    /**
     * Checks if a dialog is currently active.
     *
     * @return true if a dialog is active, false otherwise
     */
    public static boolean isActive() {
        return isActive;
    }
}
