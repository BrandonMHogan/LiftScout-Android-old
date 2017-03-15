package com.brandonhogan.liftscout.views.workout;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.controls.MaterialDialog;
import com.brandonhogan.liftscout.core.controls.NumberPicker;
import com.brandonhogan.liftscout.core.managers.NotificationServiceManager;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.NOTIFICATION_SERVICE;

public class WorkoutContainerFragment extends BaseFragment implements WorkoutContainerContract.View {

    public static final int REST_TIMER_NOTIFICATION_ID = 1231233232;


    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private static final String BUNDLE_REST_TIMER = "restTimerBundle";


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
    private View rootView;
    private WorkoutContainerContract.Presenter presenter;

    private MenuItem deleteMenu;
    private MenuItem settingsMenu;
    private MenuItem timerMenu;

    private NotificationManager notificationManager;
    private NotificationServiceManager notificationServiceManager;

    private MaterialDialog settingsDialog;
    private SweetAlertDialog deleteDialog;

    private boolean isDisplayed;

    // Binds
    //
    @Bind(R.id.workout_viewpager)
    ViewPager viewPager;

    @Bind(R.id.workout_tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.force_focus_layout)
    LinearLayout forceFocusLayout;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_container, container, false);
        return rootView;
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
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


        setupSettings();
        notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationServiceManager = new NotificationServiceManager();
        notificationServiceManager.RestTimerNotification(getActivity().getApplicationContext(), REST_TIMER_NOTIFICATION_ID, true, getString(R.string.rest_timer_with_name, presenter.getExerciseName()), presenter.getExerciseId(), presenter.getDateLong(), 0, notificationManager);

        presenter.restTimerNotification(getArguments().getInt(BUNDLE_REST_TIMER));
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
        deleteMenu = menu.findItem(R.id.action_delete);
        deleteMenu.setVisible(true);

        timerMenu = menu.findItem(R.id.action_timer);
        if (presenter.getRemainingRestTime() > 0) {
            timerMenu.setIcon(null);
            timerMenu.setTitle(Integer.toString(presenter.getRemainingRestTime()));
        }

        timerMenu.setVisible(true);

        settingsMenu = menu.findItem(R.id.action_settings);
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
            case R.id.action_settings:
                showSettings();
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
    public void onPause() {
        super.onPause();
        isDisplayed = false;
        EventBus.getDefault().unregister(this);
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

    private void resetRestTimer() {

        // Boolean to keep track of if we are in the foreground or not. Only hide the notification if we are
        if(isDisplayed)
            notificationServiceManager.clearNotification();

        if (timerMenu != null) {
            timerMenu.setIcon(getResources().getDrawable(R.drawable.ic_timer_white_48dp, getActivity().getTheme()));
            timerMenu.setTitle(getString(R.string.timer));
        }
        presenter.onRestTimerStop();
    }

    private void setupSettings() {

        settingsDialog = new MaterialDialog(
                getActivity(),
                R.string.settings,
                R.layout.dialog_tracker_settings,
                true,
                new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull
                                                com.afollestad.materialdialogs.MaterialDialog dialog,
                                        @android.support.annotation.NonNull
                                                DialogAction which) {

                        View view = dialog.getView();
                        NumberPicker picker = (NumberPicker)view.findViewById(R.id.number_picker);
                        SwitchCompat vibrateSwitch = (SwitchCompat)view.findViewById(R.id.vibrate_switch);
                        SwitchCompat soundSwitch = (SwitchCompat)view.findViewById(R.id.sound_switch);
                        SwitchCompat autoStartSwitch = (SwitchCompat)view.findViewById(R.id.auto_start_switch);
                        MaterialSpinner incrementSpinner = (MaterialSpinner)view.findViewById(R.id.increments_spinner);


                        presenter.onSettingsSave(picker.getNumberAsInt(), vibrateSwitch.isChecked(), soundSwitch.isChecked(), autoStartSwitch.isChecked(), incrementSpinner.getSelectedIndex());
                    }
                });

        View view = settingsDialog.getView();
        NumberPicker picker = (NumberPicker)view.findViewById(R.id.number_picker);
        SwitchCompat vibrateSwitch = (SwitchCompat)view.findViewById(R.id.vibrate_switch);
        SwitchCompat soundSwitch = (SwitchCompat)view.findViewById(R.id.sound_switch);
        SwitchCompat autoStartSwitch = (SwitchCompat)view.findViewById(R.id.auto_start_switch);
        MaterialSpinner incrementSpinner = (MaterialSpinner)view.findViewById(R.id.increments_spinner);

        picker.setNumber(presenter.getExerciseRestTimer());
        vibrateSwitch.setChecked(presenter.getExerciseRestVibrate());
        soundSwitch.setChecked(presenter.getExerciseRestSound());
        autoStartSwitch.setChecked(presenter.getExerciseRestAutoStart());

        incrementSpinner.setItems(presenter.getExerciseIncrementList());
        incrementSpinner.setSelectedIndex(presenter.getExerciseIncrementIndex());
    }

    private void showSettings() {
        settingsDialog.show();
    }

    private void showDeleteAlert() {
        String message = getString(R.string.dialog_tracker_delete_set_message, presenter.getExerciseName());

        deleteDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_tracker_delete_set_title))
                .setContentText(message)
                .setConfirmText(getString(R.string.delete))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        deleteDialog.cancel();
                        presenter.onDeleteSet();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

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
