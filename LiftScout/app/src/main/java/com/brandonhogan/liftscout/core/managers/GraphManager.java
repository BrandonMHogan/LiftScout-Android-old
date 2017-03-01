package com.brandonhogan.liftscout.core.managers;

/**
 * Created by Brandon on 3/1/2017.
 * Description :
 */

public class GraphManager {

    private boolean isInSearch = false;
    private int currentExerciseId;
    private String graphName;

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public boolean isInSearch() {
        return isInSearch;
    }

    public void setInSearch(boolean inSearch) {
        isInSearch = inSearch;
    }

    public int getCurrentExerciseId() {
        return currentExerciseId;
    }

    public void setCurrentExerciseId(int currentExerciseId) {
        this.currentExerciseId = currentExerciseId;
    }
}
