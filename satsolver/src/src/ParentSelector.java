package src;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is responsible for the stage of Parent Selection in the genetic algorithm
 */

public class ParentSelector {
    private double[] rouletteWheel;
    private double[] rankBoard;
    private final int numberOfChromosomesInEachRank = 2; // To  be used for rank selection
    private final GACombination.ParentSelection parentSelectionMethod;
    private final Random rand;

    public ParentSelector() {
        parentSelectionMethod = GACombination.ParentSelection.RouletteWheel;
        rand = new Random();
    }

    /**
     * Creates and populates the Roulette Wheel or Rank Board
     */
    public void setUpForGeneration(ArrayList<Chromosome> chromosomes) {
        try{
        switch (parentSelectionMethod.name()) {
            case "RouletteWheel":
                setRouletteWheel(chromosomes);
                break;
            case "Rank":
                setRankBoard(chromosomes);
                break;
            default:
                System.out.println("Error choosing parent selection method, setting up Roulette Wheel");
                setRouletteWheel(chromosomes);
        }

        } catch(Exception e) {
            System.out.print("Exception error in setting up parent selection for generation");
            System.out.print("Exception error: " + e);
            System.out.print("Setting up both Roulette Wheel and Rank Board");
            setRouletteWheel(chromosomes);
            setRankBoard(chromosomes);
        }
    }

    /**
     * Selects the parent from either the Roulette Wheel or Rank Board (Option chosen in constructor)
     */
    public Chromosome chooseParent(ArrayList<Chromosome> chromosomes, double currentGenerationTotalFitnessScore) {
        try {
            switch (parentSelectionMethod) {
                case RouletteWheel:
                    return rouletteWheelSelection(chromosomes, currentGenerationTotalFitnessScore);
                case Rank:
                    return rankBoardSelection(chromosomes, currentGenerationTotalFitnessScore);
                default:
                    System.out.println("Error in choosing parent selection method: " + parentSelectionMethod + "Choosing default " +
                            "roulette wheel ");
                    return rouletteWheelSelection(chromosomes, currentGenerationTotalFitnessScore);
            }
        } catch(Exception e) {
            System.out.println("Error choosing parent, choosing top 1st or 2nd chromosome" + "Error: " + e);
            if(rand.nextBoolean()) {
                return chromosomes.get(0);
            } else {
                return chromosomes.get(1);
            }
        }
    }

    //================================================================================
    // Roulette Wheel Functionality
    //================================================================================

    /**
     * Sets the Roulette Wheel to the class variable by creating one
     */
    public void setRouletteWheel(ArrayList<Chromosome> chromosomes) {
        this.rouletteWheel = createRouletteWheel(chromosomes);
    }

    /**
     * Selects a chromosome from the Roulette Wheel
     */
    public Chromosome rouletteWheelSelection(ArrayList<Chromosome> chromosomes, double currentGenerationTotalFitnessScore) {
        int indexOfChromosomeToChoose = 0;
        double positionOnRouletteWheel = 1 + ((currentGenerationTotalFitnessScore - 1) * rand.nextDouble()); // values from 1 to total
        double currentTotal = 0;
        for (int i = 0; i < rouletteWheel.length; i++) { // Slowly goes through roulette wheel until we get to
            currentTotal = currentTotal + rouletteWheel[i]; // the position randomly chosen
            if (currentTotal >= positionOnRouletteWheel) {
                indexOfChromosomeToChoose = i;
                break;
            }

        }
        return chromosomes.get(indexOfChromosomeToChoose);
    }

    /**
     * Creates and populates the Roulette Wheel
     */
    public double[] createRouletteWheel(ArrayList<Chromosome> chromosomes) {
        double[] rouletteWheel = new double[Population.POPULATION_SIZE];
        double rouletteTotal = 0;
        try {
            for (int i = 0; i < Population.POPULATION_SIZE; i++) {
                double chromosomeFitness = chromosomes.get(i).getFitnessScore();
                rouletteTotal = rouletteTotal + chromosomeFitness;
                rouletteWheel[i] = rouletteTotal;
            }
        } catch(Exception e) {
            System.out.println("Failure to create roulette wheel, caution: roulette wheel will be empty");
            System.out.println("Exception: " + e);
            return rouletteWheel;
        }
        return rouletteWheel;
    }

    //================================================================================
    // Ranked Functionality
    //================================================================================

    /**
     * Sets the Rank Board to the class variable by creating one
     */
    public void setRankBoard(ArrayList<Chromosome> chromosomes) {
        this.rankBoard = createRankBoard(chromosomes);
    }

    /**
     * Selects a chromosome from the Rank Board
     */
    public Chromosome rankBoardSelection(ArrayList<Chromosome> chromosomes, double currentGenerationTotalFitnessScore) {
        int rankToChoose = chooseRank(currentGenerationTotalFitnessScore);
        int positionInRank = new Random().nextInt(numberOfChromosomesInEachRank); // selects random chromosome in rank
        int indexOfChromosomeToChoose = (rankToChoose * numberOfChromosomesInEachRank) + positionInRank;

        // If population size is not a multiple of number of chromosomes in each rank, there is a chance of picking a rank
        // off the edge of the population
        if(indexOfChromosomeToChoose > chromosomes.size()-1){
            positionInRank = new Random().nextInt(chromosomes.size()%numberOfChromosomesInEachRank);
            indexOfChromosomeToChoose = (rankToChoose*numberOfChromosomesInEachRank) + positionInRank;
        }

        return chromosomes.get(indexOfChromosomeToChoose);
    }

    /**
     * Chooses the rank to pick the chromosome from
     */
    public int chooseRank(double currentGenerationTotalFitnessScore) {
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

    /**
     * Create the rank board depending on the fitness score of each of the chromosomes and number of chromosomes we want in each rank
     */
    public double[] createRankBoard(ArrayList<Chromosome> chromosomes) {
        int numberOfRanks = (Population.POPULATION_SIZE+ numberOfChromosomesInEachRank - 1 ) / numberOfChromosomesInEachRank; // round the integer up
        double[] rankBoard = new double[numberOfRanks];
        int rank = 0;
        try {
            for (int i = 0; i < Population.POPULATION_SIZE - numberOfChromosomesInEachRank; i += numberOfChromosomesInEachRank) { //Do not calculate final rank
                for (int j = 0; j < 10; j++) {
                    rankBoard[rank] = rankBoard[rank] + chromosomes.get(i).getFitnessScore();
                }
                rank++;
            }

            // Calculating the last rank needs to be done differently
            // If we have 101 chromosomes in the population, and each rank has 10 chromosomes, the last rank will have 1 chromosome not 10!
            // This means we have to calculate an average of the fitness score for the remainder chromosomes
            int numberOfMembersInLastRank = Population.POPULATION_SIZE % numberOfChromosomesInEachRank;

            if (numberOfMembersInLastRank != 0) { // means there will be chromosomes left over after grouping (the remainder)
                for (int i = 0; i < numberOfMembersInLastRank; i++) {
                    rankBoard[numberOfRanks - 1] = rankBoard[numberOfRanks - 1] + chromosomes.get(Population.POPULATION_SIZE - numberOfChromosomesInEachRank + i).getFitnessScore();
                }
                double averageFitness = rankBoard[numberOfRanks - 1] / numberOfMembersInLastRank;
                rankBoard[numberOfRanks - 1] = averageFitness * 10;
            } else { // Otherwise calculate final rank as usual
                for (int i = 0; i < numberOfChromosomesInEachRank; i++) {
                    rankBoard[numberOfRanks - 1] = rankBoard[numberOfRanks - 1] + chromosomes.get(Population.POPULATION_SIZE - numberOfChromosomesInEachRank + i).getFitnessScore();
                }
            }
        } catch(Exception e) {
            System.out.println("Failure to create roulette wheel, caution: rank board  will be empty");
            System.out.println("Exception: " + e);
            return rankBoard;
        }
        return rankBoard;
    }

    //================================================================================
    // Getters & Setters
    //================================================================================

    /**
     * For unit tests
     */
    public int getNumberOfChromosomesInEachRank() {
        return this.numberOfChromosomesInEachRank;
    }

    /**
     * For unit tests
     */
    public double[] getRouletteWheel() {
        return rouletteWheel;
    }

    /**
     * For unit tests
     */
    public double[] getRankBoard() {
        return rankBoard;
    }

}
