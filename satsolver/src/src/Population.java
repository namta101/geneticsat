package src;

import java.util.ArrayList;

/**
 * This class represents the set of solutions (chromosomes), and is responsible for producing new solutions
 * It is also responsible for deciding the population size and elitism rate
 */
public class Population {
    public static final int POPULATION_SIZE = 100;
    public static final double ELITISM_RATE = 0.95; // Proportion of chromosomes that stay on to next generation
    private ArrayList<Chromosome> chromosomes;
    private Formula formula;
    private int numberOfVariables;
    private int[] satisfyingSolution;
    private double currentGenerationHighestFitnessScore = 0;
    private boolean generationImproved = false;
    private double currentGenerationTotalFitnessScore;
    private Mutator mutator;
    private ParentSelector parentSelector;
    private ParentCrossover parentCrossover;


    public Population(Formula formula, int numberOfVariables, Mutator mutator) {
        chromosomes = new ArrayList<>();
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        this.mutator = mutator;
        parentSelector = new ParentSelector();
        parentCrossover = new ParentCrossover(formula, numberOfVariables, mutator);
    }

    /**
     * Initialises the population with the same number of chromosomes as the POPULATION_SIZE
     * Each chromosome will automatically create its own solution(genes) and calculate its own fitness score
     */
    public void initialisePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            chromosomes.add(new Chromosome(numberOfVariables, formula, mutator));
        }
    }

    /**
     * Clears the population by assigning the class variable chromosomes to a new ArrayList
     */
    public void clearPopulation(){
        chromosomes = new ArrayList<>();
    }

    /**
     * Checks whether there is a satisfying solution by comparing each chromosome's fitness core with the formula size
     * If there is, will assign the class variable satisfying solution to this chromosome's genes
     */
    public boolean isSatisfied() {
        try {
            for (Chromosome chromosome : chromosomes) {
                if (chromosome.getFitnessScore() == formula.getSize()) { // Checks it is equal to the number of clauses
                    satisfyingSolution = chromosome.getGenes();
                    return true;
                }
            }
        } catch(Exception e) {
            System.out.println("Could not check if solution is satisfied, aborting");
            throw e;
        }

        return false;
    }

    /**
     * Moves on to the next generation by creating a new population
     */
    public void nextPopulation() {
        setCurrentGenerationTotalFitnessScore();
        sortPopulationByFitnessValue(this.chromosomes);

        // Used by the solver to check if the solutions are improving
        double previousGenerationHighestFitnessScore = currentGenerationHighestFitnessScore;
        currentGenerationHighestFitnessScore = getCurrentMostSatisfyingSolutionFitnessScore();
        generationImproved = currentGenerationHighestFitnessScore > previousGenerationHighestFitnessScore;

        // Undergoes parent selection and crossover
        chromosomes = createNewPopulation();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            chromosomes.get(i).clearFitnessScore();
            chromosomes.get(i).mutate();
            chromosomes.get(i).assignFitnessScore();
        }

    }

    /**
     * Creates a new population by copying over a portion of the old population and creating new chromosomes
     * by using parent selection and crossover
     */
    private ArrayList<Chromosome> createNewPopulation() {

        ArrayList<Chromosome> newPopulation = new ArrayList<>();

        try {
            parentSelector.setUpForGeneration(chromosomes);

            // Keeps number of parents depending on ELITISM_RATE
            double individualsCloned = Math.ceil(ELITISM_RATE * (double) (POPULATION_SIZE));
            double individualsCreated = (double) POPULATION_SIZE - individualsCloned;

            for (int i = 0; i < individualsCloned; i++) {
                Chromosome clonedIndividual = chromosomes.get(i);
                newPopulation.add(clonedIndividual);
            }

            for (int i = 0; i < individualsCreated; i++) {
                Chromosome clonedIndividual = applyCrossover();
                newPopulation.add(clonedIndividual);
            }

            return newPopulation;

        } catch(Exception e) {
            System.out.println("Error creating new generation, randomly generating new population");
            System.out.println("Error message: " + e);
            this.chromosomes = new ArrayList<>();
            initialisePopulation();
            return this.chromosomes;
        }

    }

    /**
     * Creates a chromosome by choosing two parents to crossover
     */
    private Chromosome applyCrossover() {
        try {
            int[] parentOneGenes = parentSelector.chooseParent(chromosomes, currentGenerationTotalFitnessScore).getGenes();
            int[] parentTwoGenes = parentSelector.chooseParent(chromosomes, currentGenerationTotalFitnessScore).getGenes();
            int lengthOfGenes = parentOneGenes.length;

            Chromosome offSpring = parentCrossover.crossover(parentOneGenes, parentTwoGenes, lengthOfGenes);
            return offSpring;
        } catch(Exception e) {
            System.out.println("Error in applying crossover." + "Error: " + e);
            System.out.println("Returning a new chromosome as new offspring");
            Chromosome defaultChromosome = new Chromosome(numberOfVariables, formula, mutator);
            return defaultChromosome;
        }
    }

    /**
     * Returns the chromosomes in the population
     */
    public ArrayList<Chromosome> getChromosomes(){
        return this.chromosomes;
    }

    /**
     * Sets the population's chromosomes
     */
    public void setChromosomes(ArrayList<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    /**
     * Returns the most satisfying solution found yet
     */
    public int[] getCurrentMostSatisfyingSolution(){
        sortPopulationByFitnessValue(this.chromosomes);
        return chromosomes.get(0).getGenes();
    }

    /**
     * Returns the fitness score of the most satisfying solution found yet
     */
    public double getCurrentMostSatisfyingSolutionFitnessScore(){
        sortPopulationByFitnessValue(this.chromosomes);
        return chromosomes.get(0).getFitnessScore();
    }

    /**
     * Totals up every chromosome's fitness score to find a total for the population, then sets it to the class variable
     */
    public void setCurrentGenerationTotalFitnessScore() {
        this.currentGenerationTotalFitnessScore = getTotalPopulationFitnessScore();
    }

    /**
     * Returns the total of every chromosome's fitness score
     */
    public double getCurrentGenerationTotalFitnessScore() {
        return this.currentGenerationTotalFitnessScore;
    }

    /**
     * Returns the total population's fitness score
     */
    private double getTotalPopulationFitnessScore() {
        double total = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            total = total + chromosomes.get(i).getFitnessScore();
        }
        return total;
    }

    /**
     * Sorths the population by their fitness score in descending order
     */
    public void sortPopulationByFitnessValue(ArrayList<Chromosome> chromosomes) {

        chromosomes.sort((Chromosome c1, Chromosome c2) -> {
            if (c1.getFitnessScore() > c2.getFitnessScore())
                return -1;
            if (c1.getFitnessScore() < c2.getFitnessScore())
                return 1;
            return 0;

        });
    }

    /**
     * Returns the found satisfying solution (will be null if not found yet)
     */
   public int[] getSatisfyingSolution() {
        return this.satisfyingSolution;
   }

    /**
     * Returns whether the generation is improved from last generation, measured by whether the top fitness score of
     * an individual chromosome is improved or not
     */
   public boolean getGenerationImproved(){
        return generationImproved;
   }

}

