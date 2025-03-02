package com.mygdx.platformer.utilities;

public final class AppConfig {

    private AppConfig() {
    }  // private constructor prevents instantiation

    /**
     * Pixels per meter.
     **/
    public static final float PPM = 50f;

    public static final int SCREEN_WIDTH_PIXELS = 1280;

    public static final int SCREEN_HEIGHT_PIXELS = 720;

    /**
     * The default screen width.
     **/
    public static final float SCREEN_WIDTH = SCREEN_WIDTH_PIXELS / PPM;

    /**
     * The default screen height.
     **/
    public static final float SCREEN_HEIGHT = SCREEN_HEIGHT_PIXELS / PPM;

    public static final float TIME_STEP = 1 / 60f;

    public static final float TIME_SCALE = 1.5f;

    public static final int VELOCITY_ITERATIONS = 6;

    public static final int POSITION_ITERATIONS = 2;

    public static final float GRAVITY = -20f;

    public static final int PLAYER_SPAWN_X = 1;

    public static final int PLAYER_SPAWN_Y = 4;

    public static final float MAX_FRAME_TIME = 0.25f;

    public static final float PLAYER_MOVE_SPEED = 5f;

    public static final float PLAYER_JUMP_FORCE = 1f;

    public static final float MAX_JUMP_HOLD_TIME = 0.15f;

    public static final float JUMP_HOLD_FORCE = 25f;

    public static final float PLAYER_SCALE = 1.8f;

    public static final float PLAYER_WIDTH = 0.7f;

    public static final float PLAYER_HEIGHT = 0.7f;


    public static final float PLAYER_HITBOX_SCALE = .9f;

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
    public static final int TIMER_PADDING = 20;

    public static final int START_SCREEN_TOP_PADDING = 50;
    public static final int TITLE_BOTTOM_PADDING = 100;
    public static final int BUTTON_BOTTOM_PADDING = 20;

    /* ENEMIES */

    public static final float BASE_SPAWN_PROBABILITY = 0.5f;
    public static final float ENEMY_SPAWN_HEIGHT = 0.5f;

    public static final float ENEMY_MASS = 100f;

    public static final short CATEGORY_PLAYER = 0x0001;   // 0001
    public static final short CATEGORY_ENEMY = 0x0002;    // 0010
    public static final short CATEGORY_PLATFORM = 0x0004; // 0100
    public static final short CATEGORY_ATTACK = 0x0008;   // 1000

    public static final int GOBLIN_ATTACK_POWER = 15;
    public static final int GOBLIN_HEALTH = 30;
    public static final float GOBLIN_SPEED = 1f;
    public static final float GOBLIN_SCALE = 1.5f;
    public static final float GOBLIN_WIDTH = 0.5f;
    public static final float GOBLIN_HEIGHT = 0.5f;
    public static final float GOBLIN_HITBOX_SCALE = 0.5f;



    public static final int NECROMANCER_ATTACK_POWER = 20;
    public static final int NECROMANCER_HEALTH = 20;
    public static final float NECROMANCER_SPEED = .5f;
    public static final float NECROMANCER_SCALE = 3f;
    public static final float NECROMANCER_WIDTH = 0.5f;
    public static final float NECROMANCER_HEIGHT = 0.5f;
    public static final float NECROMANCER_HITBOX_SCALE = 0.5f;

    public static final String QUIT = "Quit";
    public static final String MAIN_MENU = "Main Menu";
    public static final String SURVIVAL_TIME = "Survival Time: ";
    public static final String GAME_OVER = "Game Over";


    /* Attack */

    public static final float ATTACK_SPRITE_HEIGHT = 0.3f;
    public static final float ATTACK_SPRITE_WIDTH = 0.6f;
    public static final int BASE_ATTACK_DEFAULT_DMG = 20;
    public static final int BASE_ATTACK_DEFAULT_SPEED = 25;

    /* Animation */

    public static final float STANDARD_FRAME_DURATION = 0.1f;
    public static final float WALK_FRAME_DURATION = 0.15f;
    public static final float ATTACK_FRAME_DURATION = 0.1f;

    public static final float CHARACTER_Y_OFFSET = 0.3f;


}
