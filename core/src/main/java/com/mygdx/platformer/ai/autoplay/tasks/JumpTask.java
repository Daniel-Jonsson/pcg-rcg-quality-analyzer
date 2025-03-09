package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task attempts to dodge an attack if detected.
 * @author Robert Kullman, Daniel Jönsson.
 */
public class JumpTask extends LeafTask<Player> {
    private boolean jumpTriggered = false;
    @Override
    public Status execute() {
        Player player = getObject();
        float direction = player.getDirection();

        if (!player.isGroundAhead(direction) && player.isGrounded() && !jumpTriggered) {
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
        return Status.RUNNING;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new JumpTask();
    }
}
