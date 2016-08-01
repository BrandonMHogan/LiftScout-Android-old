package com.brandonhogan.liftscout.fragments.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.Calendar;
import java.util.Date;

public class TodayPageAdapter extends FragmentStatePagerAdapter
{
    public static int LOOPS_COUNT = 1000;
    private static int DAYS = 365;


    public TodayPageAdapter(FragmentManager manager)
    {
        super(manager);
    }


    @Override
    public Fragment getItem(int position)
    {
        position = position % DAYS; // use modulo for infinite cycling

        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, position);

        return TodayFragment.newInstance(calendar.getTime());
    }


    @Override
    public int getCount()
    {

            return DAYS*LOOPS_COUNT; // simulate infinite by big number of products
    }
}