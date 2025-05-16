package com.mygdx.platformer.analysistool;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;
import com.mygdx.platformer.attacks.modifiers.AttackModifier;
import com.mygdx.platformer.attacks.modifiers.PulseModifier;
import com.mygdx.platformer.attacks.movement.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Entry point and utility for generating, exporting, and analyzing procedurally
 * and recombinantly generated attack compounds.
 * <p>
 * This class orchestrates the creation of multiple generations of
 * {@link NecromancerAttackTemplate} compounds using both
 * Procedural Content Generation (PCG) and Reuse-based Content
 * Generation (RCG) methods. It serializes these
 * generated attack patterns into Java source files using
 * {@link AttackExporter}, organizing them in a structured output
 * directory for later static analysis.
 * <p>
 * The exported files are used to gather data on code complexity as part of the
 * research and analysis pipeline. This enables automated, large-scale
 * evaluation of generated attack logic.
 * </p>
 *
 * <h2>Workflow</h2>
 * <ol>
 * <li>Generates an initial set of PCG compounds and exports them.</li>
 * <li>Iteratively generates new RCG generations from previous ones, exporting
 * each generation.</li>
 * <li>Optionally generates additional standalone PCG generations for
 * comparison.</li>
 * <li>Runs static analysis tools (e.g., SonarQube) on the generated
 * codebase.</li>
 * </ol>
 *
 * <h2>Output Structure</h2>
 * <ul>
 * <li>out/generated/{method}/gen{generation}/compound_{compoundId}/NecromancerAttack_{attackId}.java</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * 
 * <pre>
 * // Run from the command line or as a main class to generate and export attack
 * // compounds.
 * AttackGenerationExporter.main(args);
 * </pre>
 *
 * @see NecromancerAttackTemplate
 * @see AttackExporter
 * @see com.mygdx.platformer.analysistool.CoverageParser
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class AttackGenerationExporter {

    private static final int COMPOUND_COUNT = 20;
    private static final int ATTACKS_PER_COMPOUND = 5;
    private static final int GENERATIONS = 10;
    private static final Random random = new Random();

    private static final String SONAR_SCANNER_PATH = "C:\\sonarscanner\\sonar-scanner-7.1.0.4889-windows-x64\\bin\\sonar-scanner.bat";

    /**
     * Main entry point for generating, exporting, and analyzing attack compounds.
     * <p>
     * This method generates an initial set of PCG compounds, then iteratively
     * generates
     * RCG generations from previous ones, exporting each generation using
     * {@link AttackExporter}.
     * It also generates additional standalone PCG generations for comparison, and
     * runs
     * static analysis tools (e.g., SonarQube) on the generated codebase.
     * <p>
     * Code coverage analysis and coverage export are available but commented out by
     * default.
     * </p>
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        List<List<NecromancerAttackTemplate>> pcgGen0 = generatePCG();
        AttackExporter.exportCompounds(pcgGen0, 0, "PCG");

        List<List<NecromancerAttackTemplate>> rcgPrevGen = pcgGen0;
        for (int gen = 1; gen <= GENERATIONS; gen++) {
            rcgPrevGen = generateRCG(rcgPrevGen);
            AttackExporter.exportCompounds(rcgPrevGen, gen, "RCG");
        }

        for (int gen = 1; gen <= GENERATIONS; gen++) {
            List<List<NecromancerAttackTemplate>> standalonePCG = generatePCG();
            AttackExporter.exportCompounds(standalonePCG, gen, "PCG");
        }

        runSonarScanner();

        // runJacocoReport();

        // CoverageParser.extractCoverageToCSV();

    }

    /**
     * Generates a list of attack compounds using Procedural Content Generation
     * (PCG).
     * <p>
     * Each compound is a list of randomly created {@link NecromancerAttackTemplate}
     * objects,
     * representing a unique attack pattern.
     * </p>
     *
     * @return A list of PCG-generated attack compounds.
     */
    private static List<List<NecromancerAttackTemplate>> generatePCG() {
        List<List<NecromancerAttackTemplate>> compounds = new ArrayList<>();

        for (int i = 0; i < COMPOUND_COUNT; i++) {
            List<NecromancerAttackTemplate> attacks = new ArrayList<>();
            for (int j = 0; j < ATTACKS_PER_COMPOUND; j++) {
                attacks.add(createRandomAttack());
            }
            compounds.add(attacks);
        }
        return compounds;
    }

    /**
     * Generates a new generation of attack compounds using Reuse-based
     * Content Generation (RCG).
     * <p>
     * Each new compound is created by copying a host compound from the previous
     * generation and
     * replacing one of its attacks with a randomly selected attack from a donor
     * compound.
     * </p>
     *
     * @param previousGeneration The previous generation of attack compounds to
     *                           recombine.
     * @return A new list of RCG-generated attack compounds.
     */
    private static List<List<NecromancerAttackTemplate>> generateRCG(List<List<NecromancerAttackTemplate>> previousGeneration) {
        List<List<NecromancerAttackTemplate>> newGeneration = new ArrayList<>();

        for (int i = 0; i < COMPOUND_COUNT; i++) {
            // select the host
            List<NecromancerAttackTemplate> host = previousGeneration.get(i);

            // make a copy of the attack list
            List<NecromancerAttackTemplate> newCompound = new ArrayList<>(host);

            // Choose which attack to replace with a new attack from the donor
            int indexToReplace = random.nextInt(ATTACKS_PER_COMPOUND);

            // select random donor and random attack from that donor
            List<NecromancerAttackTemplate> donorCompound = previousGeneration.get(random.nextInt(previousGeneration.size()));
            NecromancerAttackTemplate donorAttack = donorCompound.get(random.nextInt(donorCompound.size()));

            // replace the old attack with the new one
            newCompound.set(indexToReplace, cloneAttack(donorAttack));

            newGeneration.add(newCompound);
        }

        return newGeneration;
    }


    /**
     * Creates a single {@link NecromancerAttackTemplate} with randomized
     * parameters.
     * <p>
     * The attack's damage, speed, movement pattern, and modifier are randomly
     * selected.
     * </p>
     *
     * @return A randomly generated attack template.
     */
    private static NecromancerAttackTemplate createRandomAttack() {
        int damage = random.nextInt(10, 30);
        float speed = random.nextFloat(1.0f, 5.0f);
        MovementPatternBehavior pattern = createRandomMovement();
        AttackModifier modifier = createRandomAttackModifier();
        return new NecromancerAttackTemplate(45, speed, damage, 5, pattern, modifier);
    }

    /**
     * Randomly selects and creates a movement pattern for an attack.
     *
     * @return A randomly chosen {@link MovementPatternBehavior} instance.
     */
    private static MovementPatternBehavior createRandomMovement() {
        return switch (random.nextInt(4)) {
            case 0 -> new StraightMovement();
            case 1 -> new ZigZagMovement();
            case 2 -> new AccelerateMovement();
            case 3 -> new MixedMovement();
            default -> new StraightMovement();
        };
    }

    /**
     * Randomly selects and creates an attack modifier.
     * <p>
     * May return {@code null} if no modifier is selected.
     * </p>
     *
     * @return A randomly chosen {@link AttackModifier}, or {@code null}.
     */
    private static AttackModifier createRandomAttackModifier() {
        return switch (random.nextInt(2)) {
            case 0 -> new PulseModifier(2f, 0.5f);
            case 1 -> null;
            default -> null;
        };
    }

    /**
     * Creates a deep copy of a given {@link NecromancerAttackTemplate}.
     *
     * @param original The attack template to clone.
     * @return A new {@link NecromancerAttackTemplate} with the same properties as
     *         the original.
     */
    private static NecromancerAttackTemplate cloneAttack(NecromancerAttackTemplate original) {
        // clone the instance
        return new NecromancerAttackTemplate(
            45,
            original.getSpeed(),
            original.getDamage(),
            5,
            original.getMovementPattern(), original.getModifier()
        );
    }


    /**
     * Runs the SonarScanner static analysis tool on the generated attack source
     * files.
     * <p>
     * This method executes SonarScanner as an external process, using the output
     * directory
     * as the working directory. It prints the result to the console.
     * </p>
     */
    private static void runSonarScanner() {
        System.out.println("Starting SonarScanner analysis...");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(SONAR_SCANNER_PATH);
            processBuilder.directory(new File("out/generated")); // Run from the generation folder
            processBuilder.inheritIO(); // show information in terminal
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("SonarScanner completed successfully!");
            } else {
                System.err.println("SonarScanner failed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the JaCoCo code coverage report task using Gradle.
     * <p>
     * This method executes the Gradle JaCoCo report task as an external process and
     * prints
     * the result to the console.
     * </p>
     */
    private static void runJacocoReport() {
        System.out.println("Running JaCoCo report task...");

        try {
            ProcessBuilder pb = new ProcessBuilder("gradlew.bat", ":core:jacocoTestReport");
            pb.directory(new File(System.getProperty("user.dir")));
            pb.inheritIO(); // send output to console

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("JaCoCo report completed successfully.");
            } else {
                System.err.println("JaCoCo report failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }






}
