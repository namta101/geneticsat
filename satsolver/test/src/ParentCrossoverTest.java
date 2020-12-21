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

    // Due to randomness of method, it is hard to predict genes that will come out
    @Test
    public void twoPointCrossover_ReturnsChromosome() {
        int[] parentOneGenes = new int[]{0,0,0,0,0,0,0,0};
        int[] parentTwoGenes = new int[]{1,1,1,1,1,1,1,1};
        Chromosome offspring = parentCrossover.twoPointCrossover(parentOneGenes, parentTwoGenes, parentOneGenes.length);

        Assertions.assertEquals(8, offspring.getGenes().length);

    }
}
