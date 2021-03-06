package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.OnBackPressListener;
import com.brandonhogan.liftscout.interfaces.contracts.ExerciseDetailContract;
import com.brandonhogan.liftscout.presenters.ExerciseDetailPresenter;
import com.brandonhogan.liftscout.repository.model.Category;
import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.utils.constants.ConstantValues;
import com.brandonhogan.liftscout.utils.controls.NumberPicker;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public class ExerciseDetailFragment extends BaseFragment implements ExerciseDetailContract.View, OnBackPressListener {


    // Private Static Properties
    //
    private final static String BUNDLE_EXERCISE_ID = "exerciseIdBundle";
    private final static String BUNDLE_CATEGORY_ID = "categoryIdBundle";
    private final static String BUNDLE_VALID_CATEGORY_ID = "validCategoryIdBundle";
    private final static String BUNDLE_NEW_EXERCISE = "newExerciseBundle";


    // Instance
    //
    public static ExerciseDetailFragment newInstance(int exerciseId, int categoryId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);
        args.putBoolean(BUNDLE_VALID_CATEGORY_ID, true);
        args.putBoolean(BUNDLE_NEW_EXERCISE, false);

        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static ExerciseDetailFragment newInstance(boolean validCategoryId, int categoryId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_EXERCISE_ID, 0);
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);
        args.putBoolean(BUNDLE_VALID_CATEGORY_ID, validCategoryId);
        args.putBoolean(BUNDLE_NEW_EXERCISE, true);

        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    // Private Properties
    //
    private ExerciseDetailContract.Presenter presenter;
    private Toast toast;
    private boolean canLeave = false;

    @BindView(R.id.name_text)
    EditText nameText;

    @BindView(R.id.category_spinner)
    Spinner categorySpinner;

    @BindView(R.id.increment_spinner)
    Spinner incrementSpinner;

    @BindView(R.id.rest_timer)
    NumberPicker restTimerPicker;

    @BindView(R.id.auto_switch)
    Switch autoSwitch;

    @BindView(R.id.sound_switch)
    Switch soundSwitch;

    @BindView(R.id.vibrate_switch)
    Switch vibrateSwitch;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_exercise_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);

        presenter = new ExerciseDetailPresenter(this,
                this.getArguments().getBoolean(BUNDLE_NEW_EXERCISE),
                this.getArguments().getInt(BUNDLE_EXERCISE_ID),
                this.getArguments().getInt(BUNDLE_CATEGORY_ID),
                this.getArguments().getBoolean(BUNDLE_VALID_CATEGORY_ID));

        setTitle(getString(R.string.exercise_new));
        setupControls();

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
        menu.findItem(R.id.action_save).setVisible(true);

        menu.findItem(R.id.action_favourite_true).setVisible(presenter.isFavourite());
        menu.findItem(R.id.action_favourite_false).setVisible(!presenter.isFavourite());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                presenter.onSave(categorySpinner.getSelectedItemPosition(), nameText.getText().toString(), incrementSpinner.getSelectedItemPosition(), restTimerPicker.getNumberAsInt(), autoSwitch.isChecked(), soundSwitch.isChecked(), vibrateSwitch.isChecked());
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
    public boolean onBackPress() {

        if (canLeave)
            return true;

        new MaterialDialog.Builder(getActivity())
                .title(R.string.exercise_leave_without_saving_title)
                .content(R.string.exercise_leave_without_saving)
                .neutralText(R.string.leave)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        canLeave = true;
                        getNavigationManager().navigateBack(getActivity());
                    }
                })
                .positiveText(R.string.cancel).show();
        return false;
    }

    private void setupControls() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_view, ConstantValues.increments_string);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incrementSpinner.setAdapter(dataAdapter);
        incrementSpinner.setSelection(ConstantValues.increments.indexOf(presenter.getDefaultIncrement()), true);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_view, presenter.getCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        restTimerPicker.setNumber(60);
    }

    @Override
    public void setupControlValues(Exercise exercise) {
        nameText.setText(exercise.getName());
        restTimerPicker.setNumber(exercise.getRestTimer());
        vibrateSwitch.setChecked(exercise.isRestVibrate());
        soundSwitch.setChecked(exercise.isRestSound());
        autoSwitch.setChecked(exercise.isRestAutoStart());

        if (ConstantValues.increments.contains(exercise.getIncrement()))
            incrementSpinner.setSelection(ConstantValues.increments.indexOf(exercise.getIncrement()), true);
        else
            incrementSpinner.setSelection(ConstantValues.increments.indexOf(presenter.getDefaultIncrement()), true);
    }

    @Override
    public void setupControlCategory(Category category) {
        if (category != null) {
            categorySpinner.setSelection(presenter.getCategories().indexOf(category.getName()));
        }
    }

    @Override
    public void onSaveSuccess() {
        canLeave = true;
        toast.setText(R.string.exercise_setting_saved);
        toast.show();
        getNavigationManager().navigateBack(getActivity());
    }

    @Override
    public void onSaveFailure(int errorMsg) {
        canLeave = false;
        toast.setText(errorMsg);
        toast.show();
    }

    @Override
    public void setFav(boolean isSet) {
        getActivity().invalidateOptionsMenu();
    }
}
