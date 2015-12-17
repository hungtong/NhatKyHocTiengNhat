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
    public static final String COLUMN_NAME_CLAUSE_TYPE ="clauseType";
    public static final String COLUMN_NAME_TRANSLATION = "translation";
    public static final String COLUMN_NAME_EXPLANATION = "explanation";
    public static final String COLUMN_NAME_COMMENT = "comment";
    public static final String COLUMN_NAME_DATE_CREATED = "dateCreated";


}
