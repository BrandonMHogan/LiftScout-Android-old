package com.brandonhogan.liftscout.views.workout.graph;

import com.brandonhogan.liftscout.core.constants.Charts;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import io.realm.RealmResults;

/**
 * Created by Brandon on 2/15/2017.
 * Description :
 */

public class GraphPresenter implements GraphContract.Presenter {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    UserManager userManager;

    // Private Properties
    //
    private GraphContract.View view;
    private int exerciseId;
    private int position = 0;
    private List<GraphDataSet> items;
    private int currentGraphType;
    private ArrayList<String> graphTypes;

    // Constructor
    //
    public GraphPresenter(GraphContract.View view, int exerciseId) {
        Injector.getAppComponent().inject(this);

        this.view = view;
        this.exerciseId = exerciseId;
    }

    @Override
    public void viewCreated(int position) {
        this.position = position;
        setupChartTypes();
        update();
    }

    @Override
    public void update(int position) {
        this.position = position;
        update();
    }

    @Override
    public void update() {
        RealmResults<Set> sets = progressManager.getSetsByExercise(exerciseId);
        Calendar calendar = Calendar.getInstance();

        switch (position) {
            case 0:
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case 1:
                calendar.add(Calendar.MONTH, -1);
                break;
            case 2:
                calendar.add(Calendar.MONTH, -3);
                break;
            case 3:
                calendar.add(Calendar.MONTH, -6);
                break;
            case 4:
                calendar.add(Calendar.YEAR, -1);
                break;
            default:
                calendar.add(Calendar.YEAR, -10);
                break;

        }

        sets = sets.where().between("date", calendar.getTime(), new Date()).findAll();

        items = new LinkedList<>();

        switch (graphTypes.get(currentGraphType)) {
            case Charts.WORKOUT_VOLUME:
                setupWorkoutVolume(sets);
                break;
            case Charts.MAX_WEIGHT:
                setupMaxWeight(sets);
                break;
            case Charts.MAX_REPS:
                setupMaxRep(sets);
                break;
            case Charts.TOTAL_REPS:
                setupTotalReps(sets);
                break;
        }

    }

    @Override
    public void onGraphTypeSelected(int position) {
        currentGraphType = position;
        update();
    }

    @Override
    public void onItemSelected(Entry entry) {

        String value = "";

        if (graphTypes.get(currentGraphType).equals(Charts.MAX_REPS) || graphTypes.get(currentGraphType).equals(Charts.TOTAL_REPS))
            value = Float.toString(((long)entry.getY())) + " reps";
        else
            value = Float.toString(((long)entry.getY())) + " " + userManager.getMeasurementValue();

        view.setSelected(BhDate.toSimpleStringDate(getDateByFloat(entry.getX())), value);
    }

    private void setupChartTypes() {
        graphTypes = new ArrayList<>();
        graphTypes.add(Charts.WORKOUT_VOLUME);
        graphTypes.add(Charts.MAX_WEIGHT);
        graphTypes.add(Charts.MAX_REPS);
        graphTypes.add(Charts.TOTAL_REPS);
        view.populateGraphTypes(graphTypes, 0);
    }

    private Date getDateByFloat(float value) {
        Calendar calendar = Calendar.getInstance();

        for(GraphDataSet item : items) {
            if ((float)item.getId() == value) {
                calendar.setTimeInMillis(item.getId());
                break;
            }
        }
        return calendar.getTime();
    }

    private void setupWorkoutVolume(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                double volume = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    volume += rep.getWeight() * rep.getCount();
                }

                if (volume > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), volume);
                    items.add(item);
                }

            }
        }
        view.setGraph(items, uniqueDateCount);
    }

    private void setupMaxWeight(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                double max = 0;
                double weight = 0;
                int reps = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    double value = rep.getWeight();

                    if (value > max) {
                        max = value;
                        weight = rep.getWeight();
                        reps = rep.getCount();
                    }
                }

                if (max > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), max, weight, reps);
                    items.add(item);
                }

            }
        }
        view.setGraph(items, uniqueDateCount);
    }

    private void setupMaxRep(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                double max = 0;
                double weight = 0;
                int reps = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    double value = rep.getCount();

                    if (value > max) {
                        max = value;
                        weight = rep.getWeight();
                        reps = rep.getCount();
                    }
                }

                if (max > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), max, weight, reps);
                    items.add(item);
                }

            }
        }
        view.setGraph(items, uniqueDateCount);
    }

    private void setupTotalReps(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                int value = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    value += rep.getCount();
                }

                if (value > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), value);
                    items.add(item);
                }

            }
        }
        view.setGraph(items, uniqueDateCount);
    }
}
