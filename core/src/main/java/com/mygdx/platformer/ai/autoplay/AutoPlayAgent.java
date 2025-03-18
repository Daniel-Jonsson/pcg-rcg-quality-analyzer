package com.mygdx.platformer.ai.autoplay;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Parallel;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.ai.btree.decorator.Repeat;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.platformer.ai.autoplay.tasks.AttackEnemyTask;
import com.mygdx.platformer.ai.autoplay.tasks.DetectEnemyTask;
import com.mygdx.platformer.ai.autoplay.tasks.DetectProjectileTask;
import com.mygdx.platformer.ai.autoplay.tasks.DodgeTask;
import com.mygdx.platformer.ai.autoplay.tasks.JumpTask;
import com.mygdx.platformer.ai.autoplay.tasks.IdleTask;
import com.mygdx.platformer.ai.autoplay.tasks.MoveForwardTask;
import com.mygdx.platformer.characters.player.Player;

/**
 * This class represents the base of the auto-play AI feature. It sets up the
 * behavior tree node structure and runs the BT continuously.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class AutoPlayAgent {
    /**
     * The game camera used for determining player position relative to the
     * viewport.
     */
    OrthographicCamera camera;

    /** The behavior tree that controls the AI decision making process. */
    private final BehaviorTree<Player> behaviorTree;

    /** Timer used to control how often the behavior tree is updated. */
    private float aiStepTimer = 0;

    /**
     * Constructor for the AutoPlayAgent class, which initializes the
     * AI with a behavior tree.
     *
     * @param player The player character which is to be controlled by the AI.
     */
    public AutoPlayAgent(Player player, OrthographicCamera camera) {
        this.camera = camera;
        behaviorTree = new BehaviorTree<>(createTree(), player);
    }

    /**
     * Creates the tree structure for the autoplay AI.
     *
     * @return The root task of the tree.
     */
    private Task<Player> createTree() {
        Selector<Player> root = new Selector<>();
        Parallel<Player> parallel = new Parallel<>();

        // Survival strategy
        Sequence<Player> survivalStrategy = new Sequence<>();
        survivalStrategy.addChild(new DetectProjectileTask());
        survivalStrategy.addChild(new DodgeTask());

        // Combat strategy
        Sequence<Player> combatStrategy = new Sequence<>();
        combatStrategy.addChild(new DetectEnemyTask());
        combatStrategy.addChild(new AttackEnemyTask());

        // Movement strategy
        Sequence<Player> movementStrategy = new Sequence<>();
        movementStrategy.addChild(new MoveForwardTask(camera));
        movementStrategy.addChild(new JumpTask());

        // Add strategies to the behavior tree

        parallel.addChild(survivalStrategy);
        parallel.addChild(combatStrategy);
        parallel.addChild(movementStrategy);

        root.addChild(parallel);
        root.addChild(new IdleTask());

        return new Repeat<>(root); // loop the tree
    }

    /**
     * Updates the AI behavior tree on each time frame.
     *
     * @param delta time since last frame.
     */
    public void update(float delta) {
        aiStepTimer += delta;
        float stepInterval = 0.05f;
        if (aiStepTimer >= stepInterval) {
            aiStepTimer = 0;
            behaviorTree.step();
        }
    }

}
