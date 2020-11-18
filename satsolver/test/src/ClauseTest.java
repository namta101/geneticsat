package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClauseTest {
    private Clause clause;

    public ClauseTest() {
         clause = new Clause(new int[]{2,4,-1,0});
    }

    @Test
    public void getVariables_validVariablesArray_returnsOneLessVariableThanOnClauseCreation() {
        int[] variables = clause.getVariables();
        Assertions.assertEquals(3, variables.length);
        Assertions.assertEquals(2, variables[0]);
        Assertions.assertEquals(4, variables[1]);
        Assertions.assertEquals(-1, variables[2]);
    }

}

