import java.util.Arrays;

public class Clause {
    private int[] variables;

    public Clause(int[] variables) {
        this.variables = Arrays.copyOf(variables, variables.length - 1); // Get rid of the 0 on the end 
    }

    public int[] getVariables() {
        return variables;
    }

    // Get Unit clause + pure literal 
    // Return variable number of each one, then lock it on the chromosome level
}