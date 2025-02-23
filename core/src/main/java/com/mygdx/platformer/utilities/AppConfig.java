package com.mygdx.platformer.utilities;

public final class AppConfig {

    private AppConfig() { }  // private constructor prevents instantiation

    /** Pixels per meter. **/
    public static final float PPM = 50f;

    public static final int SCREEN_WIDTH_PIXELS = 1280;

    public static final int SCREEN_HEIGHT_PIXELS = 720;

    /** The default screen width. **/
    public static final float SCREEN_WIDTH = SCREEN_WIDTH_PIXELS / PPM;

    /** The default screen height. **/
    public static final float SCREEN_HEIGHT = SCREEN_HEIGHT_PIXELS / PPM;

    public static final float TIME_STEP = 1 / 60f;

    public static final float TIME_SCALE = 1.5f;

    public static final int VELOCITY_ITERATIONS = 6;

    public static final int POSITION_ITERATIONS = 2;

    public static final float GRAVITY = -20f;

    public static final int PLAYER_SPAWN_X = 1;

    public static final int PLAYER_SPAWN_Y = 4;

    public static final float MAX_FRAME_TIME = 0.25f;

    public static final float PLAYER_MOVE_SPEED = 7f;

    public static final float PLAYER_JUMP_FORCE = 2f;

    public static final long MAX_JUMP_HOLD_TIME = 200L;

    public static final float JUMP_HOLD_FORCE = 15f;

    public static final float PLAYER_WIDTH = 0.5f;

    public static final float PLAYER_HEIGHT = 0.7f;

    public static final float PLAYER_HITBOX_SCALE = 0.9f;

    public static final float PLAYER_MASS = 0.35f;

    /* PCG parameters*/

    public static final float MIN_GAP = 1.0f;

    public static final float MAX_GAP = 3.0f;

    public static final float MIN_WIDTH = 3.0f;

    public static final float MAX_WIDTH = 6.0f;

    public static final float PLATFORM_HEIGHT = 0.5f;

    public static final float BASE_Y = 2.5f;

    public static final float MAX_Y_VARIATION = 0.5f;

    public static final float RIGHT_OFFSCREEN_MARGIN = 2.0f;

    public static final float FIRST_PLATFORM_WIDTH = 8f;
    public static final float FIRST_PLATFORM_X = 4f;

    /* UI */

    public static final int BUTTON_WIDTH = 250;
    public static final int BUTTON_HEIGHT = 50;
}
