package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClauseTest {
    private Clause clause;
    private Clause clause1;


    public ClauseTest() {
         clause = new Clause(new int[]{2,4,-1,0});
         clause1 = new Clause(new int[]{2,4,-1});
    }

    @Test
    public void getVariables_0variable_returnsOneLessVariableThanOnClauseCreation() {
        int[] variables = clause.getVariables();
        Assertions.assertEquals(3, variables.length);
        Assertions.assertEquals(2, variables[0]);
        Assertions.assertEquals(4, variables[1]);
        Assertions.assertEquals(-1, variables[2]);
    }

    @Test
    public void getVariables_no0variable_returnsSameVariableAsClauseCreation() {
        int[] variables = clause1.getVariables();
        Assertions.assertEquals(3, variables.length);
        Assertions.assertEquals(2, variables[0]);
        Assertions.assertEquals(4, variables[1]);
        Assertions.assertEquals(-1, variables[2]);
    }

}

