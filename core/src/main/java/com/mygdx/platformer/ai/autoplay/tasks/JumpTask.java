package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.characters.player.Player;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * This task attempts to dodge an attack if detected.
 * @author Robert Kullman, Daniel Jönsson.
 */
public class JumpTask extends LeafTask<Player> {
    private boolean jumpTriggered = false;
    private static final float tolerance = AppConfig.AUTO_PLAY_PLATFORM_DETECTION_TOLERANCE;

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Status execute() {
        Player player = getObject();
        float direction = player.getFacingDirection();


        Vector2 platformPos = player.getNextPlatformPosition();
        if (platformPos != null && !player.isGrounded()) {
            float playerX = player.getBody().getPosition().x;
            if (Math.abs(playerX - platformPos.x) < tolerance) {

                player.stop();
                return Status.RUNNING;
            }
        }

        if (player.isNotGroundAhead() && player.isGrounded() && !jumpTriggered) {
            player.jump();
            jumpTriggered = true;
            return Status.RUNNING;
        }
        if (!player.isGrounded()) {
            return Status.RUNNING;
        }


        if (jumpTriggered && player.isGrounded()) {
            jumpTriggered = false;
            return Status.SUCCEEDED;
        }
        return Status.SUCCEEDED;
    }

    /**
     * Copies the task to a new task.
     * @param task the task to be filled.
     * @return The new task.
     */
    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new JumpTask();
    }
}
