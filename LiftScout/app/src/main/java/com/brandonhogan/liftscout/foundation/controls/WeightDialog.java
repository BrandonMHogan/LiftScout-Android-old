package com.brandonhogan.liftscout.foundation.controls;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.brandonhogan.liftscout.R;

public class WeightDialog {

    // Static Properties
    //
    private static final String WEIGHT_BUNDLE = "weightBundle";
    private static final String THEME_BUNDLE = "themeBundle";
    private static final String LISTENER_BUNDLE = "themeBundle";

    // Static Properties
    //


    public WeightDialog(Activity activity, WeightDialogListener listener, double weight, boolean isDarkTheme) {
//        WeightDialog dialog = new WeightDialog();

//        Bundle args = new Bundle();
//        args.putDouble(WEIGHT_BUNDLE, weight);
//        args.putBoolean(THEME_BUNDLE, isDarkTheme);
//        args.putParcelable(LISTENER_BUNDLE, listener);
//        dialog.setArguments(args);

//        return dialog;

        this.activity = activity;
        this.listener = listener;
        this.weight = weight;
        this.isDarkTheme = isDarkTheme;
    }


    // Interface Listener
    //
    public interface WeightDialogListener {
        void onCancelWeightDialog();
        void onSaveWeightDialog(double weight);
    }


    // Private Properties
    //
    private Activity activity;
    private double weight;
    private boolean isDarkTheme;
    private NumberPicker weightPicker;
    private NumberPicker decimalPicker;
    private MaterialDialog dialog;
    private WeightDialogListener listener;


    // Overrides
    //



    private void onSave() {
        dismiss();
        listener.onSaveWeightDialog(weight);
    }

    private void onCancel() {
        dismiss();
        listener.onCancelWeightDialog();
    }

    public void show() {

        if (dialog == null) {

            View customTheme = LayoutInflater.from(activity).inflate(R.layout.con_weight_dialog, null);

            dialog = new MaterialDialog.Builder(activity)
                    .theme(isDarkTheme ? Theme.DARK : Theme.LIGHT)
                    .title(R.string.dialog_set_weight_title)
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
        }

        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

}