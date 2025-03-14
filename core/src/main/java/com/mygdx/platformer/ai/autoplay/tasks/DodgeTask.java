package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;
import com.mygdx.platformer.utilities.AppConfig;

public class DodgeTask extends LeafTask<Player> {
    private boolean jumpTriggered = false;
    private static final float tolerance = AppConfig.AUTO_PLAY_PLATFORM_DETECTION_TOLERANCE;

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Task.Status execute() {

        Player player = getObject();
         if (player.isGrounded() && !jumpTriggered) {
             System.out.println("DodgeTask");
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
        return new JumpTask();
    }
}
