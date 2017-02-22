package com.brandonhogan.liftscout.views.calendar;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public class CalendarContract {

    interface View {
        void setEvents(String monthTitle, ArrayList<Event> events);
    }

    interface Presenter {
        void viewCreated();
        void onMonthScroll(Date firstDayOfNewMonth);

    }
}
