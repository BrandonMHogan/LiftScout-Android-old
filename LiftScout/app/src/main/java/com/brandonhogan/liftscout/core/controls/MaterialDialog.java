package com.brandonhogan.liftscout.core.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.brandonhogan.liftscout.R;

/**
 * Created by Brandon on 3/6/2017.
 * Description :
 */

public class MaterialDialog {

    com.afollestad.materialdialogs.MaterialDialog dialog;


    public MaterialDialog(Context context,
                          int titleRes,
                          int customViewRes,
                          boolean inScrollView,
                          com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback callback) {

        View view = LayoutInflater.from(context).inflate(customViewRes, null);

        dialog = new com.afollestad.materialdialogs.MaterialDialog.Builder(context)
                .title(titleRes)
                .customView(view, inScrollView)
                .positiveText(R.string.save)
                .onPositive(callback)
                .negativeText(R.string.close)
                .build();
    }


    public void show() { dialog.show(); }
    public void dismiss() { dialog.dismiss(); }

    public View getView() {
        return dialog.getView();
    }
}
