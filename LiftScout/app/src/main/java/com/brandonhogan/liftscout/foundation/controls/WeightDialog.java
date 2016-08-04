package com.brandonhogan.liftscout.foundation.controls;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.brandonhogan.liftscout.R;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class WeightDialog {


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
    private MaterialNumberPicker weightPicker;
    private MaterialNumberPicker decimalPicker;
    private MaterialDialog dialog;
    private WeightDialogListener listener;


    // Constructor
    //
    public WeightDialog(Activity activity, WeightDialogListener listener, double weight, boolean isDarkTheme) {
        this.activity = activity;
        this.listener = listener;
        this.weight = weight;
        this.isDarkTheme = isDarkTheme;
    }


    // Private Functions
    //
    private void onSave() {
        dismiss();
        listener.onSaveWeightDialog(weight);
    }

    private void onCancel() {
        dismiss();
        listener.onCancelWeightDialog();
    }


    // Public Functions
    //
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