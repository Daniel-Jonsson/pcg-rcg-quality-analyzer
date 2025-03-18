package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * In autoplay, this task stops the player and performs a jump to
 * dodge an incoming projectile.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class DodgeTask extends LeafTask<Player> {
    private boolean jumpTriggered = false;

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Task.Status execute() {

        Player player = getObject();
         if (player.isGrounded() && !jumpTriggered) {
             player.stop();
            player.jump();
            player.setDodging(true);
            jumpTriggered = true;
            return Task.Status.RUNNING;
        }
        if (!player.isGrounded()) {
            return Task.Status.RUNNING;
        }


        if (jumpTriggered && player.isGrounded()) {
            jumpTriggered = false;
            player.setDodging(false);
            return Task.Status.SUCCEEDED;
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
        return new DodgeTask();
    }
}
