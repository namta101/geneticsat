package src;

/**
 * An Enum class which contains the different genetic implementations that are available
 */
public class GACombination {

    public static double MUTATION_RATE = 0.1;
    public static double ELITISM_RATE = 0.95;
    public static int POPULATION_SIZE = 100;

    public static ParentSelection PARENT_SELECTION = ParentSelection.Rank;
    public static Crossover CROSSOVER = Crossover.Uniform;
    public static Mutation MUTATION = Mutation.Random;


    public static void setPARENTSELECTION(ParentSelection parentSelection){
        PARENT_SELECTION = parentSelection;
    }

    public static void setCROSSOVER(Crossover crossover){
        CROSSOVER = crossover;
    }

    public static void setMUTATION(Mutation mutation){
        MUTATION = mutation;
    }

    public enum ParentSelection {
        RouletteWheel,
        Rank,
    }

    public enum Mutation {
        Random,
        Greedy
    }

    public static enum Crossover {
        Uniform,
        TwoPoint
    }

    public double getElitismRate() {
        return ELITISM_RATE;
    }

    public double getMutationRate() {
        return MUTATION_RATE;
    }

    public int getPopulationSize() {
        return POPULATION_SIZE;
    }

    public static void setElitismRate(double elitism_rate) {
        ELITISM_RATE = elitism_rate;
    }

    public static void setMutationRate(double mutation_rate) {
        MUTATION_RATE = mutation_rate;
    }

    public static void setPopulationSize(int populationSize) {
        POPULATION_SIZE = populationSize;
    }

    public static String getConfigurationValues() {
        StringBuilder sb = new StringBuilder();
        sb.append("The configuration for this run is: " + "\n");
        sb.append("Mutation Rate:  " + MUTATION_RATE + "\n");
        sb.append("Elitism Rate:  " + ELITISM_RATE + "\n");
        sb.append("Population Size: " + POPULATION_SIZE + "\n");
        sb.append("Parent Selection Method: " + PARENT_SELECTION + "\n");
        sb.append("Parent Crossover Method: " + CROSSOVER + "\n");
        sb.append("Mutation Method: " + MUTATION  + "\n");

        return sb.toString();
    }


}
