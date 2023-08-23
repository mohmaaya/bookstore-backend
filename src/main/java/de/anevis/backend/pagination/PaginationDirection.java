package de.anevis.backend.pagination;

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
