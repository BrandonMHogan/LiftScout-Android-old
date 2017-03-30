package com.brandonhogan.liftscout.events;

/**
 * Created by Brandon on 3/30/2017.
 * Description :
 */

public class CategoryListEvent {

    boolean update;
    boolean onFabClicked;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isOnFabClicked() {
        return onFabClicked;
    }

    public void setOnFabClicked(boolean onFabClicked) {
        this.onFabClicked = onFabClicked;
    }
}
