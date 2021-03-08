package src;

/**
 * Represents one solution in the population - its solution is found in the genes
 */
public class Chromosome {

    private final int numberOfGenes;
    private double fitnessScore;
    private int[] genes;
    private final Formula formula;
    private final Mutator mutator;

    private final double MUTATION_RATE = 0.1;

    /**
     * On creation, creates a random solution which is stored in its genes, and then assigns itself a fitness score
     * depending on the solution compared with formula
     */
    public Chromosome(int numberOfGenes, Formula formula, Mutator mutator) {
        this.formula = formula;
        this.numberOfGenes = numberOfGenes;
        this.mutator = mutator;
        initialiseGenes();
        assignFitnessScore();
    }

    /**
     * Randomly generates the solution to be held by this chromosome
     */
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

    /**
     * Sets fitness score to the number of clauses it matches with the formula
     */
    public void assignFitnessScore() {
        this.fitnessScore = formula.getClausesMatched(genes);
    }

    /**
     * Mutates the genes
     */
    public void mutate() {
        if (shouldMutate()) {
            mutator.mutate(this.genes);
        }
    }

    /**
     * Decides whether to mutate the gene - depends on mutation rate
     */
    public boolean shouldMutate() {
        double randomNumber = Math.random();
        return (randomNumber <= MUTATION_RATE);
    }

    /**
     * Replaces its own genes with another array of genes
     */
    public void intakeParentsGenes(int[] parentsGenes) {
        this.genes = parentsGenes;
    }

    /**
     * Clears its fitness score
     */
    public void clearFitnessScore() {
        this.fitnessScore = 0;
    }

    /**
     * Returns its fitness score
     */
    public double getFitnessScore() {
        return fitnessScore;
    }

    /**
     * Returns its solution
     */
    public int[] getGenes() {
        return genes;
    }

}