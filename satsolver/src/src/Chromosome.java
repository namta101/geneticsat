package src;

// This class represents one possible solution to the SAT problem
// The answer lies within the genes
public class Chromosome {

    private final int numberOfGenes;
    private double fitnessScore;
    private int[] genes;
    private final Formula formula;
    private final Mutator mutator;

    private final double MUTATION_RATE = 0.1;

    public Chromosome(int numberOfGenes, Formula formula, Mutator mutator) {
        this.formula = formula;
        this.numberOfGenes = numberOfGenes;
        this.mutator = mutator;
        initialiseGenes();
        assignFitnessScore();

    }

    // Creates the solution for the chromosome
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

    // Sets fitness score to the number of clauses matched
    public void assignFitnessScore() {
        this.fitnessScore = formula.getClausesMatched(genes);
    }

    public void mutate() {
        if (shouldMutate()) {
            mutator.mutate(this.genes);
        }
    }

    public boolean shouldMutate() {
        double randomNumber = Math.random();
        return (randomNumber <= MUTATION_RATE);
    }

    public void intakeParentsGenes(int[] parentsGenes) {
        this.genes = parentsGenes;
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