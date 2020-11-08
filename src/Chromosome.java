import java.util.ArrayList;
import java.util.Arrays;

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

    public void incrementFitnessScore() {
        this.fitnessScore++;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    public int[] getGenes() {
        return genes;
    }

}