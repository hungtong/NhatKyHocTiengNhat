package app.learning.fantaster.nhatkyhoctiengnhat.data;

public class Clause  {
    public int clauseId;
    public String clause;
    public int clauseType;
    public String translation;
    public String explanation;
    public String comment;
    public String dateCreated;

    public Clause(int clauseId, String clause, int clauseType, String translation, String explanation,
                    String comment, String dateCreated) {
        this.clauseId = clauseId;
        this.clause = clause;
        this.clauseType = clauseType;
        this.translation = translation;
        this.explanation = explanation;
        this.comment = comment;
        this.dateCreated = dateCreated;
    }

}
