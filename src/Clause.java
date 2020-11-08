import java.util.Arrays;

public class Clause {
    private int[] variables;

    public Clause(int[] variables) {

        this.variables = Arrays.copyOf(variables, variables.length - 1);
    }

    public int[] getVariables() {
        return variables;
    }
}