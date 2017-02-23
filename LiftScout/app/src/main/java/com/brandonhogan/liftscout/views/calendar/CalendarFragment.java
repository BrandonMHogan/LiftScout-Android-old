package com.brandonhogan.liftscout.views.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.history.HistoryListItem;
import com.brandonhogan.liftscout.views.workout.history.HistoryListSection;
import com.brandonhogan.liftscout.views.workout.history.HistoryTrackerEvent;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private FastItemAdapter mAdapter;


    // Binds
    //
    @Bind(R.id.date)
    TextView date;

    @Bind(R.id.calendar_view)
    CompactCalendarView calendar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setupCalendar() {

        calendar.shouldDrawIndicatorsBelowSelectedDays(true);
        calendar.canScrollHorizontally(1);
        calendar.setUseThreeLetterAbbreviation(true);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendar.getEvents(dateClicked);
                presenter.dateSelected(dateClicked);
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

    @Override
    public void setupAdapter(List<HistoryListSection> data) {
        mAdapter = new FastItemAdapter<>();
        mAdapter.withSelectable(true);
        mAdapter.add(data);

        mAdapter.withOnClickListener(new FastAdapter.OnClickListener<HistoryListItem>() {
            @Override
            public boolean onClick(View v, IAdapter<HistoryListItem> adapter, HistoryListItem item, int position) {
                getNavigationManager().startWorkoutContainer(item.exerciseId);
                return true;
            }
        });

        mAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v, IAdapter adapter, IItem item, int position) {
                mAdapter.collapse();
                return false;
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public String getEmptySetMessage() {
        return getString(R.string.frag_history_empty_set_msg);
    }

    @Override
    public void editTracker(int exerciseId) {
        getNavigationManager().startWorkoutContainer(exerciseId, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(HistoryTrackerEvent event) {
        presenter.editEvent(event);
    }
}
