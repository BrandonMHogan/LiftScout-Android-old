package com.brandonhogan.liftscout.views.calendar;

import com.brandonhogan.liftscout.core.managers.CalendarManager;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.model.CalendarEvent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public class CalendarPresenter implements CalendarContract.Presenter {

    // Injects
    @Inject
    ProgressManager progressManager;

    @Inject
    CalendarManager calendarManager;


    // Private Properties
    //
    private CalendarContract.View view;


    // Constructor
    //
    public CalendarPresenter(CalendarContract.View view) {
        Injector.getAppComponent().inject(this);
        this.view = view;
    }


    // Contracts
    //
    @Override
    public void viewCreated() {

        Date date = progressManager.getTodayProgress().getDate();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        ArrayList<CalendarEvent> events = calendarManager.getEventsForMonthYear(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

        ArrayList<Event> viewEvents = new ArrayList<>();
        for (CalendarEvent event : events) {
            viewEvents.add(new Event(event.getColor(), event.getDate().getTime()));
        }

        view.setEvents(viewEvents);

    }
}
