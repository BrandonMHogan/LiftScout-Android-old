package com.brandonhogan.liftscout.views.home.today;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;

import java.util.Calendar;
import java.util.Date;


public class TodayPageAdapter extends FragmentPagerAdapter
{

//    // Private Properties
//    //
//    private Date currentDate;
//    private static int LOOPS_COUNT = 732; // The total number of possible days that will be shown
//
//
//    // Public Properties
//    //
//    public static int TOTAL_DAYS = LOOPS_COUNT / 2; // The number of days in a direction (forward or back in time)
//
//    private Context mContext;
//    private LayoutInflater mLayoutInflater;
//
//    public TodayPageAdapter(final Context context) {
//        mContext = context;
//        mLayoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return LOOPS_COUNT;
//    }
//
//    @Override
//    public int getItemPosition(final Object object) {
//        return POSITION_NONE;
//    }
//
//    @Override
//    public Object instantiateItem(final ViewGroup container, final int position) {
//        final View view;
//
//        view = mLayoutInflater.inflate(R.layout.item, container, false);
//        setupItem(view, LIBRARIES[position]);
//
//        container.addView(view);
//        return view;
//    }
//
//    @Override
//    public boolean isViewFromObject(final View view, final Object object) {
//        return view.equals(object);
//    }
//
//    @Override
//    public void destroyItem(final ViewGroup container, final int position, final Object object) {
//        container.removeView((View) object);
//    }










    // Private Properties
    //
    private Date currentDate;
    private static int LOOPS_COUNT = 732; // The total number of possible days that will be shown


    // Public Properties
    //
    public static int TOTAL_DAYS = LOOPS_COUNT / 2; // The number of days in a direction (forward or back in time)


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
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, getRealPosition(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, getRealPosition(position), object);
    }

    @Override
    public int getItemPosition(Object object) {
        TodayFragment f = (TodayFragment) object;
        if (f != null) {
            f.update();
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return LOOPS_COUNT;
    }


    // Public Functions
    //
    public Date dateByPosition(int position) {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, getRealPosition(position) - TOTAL_DAYS);

        currentDate = BhDate.trimTimeFromDate(calendar.getTime());

        return currentDate;
    }

    public void update() {
        notifyDataSetChanged();
    }


    // Private Properties
    //
    private int getRealPosition(int position) {
        return position % LOOPS_COUNT;
    }
}