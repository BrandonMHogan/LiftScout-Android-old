package com.brandonhogan.liftscout.views;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.WorkoutContainerAdapter;
import com.brandonhogan.liftscout.events.TrackerEvent;
import com.brandonhogan.liftscout.interfaces.contracts.WorkoutContainerContract;
import com.brandonhogan.liftscout.managers.NotificationServiceManager;
import com.brandonhogan.liftscout.presenters.WorkoutContainerPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

import static android.content.Context.NOTIFICATION_SERVICE;

public class WorkoutContainerFragment extends BaseFragment implements WorkoutContainerContract.View {

    public static final int REST_TIMER_NOTIFICATION_ID = 1231233232;


    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private static final String BUNDLE_REST_TIMER = "restTimerBundle";
    private static final String SAVE_STATE_TIMER = "saveStateTimer";


    // Instance
    //
    public static WorkoutContainerFragment newInstance(int exerciseId, int timer)
    {
        WorkoutContainerFragment frag = new WorkoutContainerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        bundle.putInt(BUNDLE_REST_TIMER, timer);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private WorkoutContainerContract.Presenter presenter;
    private MenuItem timerMenu;

    private NotificationServiceManager notificationServiceManager;

    private com.afollestad.materialdialogs.MaterialDialog deleteDialog;

    private boolean isDisplayed;

    // Binds
    //
    @BindView(R.id.workout_viewpager)
    ViewPager viewPager;

    @BindView(R.id.workout_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.force_focus_layout)
    LinearLayout forceFocusLayout;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_workout_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new WorkoutContainerPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID));
        presenter.viewCreated();

        setTitle(presenter.getExerciseName());

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.workout_tracker_title)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.workout_history_title)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.workout_graphs_title)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        final WorkoutContainerAdapter adapter = new WorkoutContainerAdapter
                (getChildFragmentManager(), presenter.getExerciseId());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationServiceManager = new NotificationServiceManager();
        notificationServiceManager.RestTimerNotification(getActivity().getApplicationContext(), REST_TIMER_NOTIFICATION_ID, true, getString(R.string.rest_timer_with_name, presenter.getExerciseName()), presenter.getExerciseId(), presenter.getDateLong(), 0, notificationManager);

        presenter.restTimerNotification(getArguments().getInt(BUNDLE_REST_TIMER));
    }

    @Override
    public void onStart() {
        super.onStart();
        if(saveState != null) {
            presenter.restTimerNotification(saveState.getInt(SAVE_STATE_TIMER));
            saveState = null;
        }
    }

    @Override
    protected Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putInt(SAVE_STATE_TIMER, presenter.getRemainingRestTime());
        return bundle;
    }

    @Override
    public void onPause() {
        super.onPause();
        isDisplayed = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isDisplayed = true;

        if(presenter.getRemainingRestTime() <= 0)
            notificationServiceManager.clearNotification();

        forceFocusLayout.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        notificationServiceManager.clearNotification();
        notificationServiceManager = null;
        //resetRestTimer();
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
        menu.findItem(R.id.action_main_settings).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);

        menu.findItem(R.id.action_favourite_true).setVisible(presenter.isFavourite());
        menu.findItem(R.id.action_favourite_false).setVisible(!presenter.isFavourite());

        MenuItem deleteMenu = menu.findItem(R.id.action_delete);
        deleteMenu.setVisible(true);

        timerMenu = menu.findItem(R.id.action_timer);
        if (presenter.getRemainingRestTime() > 0) {
            timerMenu.setIcon(null);
            timerMenu.setTitle(Integer.toString(presenter.getRemainingRestTime()));
        }

        timerMenu.setVisible(true);

        MenuItem settingsMenu = menu.findItem(R.id.action_settings_tracker);
        settingsMenu.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeleteAlert();
                return true;
            case R.id.action_timer:
                if (timerMenu.getIcon() == null) {
                    resetRestTimer();
                    return false;
                }
                presenter.onTimerClicked();
                return true;
            case R.id.action_settings_tracker:
                showSettings();
                return true;
            case R.id.action_favourite_true:
                presenter.onFavClicked();

                return true;
            case R.id.action_favourite_false:
                presenter.onFavClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deleteSetSuccess() {
        getNavigationManager().navigateBack(getActivity());
    }

    @Override
    public void onRestTimerTick(int time) {

       notificationServiceManager.updateTimerText(getResources().getQuantityString(R.plurals.timer_message, time, time), time);

        if (timerMenu != null) {
            timerMenu.setIcon(null);
            timerMenu.setTitle(Integer.toString(time));
        }
    }

    @Override
    public void onRestTimerTerminate(boolean vibrate, boolean sound) {

        notificationServiceManager.updateTimerText(getResources().getString(R.string.rest_timer_notification_finished), 0);

        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        }

        if (sound) {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_RING, 100);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 700);
        }

        resetRestTimer();
    }

    @Override
    public void favouriteUpdated() {
        getActivity().invalidateOptionsMenu();
    }

    private void resetRestTimer() {

        // Boolean to keep track of if we are in the foreground or not. Only hide the notification if we are
        if(isDisplayed)
            notificationServiceManager.clearNotification();

        if (timerMenu != null) {

            if(android.os.Build.VERSION.SDK_INT >= 21){
                timerMenu.setIcon(getResources().getDrawable(R.drawable.ic_timer_white_24dp, getActivity().getTheme()));
            } else {
                timerMenu.setIcon(getResources().getDrawable(R.drawable.ic_timer_white_24dp));
            }

            timerMenu.setTitle(getString(R.string.timer));
        }
        presenter.onRestTimerStop();
    }

    private void showSettings() {
        getNavigationManager().startExerciseDetail(presenter.getExerciseId(), presenter.getCategoryId());
    }

    private void showDeleteAlert() {

        deleteDialog = new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_tracker_delete_set_title)
                .content(getString(R.string.dialog_tracker_delete_set_message, presenter.getExerciseName()))
                .positiveText(R.string.delete)
                .onPositive(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteDialog.cancel();
                        presenter.onDeleteSet();
                    }
                })
                .negativeText(R.string.cancel)
                .build();

        deleteDialog.show();
    }

    // Bus Subscriptions
    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(TrackerEvent event) {
        if (event.isNew && presenter.getExerciseRestAutoStart())
            presenter.onTimerClicked();

    }
}
