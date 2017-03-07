package com.brandonhogan.liftscout.views.workout;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.core.controls.MaterialDialog;
import com.brandonhogan.liftscout.core.controls.NotificationChrono;
import com.brandonhogan.liftscout.core.controls.NumberPicker;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.NOTIFICATION_SERVICE;

public class WorkoutContainerFragment extends BaseFragment implements WorkoutContainerContract.View {


    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";


    // Instance
    //
    public static WorkoutContainerFragment newInstance(int exerciseId)
    {
        WorkoutContainerFragment frag = new WorkoutContainerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
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
    private NotificationChrono notificationChrono;

    private MaterialDialog settingsDialog;
    private SweetAlertDialog deleteDialog;


    // Binds
    //
    @Bind(R.id.workout_viewpager)
    ViewPager viewPager;

    @Bind(R.id.workout_tab_layout)
    TabLayout tabLayout;


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

        presenter = new WorkoutContainerPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
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
        viewPager.setOffscreenPageLimit(4);
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
        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationChrono = new NotificationChrono(getActivity().getApplicationContext(), 123123, true, getString(R.string.rest_timer), notificationManager);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetRestTimer();
    }

    @Override
    public void deleteSetSuccess() {
        getNavigationManager().navigateBack(getActivity());
    }

    @Override
    public void onRestTimerTick(int time) {
        notificationChrono.updateNotification(getResources().getQuantityString(R.plurals.timer_message, time, time));
        timerMenu.setIcon(null);
        timerMenu.setTitle(Integer.toString(time));
    }

    @Override
    public void onRestTimerTerminate(boolean vibrate) {
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        }
        resetRestTimer();
    }

    private void resetRestTimer() {
        notificationChrono.clearNotification();
        timerMenu.setIcon(getResources().getDrawable(R.drawable.ic_timer_white_48dp, getActivity().getTheme()));
        timerMenu.setTitle(getString(R.string.timer));
        presenter.onRestTimerStop();
    }

    private void setupSettings() {

        settingsDialog = new MaterialDialog(
                getActivity(),
                R.string.settings,
                R.layout.dialog_tracker_settings,
                false,
                new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull
                                                com.afollestad.materialdialogs.MaterialDialog dialog,
                                        @android.support.annotation.NonNull
                                                DialogAction which) {

                        View view = dialog.getView();
                        NumberPicker picker = (NumberPicker)view.findViewById(R.id.number_picker);

                        presenter.onSettingsSave(picker.getNumberAsInt(), true);
                    }
                });

        View view = settingsDialog.getView();
        NumberPicker picker = (NumberPicker)view.findViewById(R.id.number_picker);
        picker.setNumber(presenter.getExerciseRestTimer());
    }

    private void showSettings() {
        settingsDialog.show();
    }

    private void showDeleteAlert() {
        String message =
                String.format(getString(R.string.dialog_tracker_delete_set_message)
                        , presenter.getExerciseName());

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
}
