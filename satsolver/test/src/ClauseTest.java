package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClauseTest {
    private Clause clause;

    public ClauseTest() {
         clause = new Clause(new int[]{2,4,-1,0});
    }

    @Test
    public void getVariables_nonEmptyVariableArray_returnsOneLessVariableThanClauseCreation() {
        int[] variables = clause.getVariables();
        Assertions.assertEquals(variables.length, 3);
        Assertions.assertEquals(variables[0] , 2);
        Assertions.assertEquals(variables[1] , 4);
        Assertions.assertEquals(variables[2] , -1);
    }

}

