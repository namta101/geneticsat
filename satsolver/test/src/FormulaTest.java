package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormulaTest {
    private Formula formula;

    public FormulaTest() {
        formula =  TestHelper.createFormula();
    }

    @Test
    public void getClausesMatched_returnsCorrectNumberOfClausesMatched() {
        int clausesMatched = formula.getClausesMatched(new int[]{1,1,1});
        Assertions.assertEquals(7, clausesMatched);
    }
}
