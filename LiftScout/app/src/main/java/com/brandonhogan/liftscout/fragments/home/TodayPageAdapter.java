package com.brandonhogan.liftscout.fragments.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.brandonhogan.liftscout.foundation.utils.BhDate;

import java.util.Calendar;
import java.util.Date;

public class TodayPageAdapter extends FragmentStatePagerAdapter
{

    private Date currentDate;
    // The total number of possible days that will be shown
    private static int LOOPS_COUNT = 366;
    // The number of days in a direction (forward or back in time)
    public static int TOTAL_DAYS = LOOPS_COUNT / 2;


    // Constructor
    //
    public TodayPageAdapter(FragmentManager manager) {
        super(manager);
    }


    // Overrides
    //
    @Override
    public Fragment getItem(int position) {
        return TodayFragment.newInstance(dateByPosition(position));
    }


    @Override
    public int getCount() {
        return LOOPS_COUNT;
    }


    // Public Functions
    //
    public Date dateByPosition(int position) {
        int realPosition;

        if (position < TOTAL_DAYS)
            realPosition = (position % TOTAL_DAYS);
        else
            realPosition = (position % TOTAL_DAYS) - TOTAL_DAYS;

        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, realPosition);

        currentDate = BhDate.trimTimeFromDate(calendar.getTime());

        return currentDate;
    }
}