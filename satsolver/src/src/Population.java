package src;

import java.lang.reflect.Array;
import java.util.ArrayList;

// This class represents the set of solutions (chromosomes)
// It is responsible for also deciding the population size and elitism rate
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

    // When we add a new chromosome, the chromosome automatically creates its own genes and calculates
    // its own fitness score
    public void initialisePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            chromosomes.add(new Chromosome(numberOfVariables, formula, mutator));
        }
    }

    public void clearPopulation(){
        chromosomes = new ArrayList<>();
    }

    // Checks whether there is a satisfying solution, if there is, assigns it to the
    // field variable satisfyingSolution
    public boolean isSatisfied() {
        for (Chromosome chromosome : chromosomes) {
            if (chromosome.getFitnessScore() == formula.getSize()) { // Checks it is equal to the number of clauses
                satisfyingSolution = chromosome.getGenes();
                return true;
            }
        }
        return false;
    }

    // Creates a new population, undergoing the methods of genetic algorithms
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

    // Creates new population by copying over portion of old population and creating new chromosomes
    // by undergoing parent selection and parent crossover
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

    // Creates a chromosome by choosing two parents to crossover
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

    public ArrayList<Chromosome> getChromosomes(){
        return this.chromosomes;
    }

    public void setChromosomes(ArrayList<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    public int[] getCurrentMostSatisfyingSolution(){
        sortPopulationByFitnessValue(this.chromosomes);
        return chromosomes.get(0).getGenes();
    }

    public double getCurrentMostSatisfyingSolutionFitnessScore(){
        sortPopulationByFitnessValue(this.chromosomes);
        return chromosomes.get(0).getFitnessScore();
    }

    public void setCurrentGenerationTotalFitnessScore() {
        this.currentGenerationTotalFitnessScore = getTotalPopulationFitnessScore();
    }

    public double getCurrentGenerationTotalFitnessScore() {
        return this.currentGenerationTotalFitnessScore;
    }


    private double getTotalPopulationFitnessScore() {
        double total = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            total = total + chromosomes.get(i).getFitnessScore();
        }
        return total;
    }

    // Sorts the population by their fitness score in descending order
    public void sortPopulationByFitnessValue(ArrayList<Chromosome> chromosomes) {

        chromosomes.sort((Chromosome c1, Chromosome c2) -> {
            if (c1.getFitnessScore() > c2.getFitnessScore())
                return -1;
            if (c1.getFitnessScore() < c2.getFitnessScore())
                return 1;
            return 0;

        });
    }

   public int[] getSatisfyingSolution() {
        return this.satisfyingSolution;
   }

   public boolean getGenerationImproved(){
        return generationImproved;
   }

}

