package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.characters.player.Player;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * This task moved the player forward if the path is clear.
 * @author Robert Kullman, Daniel Jönsson
 */
public class MoveForwardTask extends LeafTask<Player> {
    private final OrthographicCamera camera;
    private boolean reachedFrontLimit = false;

    /**
     * Constructor for the MoveForwardTask, allowing a camera to be passed as parameter,
     * which is necessary to determing the players relative position.
     * @param camera The game camera.
     */
    public MoveForwardTask(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Status execute() {

        Player player = getObject();
        Vector2 playerPosition = player.getBody().getPosition();

        float screenCenter = camera.position.x; // Dynamic screen reference
        float minX = screenCenter - AppConfig.AUTO_PLAY_BACKWARD_MOVEMENT_LIMIT;
        float maxX = screenCenter + AppConfig.AUTO_PLAY_FORWARD_MOVEMENT_LIMIT;


        if(player.isDodging()) {
            return Status.RUNNING;
        }

        if (player.isGrounded() && !player.isGroundAhead(player.getFacingDirection())) {
            player.moveForward(); // move forward to prepare to jump a gap
            return Status.SUCCEEDED;
        }
        // If player reaches the front limit, stop moving
        if (playerPosition.x >= maxX) {
            reachedFrontLimit = true;

            if (player.getBody().getLinearVelocity().x > 0) {
                player.stop();
            }
            return Status.SUCCEEDED;
        }
        // If player has stopped and moves back to the rear limit, reset front limit and allow movement
        if (reachedFrontLimit && playerPosition.x <= minX) {
            player.moveForward();
            reachedFrontLimit = false;
            return Status.RUNNING;
        }
        // Move forward if allowed, but only call moveForward() once
        if (!reachedFrontLimit && player.getFacingDirection() != 0) {
            player.moveForward();
            return Status.RUNNING;
        }
        return Status.RUNNING;
    }

    /**
     * Copies the task to a new task.
     * @param task the task to be filled.
     * @return The new task.
     */
    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new MoveForwardTask(camera);
    }
}
