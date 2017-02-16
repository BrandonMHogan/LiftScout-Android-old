package com.brandonhogan.liftscout.views.workout.graph;

import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;

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

    // Private Properties
    //
    private GraphContract.View view;
    private int exerciseId;
    private int position = 0;

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

        List<GraphDataSet> items = new LinkedList<>();

        int uniqueDateCount = 0;
        ArrayList<Date> dates = new ArrayList<>();

        if (sets != null) {
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

    @Override
    public void onTypeSelected(String type) {

    }
}
