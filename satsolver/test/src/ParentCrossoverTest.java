package src;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;


public class ParentCrossoverTest {
    private ParentCrossover parentCrossover;
    public ParentCrossoverTest() {
        Formula formula = TestHelper.createFormula();
        int numberOfVariables = 3;
        Mutator mutator = new Mutator(formula);
        parentCrossover = new ParentCrossover(formula, numberOfVariables, mutator);

    }

    @Test
    public void uniformCrossover_ReturnsChromosomeWithGenesOf2ParentsUniformCrossed(){
        int[] parentOneGenes = new int[]{0,0,0};
        int[] parentTwoGenes = new int[]{1,1,1};
        Chromosome offspring = parentCrossover.uniformCrossover(parentOneGenes, parentTwoGenes, parentOneGenes.length);
        int[] predictedOffSpringGenes = new int[]{0,1,0};

        Assertions.assertTrue(Arrays.equals(predictedOffSpringGenes, offspring.getGenes()));

    }

    @Test
    public void uniformCrossover_ReturnsChromosomeWithGenesOf2ParentsUniformCrossed1(){
        int[] parentOneGenes = new int[]{1};
        int[] parentTwoGenes = new int[]{0};
        Chromosome offspring = parentCrossover.uniformCrossover(parentOneGenes, parentTwoGenes, parentOneGenes.length);
        int[] predictedOffSpringGenes = new int[]{1};

        Assertions.assertTrue(Arrays.equals(predictedOffSpringGenes, offspring.getGenes()));

    }

    // Due to randomness of method, it is hard to predict genes that will come out
    @Test
    public void twoPointCrossover_ReturnsChromosome() {
        int[] parentOneGenes = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] parentTwoGenes = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        Chromosome offspring = parentCrossover.twoPointCrossover(parentOneGenes, parentTwoGenes, parentOneGenes.length);

        Assertions.assertEquals(20, offspring.getGenes().length);
    }

    @Test
    public void shouldCrossover_returnsTrueIfShouldCrossover() {

        boolean resultOfWithGenesBelowThreshold = parentCrossover.shouldCrossover(0, 0, 9);
        boolean resultOfHalfCrossover = parentCrossover.shouldCrossover(0, 5, 10);
        boolean resultOf20PercentCrossover = parentCrossover.shouldCrossover(0, 20, 100);
        boolean resultOf80PercentCrossover = parentCrossover.shouldCrossover(20,100, 100);

        Assertions.assertTrue(resultOfHalfCrossover);
        Assertions.assertTrue(resultOfWithGenesBelowThreshold);
        Assertions.assertTrue(resultOf20PercentCrossover);
        Assertions.assertTrue(resultOf80PercentCrossover);
    }

    @Test
    public void shouldCrossover_returnsFalseIfShouldNotCrossover() {

        boolean resultOfBelow20PerCent = parentCrossover.shouldCrossover(0, 19, 100);
        boolean resultOfAbove80PerCent = parentCrossover.shouldCrossover(19,100, 100);

        Assertions.assertFalse(resultOfBelow20PerCent);
        Assertions.assertFalse(resultOfAbove80PerCent);
    }
}
