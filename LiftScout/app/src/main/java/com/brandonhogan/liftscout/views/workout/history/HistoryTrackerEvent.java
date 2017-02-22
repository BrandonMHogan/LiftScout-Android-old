package com.brandonhogan.liftscout.views.workout.history;

import java.util.Date;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public class HistoryTrackerEvent {
    public final int eventID;
    public final Date date;

    public HistoryTrackerEvent(int eventID, Date date) {
        this.eventID = eventID;
        this.date = date;
    }
}
