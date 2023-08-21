package de.anevis.backend.domain;

public class Cursors {
    private String nextCursor;
    private String previousCursor;

    public Cursors(String nextCursor, String previousCursor){
        this.nextCursor = nextCursor;
        this.previousCursor = previousCursor;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public void setPreviousCursor(String previousCursor) {
        this.previousCursor = previousCursor;
    }

    public String getPreviousCursor() {
        return previousCursor;
    }
}