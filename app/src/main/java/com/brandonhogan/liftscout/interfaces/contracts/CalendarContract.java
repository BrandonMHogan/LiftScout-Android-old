package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.models.HistoryListSectionModel;
import com.brandonhogan.liftscout.events.HistoryTrackerEvent;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public interface CalendarContract {

    interface View {
        void setEvents(String monthTitle, Date date, ArrayList<Event> events);
        void setupAdapter(List<HistoryListSectionModel> data);
        void editTracker(int exerciseId);
        String getEmptySetMessage();
        void onSetDeleted(int position, int count);
    }

    interface Presenter {
        void viewCreated();
        void dateSelected(Date date);
        void onMonthScroll(Date firstDayOfNewMonth);
        void editEvent(HistoryTrackerEvent event);
        void setClicked(int exerciseId);
        void onDeleteSection(HistoryListSectionModel section, int position);
    }
}
