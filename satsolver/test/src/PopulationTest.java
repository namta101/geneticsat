package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;

public class PopulationTest {
    private Population population;

    public PopulationTest() {
        Formula formula = TestHelper.createFormula();
        population = new Population(formula, 3);
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

}



