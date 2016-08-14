package com.brandonhogan.liftscout.fragments.home.today;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

public class WorkoutSection implements ParentListItem {

    private String mName;
    private double mVolume;
    private List<WorkoutItem> mItems;

    public WorkoutSection(String name, double volume, List<WorkoutItem> items) {
        mName = name;
        mVolume = volume;
        mItems = items;
    }

    public String getName() {
        return mName;
    }

    public double getVolume() {
        return mVolume;
    }

    @Override
    public List<?> getChildItemList() {
        return mItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
