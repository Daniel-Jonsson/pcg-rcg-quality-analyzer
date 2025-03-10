package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task moved the player forward if the path is clear.
 * @author Robert Kullman, Daniel Jönsson
 */
public class MoveForwardTask extends LeafTask<Player> {
    private final OrthographicCamera camera;
    private boolean reachedFrontLimit = false;
    private boolean isMoving = false;

    public MoveForwardTask(OrthographicCamera camera) {
        this.camera = camera;
    }
    @Override
    public Status execute() {

        Player player = getObject();
        Vector2 playerPosition = player.getBody().getPosition();

        float screenCenter = camera.position.x; // Dynamic screen reference
        float minX = screenCenter - 5;
        float maxX = screenCenter + 5;

       // System.out.println("grounded: " + player.isGrounded());
       // System.out.println("ground ahead: " + player.isGroundAhead(player.getDirection()));

        if (player.isGrounded() && !player.isGroundAhead(player.getFacingDirection())) {

            return Status.SUCCEEDED;
        }
        // If player reaches the front limit, stop moving
        if (playerPosition.x >= maxX) {
            reachedFrontLimit = true;

            if (player.getBody().getLinearVelocity().x > 0) {
                player.stop();
                isMoving = false;
            }
            return Status.RUNNING;
        }
        // If player has stopped and moves back to the rear limit, reset front limit and allow movement
        if (reachedFrontLimit && playerPosition.x <= minX) {
            reachedFrontLimit = false;

        }
        // Move forward if allowed, but only call moveForward() once
        if (!reachedFrontLimit && player.getFacingDirection() != 0) {
            //System.out.println("moving forward");
            player.moveForward();
            isMoving = true;
            return Status.RUNNING;
        }
        return Status.RUNNING;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new MoveForwardTask(camera);
    }
}
