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
        Mutator mutator = new Mutator(formula);
        chromosome = new Chromosome(3, formula, mutator);
    }

    @BeforeEach
    void setUp() {
        chromosome.intakeParentsGenes(new int[]{1,1,1});
    }

    @Test
    public void initialiseGenes_ValidFormula_genesLengthEqualsClausesInFormula() {
        Assertions.assertEquals(3,chromosome.getGenes().length);
    }

//    @Test
//    public void getClausesMatched_validClauseChromosome_returnsCorrectNumberOfClausesMatched() {
//        int clausesMatched = chromosome.getClausesMatched();
//        Assertions.assertEquals(7, clausesMatched);
//    }


    @Test
    public void clearFitnessScore_SetsChromosomeFitnessScoreTo0() {
        double initialFitnessScore = chromosome.getFitnessScore();
        chromosome.clearFitnessScore();
        Assertions.assertEquals(0, chromosome.getFitnessScore());
        Assertions.assertNotEquals(initialFitnessScore, chromosome.getFitnessScore());
    }








}