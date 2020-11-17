package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class ChromosomeTest {
    Chromosome chromosome;
    public ChromosomeTest() {
        ArrayList<Clause> formula = createFormula();
        chromosome = new Chromosome(3, formula);
    }

    public ArrayList<Clause> createFormula() {
        ArrayList<Clause> formula = new ArrayList<>();
        Clause clause1 = new Clause(new int[]{-1,2,-3,0});
        Clause clause2 = new Clause(new int[]{1,2,-3,0});
        Clause clause3 = new Clause(new int[]{-1,2,3,0});
        formula.add(clause1);
        formula.add(clause2);
        formula.add(clause3);
        return formula;
    }

    @Test
    void name() {
        Assertions.assertEquals(1,1);

    }



}