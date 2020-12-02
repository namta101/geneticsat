package src;

import java.util.ArrayList;
import java.util.Random;

public class Chromosome {

    private int numberOfGenes;
    private double fitnessScore;
    private int[] genes;
    private Formula formula;
    private Mutator mutator;

    private double MUTATION_RATE = 0.2;

    public Chromosome(int numberOfGenes, Formula formula, Mutator mutator) {
        this.formula = formula;
        this.numberOfGenes = numberOfGenes;
        this.mutator = mutator;
        initialiseGenes();
        assignFitnessScore();

    }

    private void initialiseGenes() {
        genes = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++) {
            if (Math.random() < 0.5) {
                genes[i] = 0;
            } else {
                genes[i] = 1;
            }
        }
    }



    public void assignFitnessScore() {
        this.fitnessScore = formula.getNumberOfClausesMatched(genes);
    }

    public void mutate() {
        if (shouldMutate()) {
            mutator.mutate(this.genes);
        }
    }

    private boolean shouldMutate() {
        double randomNumber = Math.random();
        return (randomNumber <= MUTATION_RATE);

    }

    public void intakeParentsGenes(int[] parentsGenes) {
        this.genes = parentsGenes;
    }

    private void incrementFitnessScore() {
        this.fitnessScore++;
    }

    public void clearFitnessScore() {
        this.fitnessScore = 0;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    public int[] getGenes() {
        return genes;
    }

}