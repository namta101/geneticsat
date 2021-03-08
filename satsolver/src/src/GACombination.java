package src;

/**
 * An Enum class which contains the different genetic implementations that are available
 */
public class GACombination {

    public enum ParentSelection {
        RouletteWheel,
        Rank,
    }

    public enum Mutation {
        Random,
        Greedy
    }

    public enum Crossover {
        Uniform,
        TwoPoint
    }

}
