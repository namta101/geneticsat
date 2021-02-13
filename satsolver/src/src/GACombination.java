package src;

//Enum class that contains different types of Genetic Algorithm implementations
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
