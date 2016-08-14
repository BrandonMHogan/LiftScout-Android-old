package com.brandonhogan.liftscout.fragments.home.workout;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TodaySectionedExpandableLayoutHelper implements TodaySectionStateChangeListener {

    //data list
    private LinkedHashMap<TodaySection, ArrayList<TodayItem>> mSectionDataMap = new LinkedHashMap<TodaySection, ArrayList<TodayItem>>();
    private ArrayList<Object> mDataArrayList = new ArrayList<Object>();

    //section map
    //TODO : look for a way to avoid this
    private HashMap<String, TodaySection> mSectionMap = new HashMap<String, TodaySection>();

    //adapter
    private TodaySectionedExpandableGridAdapter mSectionedExpandableGridAdapter;

    //recycler view
    RecyclerView mRecyclerView;

    public TodaySectionedExpandableLayoutHelper(Context context, RecyclerView recyclerView, TodayItemClickListener itemClickListener,
                                           int gridSpanCount) {

        //setting the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, gridSpanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedExpandableGridAdapter = new TodaySectionedExpandableGridAdapter(context, mDataArrayList,
                gridLayoutManager, itemClickListener, this);
        recyclerView.setAdapter(mSectionedExpandableGridAdapter);

        mRecyclerView = recyclerView;
    }

    public void notifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        mSectionedExpandableGridAdapter.notifyDataSetChanged();
    }

    public void addSection(int id, String section, double volume,  ArrayList<TodayItem> items) {
        TodaySection newTodaySection;
        mSectionMap.put(section, (newTodaySection = new TodaySection(id, section, volume)));
        mSectionDataMap.put(newTodaySection, items);
    }

    public void addItem(String section, TodayItem item) {
        mSectionDataMap.get(mSectionMap.get(section)).add(item);
    }

    public void removeItem(String section, TodayItem item) {
        mSectionDataMap.get(mSectionMap.get(section)).remove(item);
    }

    public void removeSection(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    private void generateDataList () {
        mDataArrayList.clear();
        for (Map.Entry<TodaySection, ArrayList<TodayItem>> entry : mSectionDataMap.entrySet()) {
            TodaySection key;
            mDataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                mDataArrayList.addAll(entry.getValue());
        }
    }

    @Override
    public void onTodaySectionStateChanged(TodaySection section, boolean isOpen) {
        if (!mRecyclerView.isComputingLayout()) {
            section.isExpanded = isOpen;
            notifyDataSetChanged();
        }
    }
}