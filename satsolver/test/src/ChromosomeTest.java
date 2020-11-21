package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class ChromosomeTest {
    Chromosome chromosome;
    public ChromosomeTest() {
        Formula formula = TestHelper.createFormula();
        chromosome = new Chromosome(3, formula);
    }

    @BeforeEach
    void setUp() {
        chromosome.intakeParentsGenes(new int[]{1,1,1});
    }

    @Test
    public void initialiseGenes_ValidFormula_genesLengthEqualsClausesInFormula() {
        Assertions.assertEquals(3,chromosome.getGenes().length);
    }

    @Test
    public void getClausesMatched_validClauseChromosome_returnsCorrectNumberOfClausesMatched() {
        int clausesMatched = chromosome.getClausesMatched();
        Assertions.assertEquals(7, clausesMatched);
    }

    @Test
    public void randomSelectionMutate_ranOnce_AltersOneGene() {
        int[] preMutatedGenes = new int[]{1,1,1};
        chromosome.randomSelectionMutate(chromosome.getGenes());
        int[] postMutatedGenes = chromosome.getGenes();

        int numberOfGenesMutated = 0;
        for(int i=0; i<preMutatedGenes.length; i++){
            if(preMutatedGenes[i] != postMutatedGenes[i]) {
                numberOfGenesMutated++;
            }
        }
        Assertions.assertEquals(1, numberOfGenesMutated);
    }

    @Test
    public void clearFitnessScore_SetsChromosomeFitnessScoreTo0() {
        double initialFitnessScore = chromosome.getFitnessScore();
        chromosome.clearFitnessScore();
        Assertions.assertEquals(0, chromosome.getFitnessScore());
        Assertions.assertNotEquals(initialFitnessScore, chromosome.getFitnessScore());
    }








}