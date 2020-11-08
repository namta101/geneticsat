import java.util.ArrayList;
import java.util.Random;

public class Chromosome {

    private int numberOfGenes;
    private double fitnessScore;
    private int[] genes;
    private ArrayList<Clause> formula;

    private final double MUTATION_RATE = 0.7;

    public Chromosome(int numberOfGenes, ArrayList<Clause> formula) {
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
            System.out.print("Mutating" + "\n");
            Random rand = new Random();
            int upperBound = numberOfGenes - 1;
            int positionToMutate = rand.nextInt(upperBound);
            if (genes[positionToMutate] == 0) {
                genes[positionToMutate] = 1;
            } else {
                genes[positionToMutate] = 0;
            }
            
        }
    }

    private boolean shouldMutate() {
        double randomNumber = Math.random();
        return (randomNumber <= MUTATION_RATE);

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