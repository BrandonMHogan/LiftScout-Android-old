package com.brandonhogan.liftscout.events;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class TrackerEvent {

    public final boolean isUpdated;
    public final boolean isNew;

    public TrackerEvent(boolean updated) {
        this.isUpdated = updated;
        this.isNew = false;
    }

    public TrackerEvent(boolean updated, boolean isNew) {
        this.isUpdated = updated;
        this.isNew = isNew;
    }

}
