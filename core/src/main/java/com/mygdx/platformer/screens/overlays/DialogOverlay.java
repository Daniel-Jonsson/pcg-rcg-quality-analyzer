package com.mygdx.platformer.screens.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.ui.GameButton;
import com.mygdx.platformer.utilities.Assets;

/**
 * A static utility class for displaying dialog overlays in the game.
 * <p>
 * This class provides a centralized system for creating and managing modal
 * dialogs
 * that can display messages, errors, and user guides to the player. It handles:
 * <ul>
 * <li>Creating and positioning dialogs on screen</li>
 * <li>Managing input focus while dialogs are active</li>
 * <li>Supporting both standard and scrollable content for longer text</li>
 * <li>Providing specialized dialog types (standard, error, fatal)</li>
 * <li>Handling dialog lifecycle (show, hide, render, dispose)</li>
 * </ul>
 * </p>
 * <p>
 * The class uses LibGDX's Scene2D UI system for rendering and input handling.
 * All methods are static as this is a utility class that maintains its own
 * internal state.
 * </p>
 * <p>
 * Usage example:
 *
 * <pre>
 * // Show a standard dialog
 * DialogOverlay.show("Information", "This is a message for the player.");
 *
 * // Show an error that will exit the game when dismissed
 * DialogOverlay.showFatal("Critical Error", "The game cannot continue due to a fatal error.");
 * </pre>
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class DialogOverlay {
    /** The Scene2D stage used for rendering and input handling. */
    private static Stage stage;

    /** The currently active dialog instance. */
    private static Dialog currentDialog;

    /** The UI skin used for styling dialog components. */
    private static Skin skin;

    /** Flag indicating whether a dialog is currently being displayed. */
    private static boolean isActive = false;

    /** Stores the previous input processor to restore when dialog is closed. */
    private static InputProcessor previousInputProcessor;

    /**
     * Flag indicating whether the application should exit when dialog is dismissed.
     */
    private static boolean isFatal = false;

    /**
     * Initializes the dialog overlay system.
     * <p>
     * Creates the Stage with a ScreenViewport and loads the UI skin.
     * This method is called automatically when showing a dialog,
     * but can be called manually for preloading resources.
     * </p>
     */
    public static void initialize() {
        if (stage == null) {
            stage = new Stage(new ScreenViewport());
            skin = new Skin(Gdx.files.internal(Assets.UI_PATH));
        }
    }

    /**
     * Shows a dialog with the specified title and description.
     * <p>
     * This is the core method that creates and displays a dialog with customizable
     * parameters. It handles:
     * <ul>
     * <li>Capturing and redirecting input to the dialog</li>
     * <li>Creating the dialog UI with appropriate styling</li>
     * <li>Setting up special formatting for user guides (scrollable content)</li>
     * <li>Positioning the dialog in the center of the screen</li>
     * </ul>
     *
     * </p>
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

        boolean isUserGuide = title.equals(AppConfig.USER_GUIDE_TITLE);

        currentDialog.setWidth(AppConfig.ERROR_DIALOG_WIDTH * AppConfig.ERROR_DIALOG_SCROLL_SCALE);
        currentDialog.setHeight(AppConfig.ERROR_DIALOG_HEIGHT);

        Label descriptionLabel = new Label(description, skin);
        descriptionLabel.setWrap(true);

        if (isUserGuide) {
            descriptionLabel.setAlignment(Align.left);

            Table contentTable = new Table();
            contentTable.add(descriptionLabel).width(AppConfig.ERROR_DIALOG_WIDTH * AppConfig.ERROR_DIALOG_SCROLL_SCALE)
                    .padLeft(AppConfig.ERROR_DIALOG_PADDING_LEFT);

            com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scrollPane = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(
                    contentTable, skin);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setOverscroll(false, false);
            scrollPane.setScrollbarsVisible(true);

            currentDialog.getContentTable().add(scrollPane)
                    .width(AppConfig.ERROR_DIALOG_WIDTH * AppConfig.ERROR_DIALOG_SCROLL_SCALE)
                    .height(AppConfig.ERROR_DIALOG_HEIGHT * AppConfig.ERROR_DIALOG_SCROLL_WIDTH)
                    .padTop(AppConfig.ERROR_DIALOG_PADDING_TOP);
        } else {
            descriptionLabel.setAlignment(Align.center);
            currentDialog.getContentTable().add(descriptionLabel)
                    .width(AppConfig.ERROR_DIALOG_WIDTH)
                    .padTop(AppConfig.ERROR_DIALOG_PADDING_TOP);
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
     * <p>
     * This is a convenience method for showing a non-fatal dialog with the default
     * "OK" button.
     * </p>
     *
     * @param title       The title of the dialog
     * @param description The message to display
     */
    public static void show(String title, String description) {
        showDialog(title, description, AppConfig.OK_BUTTON_TEXT, false);
    }

    /**
     * Shows an error dialog with the specified title and description.
     * <p>
     * This method creates a dialog specifically for displaying error messages.
     * The dialog uses the default "OK" button text and can optionally exit the
     * application when dismissed.
     * </p>
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
     * <p>
     * This is a convenience method for showing an error dialog that will always
     * terminate the application when the user dismisses it. Use this for critical
     * errors that prevent the game from continuing.
     * </p>
     *
     * @param title       The title of the error dialog
     * @param description The error message to display
     */
    public static void showFatal(String title, String description) {
        showError(title, description, true);
    }

    /**
     * Hides the dialog if it's currently shown.
     * <p>
     * This method removes the current dialog from the stage, resets the dialog
     * state,
     * and restores the previous input processor. It's called automatically when
     * the user dismisses a dialog, but can also be called programmatically.
     * </p>
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
     * <p>
     * This method should be called in the render method of the screen that uses
     * the dialog overlay. It updates the stage and draws all actors, including
     * the current dialog if one is active.
     * </p>
     */
    public static void render() {
        if (isActive && stage != null) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    /**
     * Updates the stage viewport when the screen is resized.
     * <p>
     * This method should be called from the resize method of the screen that uses
     * the dialog overlay. It updates the viewport dimensions and repositions
     * the dialog to keep it centered on the screen.
     * </p>
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
     * <p>
     * This method releases all resources used by the dialog system, including
     * the stage and skin. It should be called when transitioning away from screens
     * that use dialogs or when the application is shutting down.
     * </p>
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
     * <p>
     * This can be used to determine if user input should be processed by the game
     * or if it's being captured by an active dialog.
     * </p>
     *
     * @return true if a dialog is active, false otherwise
     */
    public static boolean isActive() {
        return isActive;
    }
}
