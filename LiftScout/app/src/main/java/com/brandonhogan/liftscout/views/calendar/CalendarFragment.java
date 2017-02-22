package com.brandonhogan.liftscout.views.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class CalendarFragment extends BaseFragment implements CalendarContract.View {

    // Instance
    //
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    // Private Properties
    //
    private View rootView;
    private CalendarContract.Presenter presenter;


    // Binds
    //
    @Bind(R.id.date)
    TextView date;

    @Bind(R.id.calendar_view)
    CompactCalendarView calendar;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_calendar, container, false);

        presenter = new CalendarPresenter(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCalendar();
        presenter.viewCreated();
    }

    private void setupCalendar() {

        calendar.shouldDrawIndicatorsBelowSelectedDays(true);
        calendar.canScrollHorizontally(1);
        calendar.setUseThreeLetterAbbreviation(true);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendar.getEvents(dateClicked);
                Log.d(getTAG(), "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                presenter.onMonthScroll(firstDayOfNewMonth);
            }
        });
    }

    @Override
    public void setEvents(String monthTitle, ArrayList<Event> events) {
        calendar.removeAllEvents();
        calendar.addEvents(events);

        date.setText(monthTitle);
    }
}
