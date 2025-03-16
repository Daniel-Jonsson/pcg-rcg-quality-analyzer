package com.mygdx.platformer.ai.enemy;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.ai.enemy.tasks.AttackTask;
import com.mygdx.platformer.ai.enemy.tasks.CharacterInRangeTask;
import com.mygdx.platformer.ai.enemy.tasks.PursueTask;
import com.mygdx.platformer.ai.enemy.tasks.PatrolTask;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * An AI agent that controls enemy behavior using a behavior tree.
 * <p>
 * This class implements enemy AI using a hierarchical behavior tree with three
 * main behaviors:
 * <ol>
 * <li>Attack the player when in attack range</li>
 * <li>Pursue the player when detected but not in attack range</li>
 * <li>Patrol the area when no player is detected</li>
 * </ol>
 * </p>
 * <p>
 * The behavior tree is evaluated at fixed intervals to optimize performance.
 * The agent handles detection ranges, attack cooldowns, and coordinates with
 * the attack manager to spawn enemy attacks.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class EnemyAIAgent extends AIAgent {
    /** Manager responsible for spawning and handling attacks. */
    private AttackManager attackManager;

    /** The cooldown time between attacks in seconds. */
    private float attackCooldown;

    /** The fixed time interval between behavior tree updates in seconds. */
    private static final float UPDATE_INTERVAL = AppConfig.ENEMY_BT_UPDATE_INTERVAL;

    /** Accumulates time between updates to control update frequency. */
    private float timeAccumulator = 0f;

    /**
     * Creates a new enemy AI agent with the specified parameters.
     *
     * @param character      The enemy character this agent controls
     * @param detectionRange The maximum distance at which the enemy can detect the
     *                       player
     * @param attackRange    The maximum distance at which the enemy can attack the
     *                       player
     * @param attackManager  The manager responsible for spawning attacks
     * @param attackCooldown The cooldown time between attacks in seconds
     * @param world          The Box2D world for physics and ray casting
     */
    public EnemyAIAgent(BaseCharacter character, float detectionRange, float attackRange, AttackManager attackManager,
            float attackCooldown, World world) {
        super(character, detectionRange, attackRange, world);
        this.attackManager = attackManager;
        this.attackCooldown = attackCooldown;
        setupBehaviorTree();
    }

    /**
     * Updates the AI agent by updating the target distance and resetting the
     * behavior tree and stepping it once again to see if it needs to pursue the
     * target or attack.
     *
     * @param deltaTime The time since the last update.
     */
    @Override
    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        if (timeAccumulator >= UPDATE_INTERVAL) {
            updateTargetDistance();
            if (behaviorTree != null) {
                behaviorTree.resetTask();
                behaviorTree.step();
            }
            timeAccumulator = 0f;
        }
    }

    /**
     * Sets up the tree hierarchy of the enemy Behavior Tree AI.
     * <p>
     * Creates a behavior tree with the following priority-based structure:
     * <ol>
     * <li>Attack sequence: Check if target is in attack range, then attack</li>
     * <li>Pursue sequence: Check if target is in detection range, then pursue</li>
     * <li>Patrol task: Default behavior when no target is detected</li>
     * </ol>
     * </p>
     */
    private void setupBehaviorTree() {
        Selector<AIAgent> root = new Selector<>();

        Sequence<AIAgent> attackSequence = new Sequence<>();
        attackSequence.addChild(new CharacterInRangeTask(true));
        attackSequence.addChild(new AttackTask(attackCooldown, attackManager));

        Sequence<AIAgent> pursueSequence = new Sequence<>();
        pursueSequence.addChild(new CharacterInRangeTask(false));
        pursueSequence.addChild(new PursueTask());

        root.addChild(attackSequence);
        root.addChild(pursueSequence);
        root.addChild(new PatrolTask());

        behaviorTree = new BehaviorTree<>(root);
        behaviorTree.setObject(this);
    }
}
