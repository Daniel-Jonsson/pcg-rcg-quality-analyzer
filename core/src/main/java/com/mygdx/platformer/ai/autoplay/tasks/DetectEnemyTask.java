package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

public class DetectEnemyTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        Player player = getObject();
        if (player.hasEnemiesNearby(player.getFacingDirection())) {
            return Status.SUCCEEDED;
        }
        return Status.RUNNING;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new DetectEnemyTask();
    }
}
