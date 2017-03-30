package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.events.HistoryTrackerEvent;
import com.brandonhogan.liftscout.interfaces.contracts.CalendarContract;
import com.brandonhogan.liftscout.models.HistoryListItemModel;
import com.brandonhogan.liftscout.models.HistoryListSectionModel;
import com.brandonhogan.liftscout.presenters.CalendarPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;
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
import butterknife.OnClick;

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
    TextView titleTextView;

    @Bind(R.id.calendar_view)
    CompactCalendarView calendar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.workout_no_data)
    LinearLayout noDataLayout;


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

        setTitle(getString(R.string.nav_calendar));

        setupCalendar();
        presenter.viewCreated();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    private void setupCalendar() {

        // This will set the first day to Sunday, instead of Monday
        calendar.setFirstDayOfWeek(1);
        calendar.shouldDrawIndicatorsBelowSelectedDays(true);
        calendar.canScrollHorizontally(1);
        calendar.setUseThreeLetterAbbreviation(true);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                presenter.dateSelected(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                presenter.onMonthScroll(firstDayOfNewMonth);
                presenter.dateSelected(firstDayOfNewMonth);
            }
        });
    }

    public void showDeleteRepAlert(final HistoryListSectionModel section, final int position) {

        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_tracker_delete_rep_title)
                .content(R.string.dialog_tracker_delete_rep_message)
                .neutralText(R.string.delete)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.onDeleteSection(section, position);
                    }
                })
                .positiveText(R.string.cancel).show();
    }

    @Override
    public void setEvents(String monthTitle, Date date, ArrayList<Event> events) {
        calendar.removeAllEvents();
        calendar.setCurrentDate(date);
        calendar.removeAllEvents();
        calendar.addEvents(events);

        titleTextView.setText(monthTitle);
    }

    @Override
    public void onSetDeleted(int position, int count) {
        mAdapter.removeItemRange(position, count);
        checkIfEmpty();
    }

    @Override
    public void setupAdapter(List<HistoryListSectionModel> data) {
        mAdapter = new FastItemAdapter<>();
        mAdapter.withSelectable(true);
        mAdapter.add(data);

        mAdapter.withOnClickListener(new FastAdapter.OnClickListener<HistoryListItemModel>() {
            @Override
            public boolean onClick(View v, IAdapter<HistoryListItemModel> adapter, HistoryListItemModel item, int position) {
                presenter.setClicked(item.exerciseId);
                return true;
            }
        });

        mAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v, IAdapter adapter, IItem item, int position) {
                try {
                    showDeleteRepAlert(((HistoryListSectionModel) mAdapter.getAdapterItem(position)), position);
                }
                catch (Exception ex) {

                }
                return false;
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkIfEmpty();
    }

    @Override
    public String getEmptySetMessage() {
        return getString(R.string.frag_history_empty_set_msg);
    }

    @Override
    public void editTracker(int exerciseId) {
        getNavigationManager().startWorkoutContainer(exerciseId, true);
    }


    // Private Functions
    //

    private void checkIfEmpty() {
        if (mAdapter.getAdapterItemCount() < 1 )
            noDataLayout.setVisibility(View.VISIBLE);
        else
            noDataLayout.setVisibility(View.GONE);
    }

    // Bus Subscriptions
    //

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(HistoryTrackerEvent event) {
        presenter.editEvent(event);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        getNavigationManager().startExerciseContainerAddSet();
    }
}
