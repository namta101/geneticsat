package src;

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
