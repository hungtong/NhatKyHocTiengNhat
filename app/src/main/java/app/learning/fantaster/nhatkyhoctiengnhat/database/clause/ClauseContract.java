package app.learning.fantaster.nhatkyhoctiengnhat.database.clause;

public class ClauseContract {

    // information about database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Clause.sqlite";
    public static final String DATABASE_PATH = "/data/data/app.learning.fantaster.nhatkyhoctiengnhat/databases/";

    // This column is actually foreign key connected to the primary key COLUMN_ID
    public static final String COLUMN_CLAUSE_ID = "clause_id";

    // For Clauses Table
    public static final String TABLE_CLAUSES = "Clauses";
    public static final String COLUMN_ID = "_id";   // primary key
    public static final String COLUMN_CLAUSE_NAME = "clause";
    public static final String COLUMN_FORMULA = "formula";
    public static final String COLUMN_BRIEF_SUMMARY = "briefSummary";
    public static final String COLUMN_EXPLANATION = "explanation";
    public static final String COLUMN_LAST_EXAMPLE_ON = "lastExampleOn";

    // For Topics Table - Table with all possible topics that has many-to-many relationship
    // with Clauses Table
    // One clause can belong to multiple topics, one topic can be used to describe
    // multiple clauses as well.
    public static final String TABLE_TOPICS = "Topics";
    public static final String COLUMN_TOPIC = "topic";

    // For ClausesTopics Table - The bridge performing many-to-many relationship between Clauses
    // and Topics
    public static final String TABLE_CLAUSES_TOPICS ="ClausesTopics";
    public static final String COLUMN_TOPIC_ID = "topic_id";

    // For Examples Table - Clauses Table has one-to-many relationship with this
    // One clause can have multiple examples, but one example usually works for one clause
    // only,
    public static final String TABLE_EXAMPLES = "Examples";
    public static final String COLUMN_EXAMPLE = "example";

    // For MemoryTricks Table
    public static final String TABLE_MEMORY_TRICKS = "MemoryTricks";
    public static final String COLUMN_MEMORY_TRICK = "memoryTrick";

}
