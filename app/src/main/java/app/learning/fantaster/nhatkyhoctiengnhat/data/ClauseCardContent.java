package app.learning.fantaster.nhatkyhoctiengnhat.data;

public class ClauseCardContent {
    private int headerImage;
    private String title, clause;

    public ClauseCardContent(int headerImage, String title, String clause) {
        setHeaderImage(headerImage);
        setTitle(title);
        setClause(clause);
    }

    public int getHeaderImage() {
        return headerImage;
    }

    public String getTitle() {
        return title;
    }

    public String getClause() {
        return clause;
    }

    public void setHeaderImage(int headerImage) {
        this.headerImage = headerImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }
}
