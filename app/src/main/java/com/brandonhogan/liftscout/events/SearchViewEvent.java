package com.brandonhogan.liftscout.events;

/**
 * Created by Brandon on 3/22/2017.
 * Description :
 */

public class SearchViewEvent {

    private final boolean isActive;
    private final String newText;

    public SearchViewEvent(boolean isActive, String newText) {
        this.isActive = isActive;
        this.newText = newText;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getNewText() {
        return newText;
    }
}
