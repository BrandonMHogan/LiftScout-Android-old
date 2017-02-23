package com.brandonhogan.liftscout.views.calendar;

import com.brandonhogan.liftscout.views.workout.history.HistoryListSection;
import com.brandonhogan.liftscout.views.workout.history.HistoryTrackerEvent;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public class CalendarContract {

    interface View {
        void setEvents(String monthTitle, Date date, ArrayList<Event> events);
        void setupAdapter(List<HistoryListSection> data);
        void editTracker(int exerciseId);
        String getEmptySetMessage();
    }

    interface Presenter {
        void viewCreated();
        void dateSelected(Date date);
        void onMonthScroll(Date firstDayOfNewMonth);
        void editEvent(HistoryTrackerEvent event);
        void setClicked(int exerciseId);
    }
}
