package com.brandonhogan.liftscout.fragments.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.Calendar;

public class TodayPageAdapter extends FragmentStatePagerAdapter
{
    // The total number of possible days that will be shown
    private static int LOOPS_COUNT = 366;
    // The number of days in a direction (forward or back in time)
    public static int TOTAL_DAYS = LOOPS_COUNT / 2;


    public TodayPageAdapter(FragmentManager manager) {
        super(manager);
    }


    @Override
    public Fragment getItem(int position) {
        int realPosition;

        if (position < TOTAL_DAYS)
            realPosition = (position % TOTAL_DAYS);
        else
            realPosition = (position % TOTAL_DAYS) - TOTAL_DAYS;





        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, realPosition);

        return TodayFragment.newInstance(calendar.getTime());
    }


    @Override
    public int getCount() {
        return LOOPS_COUNT;
    }
}