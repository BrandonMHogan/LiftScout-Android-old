package com.brandonhogan.liftscout.views.exercises;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;

public class ExerciseEditDialog {
    // Private Static
    //
    private static final String TAG = "CategoryEditDialog";


    // Interface Listener
    //
    public interface ExerciseEditDialogListener {
        void onCancelExerciseEditDialog();
        void onSaveExerciseEditDialog(ExerciseListModel category);
    }


    // Private Properties
    //
    private Activity activity;
    private ExerciseListModel exercise;
    private boolean isNew;
    private boolean isDarkTheme;
    private MaterialDialog dialog;
    private ExerciseEditDialogListener listener;

    private EditText nameEditText;



    // Constructor
    //
    public ExerciseEditDialog(Activity activity, ExerciseEditDialogListener listener, boolean isDarkTheme,
                              ExerciseListModel exercise) {
        this.activity = activity;
        this.listener = listener;
        this.isDarkTheme = isDarkTheme;

        this.exercise = exercise;

        if (this.exercise == null) {
            isNew = true;
            this.exercise = new ExerciseListModel();
        }

    }


    // Private Functions
    //
    private void onSave() {
        dismiss();

        exercise.setName(nameEditText.getText().toString());

        listener.onSaveExerciseEditDialog(exercise);
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
                    .customView(customTheme, false)
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

            nameEditText = (EditText) dialog.getCustomView().findViewById(R.id.name);


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