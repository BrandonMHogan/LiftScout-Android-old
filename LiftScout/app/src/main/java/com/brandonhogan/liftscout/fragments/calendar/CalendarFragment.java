package com.brandonhogan.liftscout.fragments.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.AppController;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BHFragment;
import com.brandonhogan.liftscout.fragments.calendar.decorators.EventDecorator;
import com.brandonhogan.liftscout.fragments.calendar.decorators.HighlightWeekendsDecorator;
import com.brandonhogan.liftscout.fragments.calendar.decorators.MySelectorDecorator;
import com.brandonhogan.liftscout.fragments.calendar.decorators.OneDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalendarFragment extends BHFragment implements OnDateSelectedListener {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    public CalendarFragment() {
        super(AppController.getInstance().getString(R.string.nav_calendar));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_calendar, container, false);
        ButterKnife.bind(this, view);

        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        widget.setMinimumDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        widget.setMaximumDate(calendar.getTime());

        widget.addDecorators(
                new MySelectorDecorator(getActivity()),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        setEvents();

        return view;
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
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
