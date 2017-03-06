package com.brandonhogan.liftscout.core.managers;

import com.brandonhogan.liftscout.core.model.CalendarEvent;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.ProgressRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.impl.ProgressRepoImpl;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by Brandon on 2/22/2017.
 * Description :
 */

public class CalendarManager {


    // Private Properties
    //
    private ProgressRepo progressRepo;
    private CategoryRepo categoryRepo;

    // Constructor
    //
    public CalendarManager() {
        progressRepo = new ProgressRepoImpl();
        categoryRepo = new CategoryRepoImpl();
    }

    public ArrayList<CalendarEvent> getEventsForMonthYear(int month, int year) {

        ArrayList<CalendarEvent> events = new ArrayList<>();
        RealmResults<Progress> progresses = progressRepo.getAllProgressForMonth(month, year);

        if(progresses.isValid() && !progresses.isEmpty()) {

            //Loops through all the progresses returned
            for(Progress progress : progresses) {
                ArrayList<Integer> categoryTypes = new ArrayList<>();

                //Each set can contain different category types. Loops through to find distinct types
                for (Set set : progress.getSets()) {

                    int categoryId = set.getExercise().getCategoryId();
                    if (categoryTypes.contains(categoryId))
                        continue;

                    categoryTypes.add(categoryId);
                    Category category = categoryRepo.getCategory(categoryId);

                    CalendarEvent event = new CalendarEvent(category.getColor(), progress.getDate());
                    events.add(event);
                }
            }
        }

        return events;
    }
}
