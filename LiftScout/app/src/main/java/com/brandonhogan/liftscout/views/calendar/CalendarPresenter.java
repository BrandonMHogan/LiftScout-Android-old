package com.brandonhogan.liftscout.views.calendar;

import com.brandonhogan.liftscout.core.managers.CalendarManager;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.CalendarEvent;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.workout.history.HistoryListItem;
import com.brandonhogan.liftscout.views.workout.history.HistoryListSection;
import com.brandonhogan.liftscout.views.workout.history.HistoryTrackerEvent;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.mikepenz.fastadapter.IItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;

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

    @Inject
    UserManager userManager;


    // Private Properties
    //
    private CalendarContract.View view;
    private ArrayList<HistoryListSection> adapterData;
    private Date date;


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
        date = progressManager.getTodayProgress().getDate();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String monthTitle = BhDate.toMonthYearStringDate(cal.getTime());
        view.setEvents(monthTitle, getEvents(cal));

        updateAdapter();
    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        date = firstDayOfNewMonth;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String monthTitle = BhDate.toMonthYearStringDate(cal.getTime());
        view.setEvents(monthTitle, getEvents(cal));
    }

    @Override
    public void dateSelected(Date date) {
        this.date = date;
        updateAdapter();
    }

    // Private Functions
    //
    private ArrayList<Event> getEvents(Calendar cal) {

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        ArrayList<CalendarEvent> events = calendarManager.getEventsForMonthYear(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

        ArrayList<Event> viewEvents = new ArrayList<>();
        for (CalendarEvent event : events) {
            viewEvents.add(new Event(event.getColor(), event.getDate().getTime()));
        }

        return viewEvents;
    }

    private void updateAdapter() {

        adapterData = new ArrayList<>();
        RealmList<Set> sets = progressManager.getSetsByDate(date);

        if (sets != null) {
            for (Set set : sets) {

                List<IItem> items = new LinkedList<>();
                double volume = 0;
                boolean isEmpty = true;
                int setCount = 0;

                for (Rep rep : set.getReps()) {
                    items.add(new HistoryListItem(set.getId(), set.getExercise().getId(), rep.getCount(), rep.getWeight(), userManager.getMeasurementValue()));
                    volume += rep.getWeight();
                    isEmpty = false;
                }

                if (isEmpty)
                    items.add(new HistoryListItem(set.getId(), set.getExercise().getId(), true, view.getEmptySetMessage()));
                else
                    setCount = set.getReps().size();

                HistoryListSection expandableItem = new HistoryListSection(set.getId(), set.getDate(), set.getExercise().getName(), set.getExercise().getId(), volume, setCount, userManager.getMeasurementValue(), isEmpty);
                expandableItem.withIsExpanded(true);
                expandableItem.withSubItems(items);
                adapterData.add(expandableItem);
            }
        }

        view.setupAdapter(adapterData);
    }

    @Override
    public void editEvent(HistoryTrackerEvent event) {

        if (event.eventID == HistoryTrackerEvent.EVENT_EDIT_SET) {
            progressManager.setTodayProgress(event.date);
            view.editTracker(event.exerciseId);
        }

    }
}
