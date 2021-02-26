package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ParentSelectorTest {
    Population population;
    ParentSelector parentSelector;
    public ParentSelectorTest() {
        Formula formula = TestHelper.createFormula();
        population = new Population(formula,3);
        population.initialisePopulation();
        population.sortPopulationByFitnessValue(population.getChromosomes());
        population.setCurrentGenerationTotalFitnessScore();
        parentSelector = new ParentSelector();
    }

    @Test
    public void setUpForGeneration_setsUpRouletteWheel(){
        parentSelector.setUpForGeneration(population.getChromosomes());
        double[] rouletteWheel = parentSelector.getRouletteWheel();
        Assertions.assertNotNull(rouletteWheel);
        Assertions.assertEquals(15, rouletteWheel.length);
    }

    // We cannot test these 2 method because it could return any chromosome. But we can use it to easily debug the method.
    @Test
    public void rouletteWheelSelection_returnsCorrectChromosome() {
        parentSelector.setRouletteWheel(population.getChromosomes());

        Chromosome chromosomeChosen = parentSelector.chooseParent(population.getChromosomes(), population.getCurrentGenerationTotalFitnessScore());
        Chromosome chromosomeChosen1 = parentSelector.rouletteWheelSelection(population.getChromosomes(), population.getCurrentGenerationTotalFitnessScore());
        Assertions.assertEquals(3, chromosomeChosen.getGenes().length);
        Assertions.assertEquals(3, chromosomeChosen1.getGenes().length);

    }

    @Test
    public void createRouletteWheel_returnsCorrectSizedRouletteWheel()
    {
        double[] rouletteBoard = parentSelector.createRouletteWheel(population.getChromosomes());
        Assertions.assertEquals(population.getChromosomes().size(), rouletteBoard.length);
    }



    @Test
    public void createRankBoard_returnsCorrectRankBoardSize() {
        double[] rankBoard = parentSelector.createRankBoard(population.getChromosomes());
        double assumedNumberOfRanks = Math.ceil((double)population.getChromosomes().size() / parentSelector.getNumberOfChromosomesInEachRank());
        double numberOfRanks = rankBoard.length;

        Assertions.assertEquals(assumedNumberOfRanks, numberOfRanks);

        // Check that the rank board is in descending order
        for (int i = 0; i < rankBoard.length-1; i++) {
            if (rankBoard[i] < rankBoard[i+1]) {
                Assertions.fail();
            }
        }
    }



    @Test
    // Hard to test as the rank chosen will change each time... Unless we run it many times and hope the probability of it choosing a rank
    // in the top half is chosen more often than bottom half EVERY time we run.
    public void chooseRank_returnsCorrectRank() {

        parentSelector.setRankBoard(population.getChromosomes());
        int chosenRank = parentSelector.chooseRank(population.getCurrentGenerationTotalFitnessScore());

        // Make sure the chosen rank is possible within the rank board
        Assertions.assertTrue(chosenRank>=0 && chosenRank<=Math.ceil((double)population.getChromosomes().size() / parentSelector.getNumberOfChromosomesInEachRank()));
    }

    // We cannot test this method because it could return any chromosome. But we can use it to easily debug the method.
    @Test
    public void rankBoardSelection_returnsCorrectChromosome() {
        parentSelector.setRankBoard(population.getChromosomes());

        Chromosome chromosomeChosen = parentSelector.rankBoardSelection(population.getChromosomes(), population.getCurrentGenerationTotalFitnessScore());
        Assertions.assertEquals(3, chromosomeChosen.getGenes().length); // T
    }
}
