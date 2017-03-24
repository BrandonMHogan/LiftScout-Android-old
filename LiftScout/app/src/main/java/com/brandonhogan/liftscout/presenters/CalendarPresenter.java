package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.interfaces.contracts.CalendarContract;
import com.brandonhogan.liftscout.managers.CalendarManager;
import com.brandonhogan.liftscout.managers.ProgressManager;
import com.brandonhogan.liftscout.managers.RecordsManager;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.model.CalendarEvent;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ExerciseRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ExerciseRepoImpl;
import com.brandonhogan.liftscout.models.HistoryListItemModel;
import com.brandonhogan.liftscout.models.HistoryListSectionModel;
import com.brandonhogan.liftscout.events.HistoryTrackerEvent;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.mikepenz.fastadapter.IItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;

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

    @Inject
    RecordsManager recordsManager;


    // Private Properties
    //
    private CalendarContract.View view;
    private ArrayList<HistoryListSectionModel> adapterData;
    private Date date;
    private ExerciseRepo exerciseRepo;
    private CategoryRepo categoryRepo;


    // Constructor
    //
    public CalendarPresenter(CalendarContract.View view) {
        Injector.getAppComponent().inject(this);
        exerciseRepo = new ExerciseRepoImpl();
        categoryRepo = new CategoryRepoImpl();
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
        view.setEvents(monthTitle, date, getEvents(cal));

        updateAdapter();
    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        date = firstDayOfNewMonth;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String monthTitle = BhDate.toMonthYearStringDate(cal.getTime());
        view.setEvents(monthTitle, date, getEvents(cal));
    }

    @Override
    public void dateSelected(Date date) {
        this.date = date;
        progressManager.setTodayProgress(date);
        updateAdapter();
    }

    @Override
    public void setClicked(int exerciseId) {
        progressManager.setTodayProgress(date);
        view.editTracker(exerciseId);
    }

    @Override
    public void onDeleteSection(HistoryListSectionModel section, int position) {
        adapterData.remove(adapterData.indexOf(section));

        //The adapter will remove a range. Which is the children, plus the section
        int count = section.getSubItems().size() + 1;

        progressManager.deleteSet(section.setId);
        progressManager.clearUpdatedSet();
        view.onSetDeleted(position, count);
        onMonthScroll(date);
    }

    // Private Functions
    //
    private ArrayList<Event> getEvents(Calendar cal) {

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
            int setCounter = 0;
            for (Set set : sets) {

                List<IItem> items = new LinkedList<>();
                double volume = 0;
                boolean isEmpty = true;
                int setCount = 0;

                int repCounter = 0;
                for (Rep rep : set.getReps()) {
                    items.add(new HistoryListItemModel(set.getId(), set.getExercise().getId(), set.getDate(), rep.getCount(), rep.getWeight(),
                            userManager.getMeasurementValue(), recordsManager.isRecord(rep.getId()), set.getReps().size() - 1 == repCounter));
                    volume += rep.getWeight();
                    isEmpty = false;
                    repCounter ++;
                }

                if (isEmpty)
                    items.add(new HistoryListItemModel(set.getId(), set.getExercise().getId(), set.getDate(), true, view.getEmptySetMessage()));
                else
                    setCount = set.getReps().size();

                HistoryListSectionModel expandableItem = new HistoryListSectionModel(set.getId(), set.getDate(), set.getExercise().getName(), set.getExercise().getId(), volume, setCount, userManager.getMeasurementValue(), isEmpty, setCounter == 0);
                expandableItem.withIsExpanded(true);
                expandableItem.withSubItems(items);
                adapterData.add(expandableItem);
                setCounter ++;
            }
        }

        view.setupAdapter(adapterData);
    }

    @Override
    public void editEvent(HistoryTrackerEvent event) {

        if (event.eventID == HistoryTrackerEvent.EVENT_EDIT_SET) {
            setClicked(event.exerciseId);
        }
    }
}
