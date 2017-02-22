package com.brandonhogan.liftscout.views.calendar;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public class CalendarContract {

    interface View {
        void setEvents(ArrayList<Event> events);
    }

    interface Presenter {
        void viewCreated();

    }
}
