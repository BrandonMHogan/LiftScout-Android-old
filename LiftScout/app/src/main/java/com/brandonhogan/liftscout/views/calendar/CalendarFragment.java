package com.brandonhogan.liftscout.views.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
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
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

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
    private SweetAlertDialog dialog;


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

        setTitle(getString(R.string.app_name));

        setupCalendar();
        presenter.viewCreated();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem addMenu = menu.findItem(R.id.action_add);
        addMenu.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                getNavigationManager().startCategoryListAddSet();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onResume() {
        super.onResume();
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
                presenter.dateSelected(firstDayOfNewMonth);
            }
        });
    }

    public void showDeleteRepAlert(final HistoryListSection section, final int position) {

        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_tracker_delete_rep_title))
                .setContentText(getString(R.string.dialog_tracker_delete_rep_message))
                .setConfirmText(getString(R.string.delete))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.cancel();
                        presenter.onDeleteSection(section, position);
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
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
    public void setupAdapter(List<HistoryListSection> data) {
        mAdapter = new FastItemAdapter<>();
        mAdapter.withSelectable(true);
        mAdapter.add(data);

        mAdapter.withOnClickListener(new FastAdapter.OnClickListener<HistoryListItem>() {
            @Override
            public boolean onClick(View v, IAdapter<HistoryListItem> adapter, HistoryListItem item, int position) {
                presenter.setClicked(item.exerciseId);
                return true;
            }
        });

        mAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v, IAdapter adapter, IItem item, int position) {
                try {
                    showDeleteRepAlert(((HistoryListSection) mAdapter.getAdapterItem(position)), position);
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

    @OnClick(R.id.start_workout)
    void startWorkoutClicked() {
        getNavigationManager().startCategoryListAddSet();
    }


    // Bus Subscriptions
    //

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(HistoryTrackerEvent event) {
        presenter.editEvent(event);
    }
}
