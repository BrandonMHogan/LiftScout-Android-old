package com.brandonhogan.liftscout.events;

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

    public boolean isUpdated() {
        return isUpdated;
    }

    public boolean isNew() {
        return isNew;
    }
}
