package src;

import java.util.ArrayList;
import java.util.Random;

public class ParentSelector {
    private double [] rouletteWheel;
    private double[] rankBoard;
    private int numberOfChromosomesInEachRank = 10;
    private GACombination.ParentSelection parentSelectionMethod;

    public ParentSelector() {
        parentSelectionMethod = GACombination.ParentSelection.RouletteWheel;
    }

    public void setUpForGeneration(ArrayList<Chromosome> chromosomes) {
        switch (parentSelectionMethod) {
            case RouletteWheel:
                setRouletteWheel(chromosomes);
            case Rank:
                setRankBoard(chromosomes);
            default:
                System.out.println("Error in choosing parent selection method");
        }
    }

    public Chromosome chooseParent(ArrayList<Chromosome> chromosomes, double currentGenerationTotalFitnessScore) {
        switch (parentSelectionMethod) {
            case RouletteWheel:
                return rouletteWheelSelection(chromosomes, currentGenerationTotalFitnessScore);
            case Rank:
                return rankBoardSelection(chromosomes, currentGenerationTotalFitnessScore);
            default:
                System.out.println("Error in choosing parent selection method: " + parentSelectionMethod + "Choosing default " +
                        "roulette wheel " );
                return rouletteWheelSelection(chromosomes, currentGenerationTotalFitnessScore);
        }
    }

    public void setRouletteWheel(ArrayList<Chromosome> chromosomes) {
        this.rouletteWheel = createRouletteWheel(chromosomes);
    }

    public Chromosome rouletteWheelSelection(ArrayList<Chromosome> chromosomes, double currentGenerationTotalFitnessScore) {
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

    public double[] createRouletteWheel(ArrayList<Chromosome> chromosomes) {
        double[] newRouletteWheel = new double[Population.POPULATION_SIZE];
        double rouletteTotal = 0;
        for (int i = 0; i < Population.POPULATION_SIZE; i++) {
            double chromosomeFitness = chromosomes.get(i).getFitnessScore();
            rouletteTotal = rouletteTotal + chromosomeFitness;
            newRouletteWheel[i] = rouletteTotal;
        }

        return newRouletteWheel;
    }

    public void setRankBoard(ArrayList<Chromosome> chromosomes) {
        this.rankBoard = createRankBoard(chromosomes);
    }

    public Chromosome rankBoardSelection(ArrayList<Chromosome> chromosomes, double currentGenerationTotalFitnessScore) {
        int rankToChoose = chooseRank(currentGenerationTotalFitnessScore);
        int positionInRank = new Random().nextInt(numberOfChromosomesInEachRank); // selects random chromosome in rank
        int indexOfChromosomeToChoose = (rankToChoose * numberOfChromosomesInEachRank) + positionInRank;
        return chromosomes.get(indexOfChromosomeToChoose);
    }

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

    // Create the rank board depending on the fitness score of each of the chromosomes and number of chromosomes we want in each rank
    public double[] createRankBoard(ArrayList<Chromosome> chromosomes) {
        int numberOfRanks = (Population.POPULATION_SIZE+ numberOfChromosomesInEachRank - 1 ) / numberOfChromosomesInEachRank; // round the integer up
        double[] rankBoard = new double[numberOfRanks];
        int rank = 0;
        for(int i = 0; i<Population.POPULATION_SIZE-numberOfChromosomesInEachRank; i += numberOfChromosomesInEachRank) { //Do not calculate final rank
            for(int j=0; j<10; j++) {
                rankBoard[rank] = rankBoard[rank] + chromosomes.get(i).getFitnessScore();
            }
            rank++;
        }

        // Calculating the last rank needs to be done differently
        // If we have 101 chromosomes in the population, and each rank has 10 chromosomes, the last rank will have 1 chromosome not 10!
        // This means we have to calculate an average of the fitness score for the remainder chromosomes
        int numberOfMembersInLastRank = Population.POPULATION_SIZE%numberOfChromosomesInEachRank;

        if(numberOfMembersInLastRank != 0) { // means there will be chromosomes left over after grouping (the remainder)
            for (int i = 0; i < numberOfMembersInLastRank; i++) {
                rankBoard[numberOfRanks - 1] = rankBoard[numberOfRanks - 1] + chromosomes.get(Population.POPULATION_SIZE - numberOfChromosomesInEachRank + i).getFitnessScore();
            }
            double averageFitness = rankBoard[numberOfRanks-1] / numberOfMembersInLastRank;
            rankBoard[numberOfRanks-1] = averageFitness * 10;
        }  else { // Otherwise calculate final rank as usual
            for(int i =0; i< numberOfChromosomesInEachRank; i++) {
                rankBoard[numberOfRanks - 1] = rankBoard[numberOfRanks - 1] + chromosomes.get(Population.POPULATION_SIZE - numberOfChromosomesInEachRank + i).getFitnessScore();
            }
        }
        return rankBoard;
    }

    public int getNumberOfChromosomesInEachRank() {
        return this.numberOfChromosomesInEachRank;
    }
}
