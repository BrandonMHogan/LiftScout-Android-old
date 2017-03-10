package com.brandonhogan.liftscout.views.exercises;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.ConstantValues;
import com.brandonhogan.liftscout.core.controls.NumberPicker;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class ExerciseEditDialog {
    // Private Static
    //
    private static final String TAG = "CategoryEditDialog";


    // Interface Listener
    //
    public interface ExerciseEditDialogListener {
        void onCancelExerciseEditDialog();
        void onSaveExerciseEditDialog(int id, String name, double increment, boolean vibrate, boolean sound, boolean autoStart, int restTimer);
    }


    // Private Properties
    //
    private Activity activity;
    private Exercise exercise;
    private boolean isNew;
    private MaterialDialog dialog;
    private ExerciseEditDialogListener listener;

    private EditText nameEditText;
    private NumberPicker restTimerPicker;
    private MaterialSpinner incrementSpinner;
    private SwitchCompat vibrateSwitch;
    private SwitchCompat soundSwitch;
    private SwitchCompat autoStartSwitch;

    private double defaultIncrement;



    // Constructor
    //
    public ExerciseEditDialog(Activity activity, ExerciseEditDialogListener listener,
                              Exercise exercise, double defaultIncrement) {
        this.activity = activity;
        this.listener = listener;
        this.defaultIncrement = defaultIncrement;

        this.exercise = exercise;

        if (this.exercise == null) {
            isNew = true;
            this.exercise = new Exercise();
        }

    }


    // Private Functions
    //
    private void onSave() {
        dismiss();

        listener.onSaveExerciseEditDialog(
                exercise.getId(),
                nameEditText.getText().toString(),
                ConstantValues.increments.get(incrementSpinner.getSelectedIndex()),
                vibrateSwitch.isChecked(),
                soundSwitch.isChecked(),
                autoStartSwitch.isChecked(),
                restTimerPicker.getNumberAsInt());
    }

    private void onCancel() {
        dismiss();
        listener.onCancelExerciseEditDialog();
    }


    // Public Functions
    //
    public void show() {

        if (dialog == null) {

            View customTheme = LayoutInflater.from(activity).inflate(R.layout.dialog_exercise_edit, null);

            dialog = new MaterialDialog.Builder(activity)
                    .title(isNew ? R.string.dialog_add_exercise_title : R.string.dialog_edit_exercise_title)

                    .customView(customTheme, true)
                    .positiveText(R.string.save)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            onSave();
                        }
                    })
                    .negativeText(R.string.cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            onCancel();
                        }
                    })
                    .build();

            nameEditText = (EditText) dialog.getView().findViewById(R.id.name);

            View view = dialog.getView();
            restTimerPicker = (NumberPicker)view.findViewById(R.id.number_picker);
            vibrateSwitch = (SwitchCompat)view.findViewById(R.id.vibrate_switch);
            soundSwitch = (SwitchCompat)view.findViewById(R.id.sound_switch);
            autoStartSwitch = (SwitchCompat)view.findViewById(R.id.auto_start_switch);
            incrementSpinner = (MaterialSpinner)view.findViewById(R.id.increments_spinner);

            restTimerPicker.setNumber(exercise.getRestTimer());
            vibrateSwitch.setChecked(exercise.isRestVibrate());
            soundSwitch.setChecked(exercise.isRestSound());
            autoStartSwitch.setChecked(exercise.isRestAutoStart());

            incrementSpinner.setItems(ConstantValues.increments);

            if (ConstantValues.increments.contains(exercise.getIncrement()))
                incrementSpinner.setSelectedIndex(ConstantValues.increments.indexOf(exercise.getIncrement()));
            else
                incrementSpinner.setSelectedIndex(ConstantValues.increments.indexOf(defaultIncrement));


            if (!isNew) {
                nameEditText.setText(exercise.getName());
            }
        }

        dialog.show();
        nameEditText.requestFocus();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}