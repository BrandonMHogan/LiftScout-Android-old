package com.brandonhogan.liftscout.fragments.home.today;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.brandonhogan.liftscout.R;

import java.util.List;

public class WorkoutSectionAdapter extends ExpandableRecyclerAdapter<WorkoutSectionViewHolder, WorkoutItemViewHolder> {

    private LayoutInflater mInflator;

    public WorkoutSectionAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public WorkoutSectionViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.workout_section, parentViewGroup, false);
        return new WorkoutSectionViewHolder(recipeView);
    }

    @Override
    public WorkoutItemViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.workout_item, childViewGroup, false);
        return new WorkoutItemViewHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(WorkoutSectionViewHolder workoutSectionViewHolder, int position, ParentListItem parentListItem) {
        WorkoutSection recipe = (WorkoutSection) parentListItem;
        workoutSectionViewHolder.bind(recipe);
    }

    @Override
    public void onBindChildViewHolder(WorkoutItemViewHolder workoutItemViewHolder, int position, Object childListItem) {
        WorkoutItem workoutItem = (WorkoutItem) childListItem;
        workoutItemViewHolder.bind(workoutItem);
    }
}
