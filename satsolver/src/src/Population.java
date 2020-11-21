package src;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    public static final int POPULATION_SIZE = 300;
    private double ELITISM_RATE = 0.8;
    private ArrayList<Chromosome> chromosomes;
    private Formula formula;
    private int numberOfVariables;
    private int[] satisfyingSolution;

    public Population(Formula formula, int numberOfVariables) {
        chromosomes = new ArrayList<>();
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
    }

    public void initialisePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            chromosomes.add(new Chromosome(numberOfVariables, formula));
        }
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

        sortPopulationByFitnessValue(this.chromosomes);
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

    private Chromosome applyCrossover() {

        int[] parentOneGenes = selectParent().getGenes();
        int[] parentTwoGenes = selectParent().getGenes();
        int lengthOfGenes = parentOneGenes.length;

        Chromosome offSpring = uniformCrossover(parentOneGenes, parentTwoGenes, lengthOfGenes);

        return offSpring;
    }

    private Chromosome uniformCrossover(int[] parentOneGenes, int[] parentTwoGenes, int lengthOfGenes) {
        Chromosome offspring = new Chromosome(numberOfVariables, formula);
        int[] offspringGenes = new int[lengthOfGenes];

        for (int i = 0; i < lengthOfGenes - 1; i += 2) {
            if (i == lengthOfGenes) {
                offspringGenes[i] = parentOneGenes[i];
                break;
            }
            offspringGenes[i] = parentOneGenes[i];
            offspringGenes[i + 1] = parentTwoGenes[i + 1];
        }

        offspring.intakeParentsGenes(offspringGenes);
        return offspring;
    }


    private Chromosome selectParent() {
        return rouletteWheelSelection();
    }

    // TODO only create roulette wheel once per generation
    // Select a pair of chromosomes to crossover
    private Chromosome rouletteWheelSelection() {
        // create the roulette wheel
        double[] rouletteWheel = new double[POPULATION_SIZE];
        double rouletteTotal = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double chromosomeFitness = chromosomes.get(i).getFitnessScore();
            rouletteTotal = rouletteTotal + chromosomeFitness;
            rouletteWheel[i] = rouletteTotal;
        }

        int indexOfChromosomeToChoose = 0;
        Random rand = new Random();
        double positionOnRouletteWheel = 1 + ((rouletteTotal - 1) * rand.nextDouble()); // values from 1 to total
        for (int i = 0; i < rouletteWheel.length; i++) {
            if (rouletteWheel[i] >= positionOnRouletteWheel) {
                indexOfChromosomeToChoose = i;
                break;
            }
        }

        return chromosomes.get(indexOfChromosomeToChoose);

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

    private double totalPopulationFitnessScore() {
        double total = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            total = total + chromosomes.get(i).getFitnessScore();
        }
        return total;
    }

    // Sorts the population by their fitness score in descending order
    public void sortPopulationByFitnessValue(ArrayList<Chromosome> chromosomes) {

        // System.out.println("before: ");
        // for (int i = 0; i < POPULATION_SIZE; i++) {
        // System.out.println(population.get(i).getFitnessScore());
        // }
        chromosomes.sort((Chromosome c1, Chromosome c2) -> {
            if (c1.getFitnessScore() > c2.getFitnessScore())
                return -1;
            if (c1.getFitnessScore() < c2.getFitnessScore())
                return 1;
            return 0;

        });
        // System.out.println("after: ");
        // for (int i = 0; i < POPULATION_SIZE; i++) {
        // System.out.println(population.get(i).getFitnessScore());
        // }

    }

   public int[] getSatisfyingSolution() {
        return this.satisfyingSolution;
   }

}

