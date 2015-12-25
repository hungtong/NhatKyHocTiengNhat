package app.learning.fantaster.nhatkyhoctiengnhat.data;

public class ClauseCardContent {
    private String clause, formula, briefSummary, explanation, topic, lastExampleOn;

    public ClauseCardContent(String clause, String formula, String briefSummary,
                             String explanation, String topic, String lastExampleOn) {
        this.clause = clause;
        this.formula = formula;
        this.briefSummary = briefSummary;
        this.explanation = explanation;
        this.topic = topic;
        this.lastExampleOn = lastExampleOn;
    }

    public ClauseCardContent() {

    }

    public String getClause() {return clause;}

    public String getFormula() {return formula;}

    public String getBriefSummary() {return briefSummary;}

    public String getExplanation() {return explanation; }

    public String getTopic() {return topic;}

    public String getLastExampleOn() {return lastExampleOn;}

    public void setClause(String clause) {this.clause = clause;}

    public void setFormula(String formula) {this.formula = formula;}

    public void setBriefSummary(String briefSummary) {this.briefSummary = briefSummary;}

    public void setExplanation(String explanation) {this.explanation = explanation;}

    public void setTopic(String topic) {this.topic = topic;}
}
