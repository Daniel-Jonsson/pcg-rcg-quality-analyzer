package com.mygdx.platformer.utilities;

public final class AppConfig {

    /** Pixels per meter **/
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

    public static final int PLAYER_SPAWN_Y = 1;

    public static final float MAX_FRAME_TIME = 0.25f;

    public static final float PLAYER_MOVE_SPEED = 7f;

    public static final float PLAYER_JUMP_FORCE = 5f;

    public static final float PLAYER_WIDTH = 0.5f;

    public static final float PLAYER_HEIGHT = 0.7f;
}
