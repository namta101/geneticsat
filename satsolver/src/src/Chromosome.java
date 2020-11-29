package src;

import java.util.ArrayList;
import java.util.Random;

public class Chromosome {

    private int numberOfGenes;
    private double fitnessScore;
    private int[] genes;
    private Formula formula;

    private double MUTATION_RATE = 0.06;

    public Chromosome(int numberOfGenes, Formula formula) {
        this.formula = formula;
        this.numberOfGenes = numberOfGenes;
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

    // Return the number of clauses matched
    public int getClausesMatched() {
        int numberOfClausesMatched = 0;
        for (Clause clause : formula.getClauses()) {
            int[] variables = clause.getVariables();
            boolean isClauseSatisfied = false;
            for (int var : variables) {
                if (var > 0) { // If the variable is positive, and the binary string bit that connects to that  variable is 1,
                    if (genes[var - 1] == 1) { // then the clause is satisfied. (The variable 1 connects to the first bit of the string)
                        isClauseSatisfied = true;
                        break;
                    }
                } else { //
                    if (genes[Math.abs(var + 1)] == 0) { // If the variable is negative, then a variable -1, we will need to connect to
                        isClauseSatisfied = true; // the first bit of the string. If the first bit of the string is 0, then clause satisfied
                        break;                    //(If variable is -5 then it will connect to the 4th bit of the string)

                    }
                }
            }
            if (isClauseSatisfied) {
                numberOfClausesMatched++;
            }
        }
        return numberOfClausesMatched;
    }

    public void assignFitnessScore() {
        this.fitnessScore = getClausesMatched();
    }

    public void mutate() {
        if (shouldMutate()) {
            randomSelectionMutate(this.genes);
        }
    }


    public int[] randomSelectionMutate(int[] genes) {
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