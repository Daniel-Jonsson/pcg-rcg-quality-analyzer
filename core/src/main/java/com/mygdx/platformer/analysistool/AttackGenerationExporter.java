package com.mygdx.platformer.analysistool;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;
import com.mygdx.platformer.attacks.movement.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AttackGenerationExporter {

    private static final int COMPOUND_COUNT = 20;
    private static final int ATTACKS_PER_COMPOUND = 5;
    private static final int GENERATIONS = 10;
    private static final Random random = new Random();

    private static final String SONAR_SCANNER_PATH = "C:\\sonarscanner\\sonar-scanner-7.1.0.4889-windows-x64\\bin\\sonar-scanner.bat";

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

        runJacocoReport();

        CoverageParser.extractCoverageToCSV();

    }

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


    private static NecromancerAttackTemplate createRandomAttack() {
        int damage = random.nextInt(10, 30);
        float speed = random.nextFloat(1.0f, 5.0f);
        MovementPatternBehavior pattern = createRandomMovement();
        return new NecromancerAttackTemplate(45, speed, damage, 5, pattern);
    }

    private static MovementPatternBehavior createRandomMovement() {
        return switch (random.nextInt(4)) {
            case 0 -> new StraightMovement();
            case 1 -> new ZigZagMovement();
            case 2 -> new AccelerateMovement();
            case 3 -> new MixedMovement();
            default -> new StraightMovement();
        };
    }

    private static NecromancerAttackTemplate cloneAttack(NecromancerAttackTemplate original) {
        // clone the instance
        return new NecromancerAttackTemplate(
            45,
            original.getSpeed(),
            original.getDamage(),
            5,
            original.getMovementPattern()
        );
    }


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
