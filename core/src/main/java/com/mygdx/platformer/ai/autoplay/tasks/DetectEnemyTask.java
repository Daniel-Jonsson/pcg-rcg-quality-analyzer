package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

public class DetectEnemyTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        Player player = getObject();

        if (player.getBody().getLinearVelocity().x < 0.1 && player.getBody().getLinearVelocity().y < 0.1 && player.hasEnemiesNearby(-1)) {
            player.setFacingRight(false);
            return Status.SUCCEEDED;
        }

        if (player.hasEnemiesNearby(1)) {
            player.setFacingRight(true);
            return Status.SUCCEEDED;
        }
        return Status.RUNNING;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new DetectEnemyTask();
    }
}
