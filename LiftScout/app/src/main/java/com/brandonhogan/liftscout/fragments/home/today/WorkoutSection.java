package com.brandonhogan.liftscout.fragments.home.today;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

public class WorkoutSection implements ParentListItem {

    private String mName;
    private List<WorkoutItem> mItems;

    public WorkoutSection(String name, List<WorkoutItem> items) {
        mName = name;
        mItems = items;
    }

    public String getName() {
        return mName;
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
