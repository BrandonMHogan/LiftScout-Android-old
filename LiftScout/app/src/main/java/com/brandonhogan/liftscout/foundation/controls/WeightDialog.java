package com.brandonhogan.liftscout.foundation.controls;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.brandonhogan.liftscout.R;

import java.text.ParseException;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class WeightDialog {


    // Private Static
    //
    private static final String TAG = "WeightDialog";


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
    private long intPart;
    private double fracPart;
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
        this.isDarkTheme = isDarkTheme;

        this.weight = weight;
        this.intPart = (long) weight;
        String decimalString = Double.toString(weight);
        decimalString = decimalString.substring(decimalString.indexOf(".")+1);

        this.fracPart = Double.parseDouble(decimalString);

    }


    // Private Functions
    //
    private void onSave() {
        dismiss();

        try {
            String weightString = weightPicker.getValue() + "." + decimalPicker.getValue();
            weight = java.text.NumberFormat.getInstance().parse(weightString).doubleValue();

        } catch (ParseException ex) {
            Log.e(TAG, "Failed to convert string to double :" + ex.getMessage());
        }

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

            weightPicker = (MaterialNumberPicker) dialog.getCustomView().findViewById(R.id.weightNumberPicker);
            decimalPicker = (MaterialNumberPicker) dialog.getCustomView().findViewById(R.id.decimalNumberPicker);

            weightPicker.setValue((int)intPart);
            decimalPicker.setValue((int)fracPart);

        }

        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}