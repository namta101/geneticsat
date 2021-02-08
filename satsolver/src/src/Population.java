package src;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    public static final int POPULATION_SIZE = 300;
    public static final double ELITISM_RATE = 0.8;
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


    public Population(Formula formula, int numberOfVariables) {
        chromosomes = new ArrayList<>();
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        mutator = new Mutator(formula);
        parentSelector = new ParentSelector();
        parentCrossover = new ParentCrossover(formula, numberOfVariables, mutator);
    }

    public void initialisePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            chromosomes.add(new Chromosome(numberOfVariables, formula, mutator));
        }
    }

    public void clearPopulation(){
        chromosomes = new ArrayList<>();
    }

    // Checks whether there is a satisfying solution, if there is, assigns it to the
    // field variable satisfying solution
    public boolean isSatisfied() {
        for (Chromosome chromosome : chromosomes) {
            if (chromosome.getFitnessScore() == formula.getSize()) {
                satisfyingSolution = chromosome.getGenes();
                return true;
            }
        }
        return false;
    }

    public void nextPopulation() {
        setCurrentGenerationTotalFitnessScore();
        sortPopulationByFitnessValue(this.chromosomes);

        double previousGenerationHighestFitnessScore = currentGenerationHighestFitnessScore;
        currentGenerationHighestFitnessScore = chromosomes.get(0).getFitnessScore();
        generationImproved = currentGenerationHighestFitnessScore > previousGenerationHighestFitnessScore;

        chromosomes = createNewPopulation();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            chromosomes.get(i).clearFitnessScore();
            chromosomes.get(i).mutate();
            chromosomes.get(i).assignFitnessScore();
            // sortPopulationByFitnessValue();
        }

    }

    private ArrayList<Chromosome> createNewPopulation() {

        ArrayList<Chromosome> newPopulation = new ArrayList<>();

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

    }

    // Creates a chromosome by choosing two parents to crossover
    private Chromosome applyCrossover() {

        int[] parentOneGenes = parentSelector.chooseParent(chromosomes, currentGenerationTotalFitnessScore).getGenes();
        int[] parentTwoGenes = parentSelector.chooseParent(chromosomes, currentGenerationTotalFitnessScore).getGenes();
        int lengthOfGenes = parentOneGenes.length;

        Chromosome offSpring = parentCrossover.crossover(parentOneGenes, parentTwoGenes, lengthOfGenes);
        return offSpring;
    }

    public ArrayList<Chromosome> getChromosomes(){
        return this.chromosomes;
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

