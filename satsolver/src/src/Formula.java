package src;

import java.util.ArrayList;

public class Formula {
    private ArrayList<Clause> clauses;

    public Formula() {
        clauses = new ArrayList<>();
    }

    public ArrayList<Clause> getClauses() {
        return this.clauses;
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public int getSize() {
        return clauses.size();
    }


}

