package de.anevis.backend.domain;

public enum PaginationDirection {
    NEXT_PAGE("next"),
    PREVIOUS_PAGE("previous"),
    CURRENT_PAGE("current");

    private final String direction;

    private PaginationDirection(String direction){
        this.direction = direction;
    }

    public String toString(){
        return direction;
    }
}
