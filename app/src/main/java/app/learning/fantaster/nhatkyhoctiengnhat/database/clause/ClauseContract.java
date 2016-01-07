package app.learning.fantaster.nhatkyhoctiengnhat.database.clause;

public class ClauseContract {

    // information about database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Clause.sqlite";
    public static final String DATABASE_PATH = "/data/data/app.learning.fantaster.nhatkyhoctiengnhat/databases/";

    // information about table
    public static final String TABLE_NAME = "Clauses";
    public static final String COLUMN_NAME_CLAUSE_ID = "_id";
    public static final String COLUMN_NAME_CLAUSE = "clause";
    public static final String COLUMN_NAME_FORMULA ="formula";
    public static final String COLUMN_NAME_BRIEF_SUMMARY = "briefSummary";
    public static final String COLUMN_NAME_EXPLANATION = "explanation";
    public static final String COLUMN_NAME_EXAMPLE = "example";
    public static final String COLUMN_NAME_TOPIC = "topic";
    public static final String COLUNM_NAME_LAST_EXAMPLE_ON = "lastExampleOn";
    public static final String COLUMN_NAME_MEMORY_TRICK = "memoryTrick";

}
