package de.anevis.backend.pagination;

public class Cursors {
    private final String nextCursor;
    private final String previousCursor;
    private final String currentCursor;

    public Cursors(String nextCursor, String previousCursor, String currentCursor){
        this.nextCursor = nextCursor;
        this.previousCursor = previousCursor;
        this.currentCursor = currentCursor;
    }

    public String getNextCursor() {return nextCursor;}

    public String getPreviousCursor() {return previousCursor;}

    public String getCurrentCursor() { return currentCursor;}
}