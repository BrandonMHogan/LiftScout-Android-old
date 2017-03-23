package com.brandonhogan.liftscout.views.exercises;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 3/23/2017.
 * Description :
 */

public class ExerciseFilter extends Filter {

    private List<ExerciseListModel> exerciseList;
    private List<ExerciseListModel> filteredExerciseList;
    private ExerciseListAdapter adapter;

    public ExerciseFilter(List<ExerciseListModel> exerciseList, ExerciseListAdapter adapter) {
        this.adapter = adapter;
        this.exerciseList = exerciseList;
        this.filteredExerciseList = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredExerciseList.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredContactList
        for (final ExerciseListModel item : exerciseList) {
            if (item.getName().toLowerCase().trim().contains(constraint)) {
                filteredExerciseList.add(item);
            }
        }

        results.values = filteredExerciseList;
        results.count = filteredExerciseList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setAdapterList(filteredExerciseList);
    }
}