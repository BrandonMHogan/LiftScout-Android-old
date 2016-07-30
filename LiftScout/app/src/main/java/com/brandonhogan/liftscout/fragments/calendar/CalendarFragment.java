package com.brandonhogan.liftscout.fragments.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.brandonhogan.liftscout.fragments.calendar.decorators.EventDecorator;
import com.brandonhogan.liftscout.fragments.calendar.decorators.OneDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;

public class CalendarFragment extends BaseFragment implements OnDateSelectedListener, OnMonthChangedListener {

    // Instance
    //
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    // Private Properties
    //
    private View rootView;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    // Binds
    //
    @Bind(R.id.calendarView)
    MaterialCalendarView widget;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_calendar, null);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        widget.setTopbarVisible(false);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());
        setMinMax(calendar);

        widget.addDecorators(
                oneDayDecorator
        );

        setEvents();

        // widget.setSelectedDate(CalendarDay.from(2016,7,27));
        // widget.setCurrentDate(CalendarDay.from(2016,7,27),true);
    }



    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
        Toast.makeText(getContext(), "Day: " + date.getDate().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        setTitle(formatter.format(date.getDate()));
    }

    // Sets the calendars min and max ranges
    private void setMinMax(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR) + 2, Calendar.DECEMBER, 31);
        widget.setMaximumDate(calendar.getTime());

        calendar.set(2010, Calendar.JANUARY, 1);
        widget.setMinimumDate(calendar.getTime());
    }

    private void setEvents() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        ArrayList<CalendarDay> dates = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            CalendarDay day = CalendarDay.from(calendar);
            dates.add(day);
            calendar.add(Calendar.DATE, 5);
        }

        widget.addDecorator(new EventDecorator(Color.RED, dates));
    }
}
