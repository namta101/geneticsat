import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Chromosome {

    private int numberOfGenes;
    private double fitnessScore;
    private int[] genes;
    private ArrayList<Clause> formula;

    public Chromosome(int numberOfGenes, ArrayList<Clause> formula) {
        this.formula = formula;
        this.numberOfGenes = numberOfGenes;
        genes = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++) {
            if (Math.random() < 0.5) {
                genes[i] = 0;
            } else {
                genes[i] = 1;
            }
        }
        getClausesMatched();
    }

    // Increment fitness score for every clause satisfied by the chromosome
    public void getClausesMatched() {
        for (int i = 0; i < formula.size(); i++) {
            Clause clause = formula.get(i);
            int[] variables = clause.getVariables();
            boolean isClauseSatisfied = false;
            for (int var : variables) {
                if (var > 0) {
                    if (genes[var - 1] == 1) {
                        isClauseSatisfied = true;
                    }
                } else {
                    if (genes[Math.abs(var + 1)] == 0) {
                        isClauseSatisfied = true;
                    }
                }
            }
            if (isClauseSatisfied) {
                incrementFitnessScore();
            }
        }

    }

    public int[] mutate() {
        Random rand = new Random();
        int upperBound = numberOfGenes - 1;
        int positionToMutate = rand.nextInt(upperBound);
        if (genes[positionToMutate] == 0) {
            genes[positionToMutate] = 1;
        } else {
            genes[positionToMutate] = 0;
        }
        return genes;
    }

    private double getMutationRate() {
        return 0.01;
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