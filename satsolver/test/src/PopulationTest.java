package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class PopulationTest {
    private Population population;

    public PopulationTest() {
        Formula formula = TestHelper.createFormula();
        population = new Population(formula, 3, TestHelper.createMutator());
    }

    @Test
    public void initialisePopulation_createsCreatesPopulationSizeNumberOfChromosomes() {
        population.initialisePopulation();
        Assertions.assertEquals(Population.POPULATION_SIZE, population.getChromosomes().size());
    }

    // Correct solution is 1,1,1
    @Test
    public void isSatisfied_onEasySatInstances_returnsTrue() {
        population.initialisePopulation();
        boolean solutionFound = population.isSatisfied();
        int[] correctSolution = new int[]{1, 1, 1};
        if (solutionFound) {
            if (!Arrays.equals(correctSolution, population.getSatisfyingSolution())) {
                Assertions.fail();
            }
        }
    }

    @Test
    public void sortByFitnessValue_SortsChromosomesInOrderOfFitnessValueDescending() {
        population.initialisePopulation();
        population.sortPopulationByFitnessValue(population.getChromosomes());
        for (int i = 0; i < population.getChromosomes().size()-1; i++) {
            if (population.getChromosomes().get(i).getFitnessScore() >= population.getChromosomes().get(i+1).getFitnessScore()) {
                // continue
            }
            else {
                Assertions.fail();
            }
        }
    }

    @Test
    public void getTotalPopulationFitnessScore_onMethodCall_ReturnsAccumulatedFitnessScoreOfPopulation(){
        population.initialisePopulation();
        ArrayList<Chromosome> chromosomes = population.getChromosomes();
        double accumulatedFitnessScore = 0;
        for(Chromosome chromosome: chromosomes) {
            accumulatedFitnessScore = accumulatedFitnessScore + chromosome.getFitnessScore();
        }

        population.setCurrentGenerationTotalFitnessScore();
        Assertions.assertEquals(accumulatedFitnessScore, population.getCurrentGenerationTotalFitnessScore());
    }

    @Test
    public void getCurrentMostSatisfyingSolutionFitnessScore_returnsFitnessScoreOfMostFitChromosome() {
        population.initialisePopulation();
        double fitnessScoreOfMostFitChromosome = population.getCurrentMostSatisfyingSolutionFitnessScore();
        ArrayList<Chromosome> chromosomes = population.getChromosomes();
        for(int i = 0; i<chromosomes.size(); i++){
            if(fitnessScoreOfMostFitChromosome < chromosomes.get(i).getFitnessScore()) {
                Assertions.fail();
            }
        }
    }

    @Test
    public void getCurrentMostSatisfyingSolution_returnsCurrentMostSatisfyingSolutionGenes(){
        population.initialisePopulation();
        population.getChromosomes().get(0).intakeParentsGenes(new int[]{1,1,1}); // Make one gene have the satisfying solution
        int[] currentMostSatisfyingSolution = population.getCurrentMostSatisfyingSolution();

        Assertions.assertArrayEquals(currentMostSatisfyingSolution, new int[]{1,1,1});
    }

    @Test
    public void nextPopulation_setsTotalFitnessScore() {
        population.initialisePopulation();
        double fitnessScoreBefore = population.getCurrentGenerationTotalFitnessScore();
        population.nextPopulation();
        double fitnessScoreAfter = population.getCurrentGenerationTotalFitnessScore();
        Assertions.assertTrue(fitnessScoreAfter > fitnessScoreBefore);
    }

    @Test
    public void nextPopulation_createsNewPopulationWithDifferentChromosomes(){
        population.initialisePopulation();
        ArrayList<Chromosome> originalChromosomes = population.getChromosomes();
        population.nextPopulation();
        ArrayList<Chromosome> newChromosomes = population.getChromosomes();
        Assertions.assertNotEquals(originalChromosomes, newChromosomes);
    }

    @Test
    public void createPopulation_throwsError_ResetsPopulation() {
        ArrayList<Chromosome> newPopulation = population.createNewPopulation(); // population's chromosomes will be null
        Assertions.assertNotNull(newPopulation);
        Assertions.assertEquals(newPopulation.size(), Population.POPULATION_SIZE);
    }

    @Test
    public void clearPopulation_makesChromosomesEmpty(){
        population.initialisePopulation();
        Assertions.assertTrue(population.getChromosomes().size() > 0);
        population.clearPopulation();
        int numberOfChromosomes = population.getChromosomes().size();
        Assertions.assertEquals(0, numberOfChromosomes);
    }





    }



