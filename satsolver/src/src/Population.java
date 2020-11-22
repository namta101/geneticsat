package src;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    public static final int POPULATION_SIZE = 291;
    private double ELITISM_RATE = 0.8;
    private ArrayList<Chromosome> chromosomes;
    private Formula formula;
    private int numberOfVariables;
    private int[] satisfyingSolution;
    private double currentGenerationTotalFitnessScore;
    private double [] rouletteWheel;
    private double [] rankBoard;
    private GACombination.ParentSelection parentSelectionMethod;

    public Population(Formula formula, int numberOfVariables) {
        chromosomes = new ArrayList<>();
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        setCombinationOfGAMethodsToUse();
        rouletteWheel = new double[POPULATION_SIZE];
        rankBoard = new double[(POPULATION_SIZE + 10 - 1) / 10];
    }

    public void setCombinationOfGAMethodsToUse() {
        parentSelectionMethod = GACombination.ParentSelection.RouletteWheel;
        // If we choose rank selection, we want to  be able to create enough ranks for this to be feasible
        if (parentSelectionMethod == GACombination.ParentSelection.Rank && POPULATION_SIZE < 300 ) {
            parentSelectionMethod = GACombination.ParentSelection.RouletteWheel;
        }
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
        currentGenerationTotalFitnessScore = totalPopulationFitnessScore();
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

        this.rouletteWheel = createRouletteWheel();
        this.rankBoard = createRankBoard();
        chooseRank();

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
        switch (parentSelectionMethod) {
            case RouletteWheel:
                return rouletteWheelSelection();

            default:
                return rouletteWheelSelection();
        }
    }

    // Select a pair of chromosomes to crossover
    private Chromosome rouletteWheelSelection() {
        int indexOfChromosomeToChoose = 0;
        Random rand = new Random();
        double positionOnRouletteWheel = 1 + ((currentGenerationTotalFitnessScore - 1) * rand.nextDouble()); // values from 1 to total
        double currentTotal = 0;
        for (int i = 0; i < rouletteWheel.length; i++) {
            currentTotal = currentTotal + rouletteWheel[i];
            if (currentTotal >= positionOnRouletteWheel) {
                indexOfChromosomeToChoose = i;
                break;
            }

        }

        return chromosomes.get(indexOfChromosomeToChoose);

    }


    public double[] createRouletteWheel() {
        double[] newRouletteWheel = new double[POPULATION_SIZE];
        double rouletteTotal = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double chromosomeFitness = chromosomes.get(i).getFitnessScore();
            rouletteTotal = rouletteTotal + chromosomeFitness;
            newRouletteWheel[i] = rouletteTotal;
        }

        return newRouletteWheel;
    }

    public double[] createRankBoard() {
        int groupSize = 10;
        int numberOfRanks = (POPULATION_SIZE + groupSize - 1 ) / groupSize; // round the integer up
        double[] rankBoard = new double[numberOfRanks];
        int rank = 0;
        for(int i = 0; i<POPULATION_SIZE-groupSize; i += groupSize) { //Do not calculate final rank
            for(int j=0; j<10; j++) {
                rankBoard[rank] = rankBoard[rank] + chromosomes.get(i).getFitnessScore();
            }
            rank++;
        }

        int numberOfMembersInLastRank = POPULATION_SIZE%groupSize;

        if(numberOfMembersInLastRank != 0) { // means number of members is not equal to groupsize
            for (int i = 0; i < numberOfMembersInLastRank; i++) {
                rankBoard[numberOfRanks - 1] = rankBoard[numberOfRanks - 1] + chromosomes.get(POPULATION_SIZE - groupSize + i).getFitnessScore();
            }
            double averageFitness = rankBoard[numberOfRanks-1] / numberOfMembersInLastRank;
            rankBoard[numberOfRanks-1] = averageFitness * 10;
        }  else {
            for(int i =0; i< groupSize; i++) {
                rankBoard[numberOfRanks - 1] = rankBoard[numberOfRanks - 1] + chromosomes.get(POPULATION_SIZE - groupSize + i).getFitnessScore();
            }
        }
        return rankBoard;
    }

    public int chooseRank() {
        Random rand = new Random();
        double positionOfRank = 1 + ((currentGenerationTotalFitnessScore - 1) * rand.nextDouble());
        int indexOfRankToChoose = 0;
        double currentTotal = 0;
        for(int i = 0; i<rankBoard.length; i++) {
            currentTotal = currentTotal + rankBoard[i];
            if(currentTotal>=positionOfRank) {
                indexOfRankToChoose = i;
                break;
            }
        }

        return indexOfRankToChoose;

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

}

