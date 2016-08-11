package com.brandonhogan.liftscout.foundation.controls;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.exercises.CategoryListModel;

public class CategoryEditDialog {

    // Private Static
    //
    private static final String TAG = "CategoryEditDialog";


    // Interface Listener
    //
    public interface CategoryEditDialogListener {
        void onCancelCategoryEditDialog();
        void onSaveCategoryEditDialog(CategoryListModel category);
    }


    // Private Properties
    //
    private Activity activity;
    private CategoryListModel category;
    private boolean isNew;
    private boolean isDarkTheme;
    private MaterialDialog dialog;
    private CategoryEditDialogListener listener;

    private EditText nameEditText;

    

    // Constructor
    //
    public CategoryEditDialog(Activity activity, CategoryEditDialogListener listener, boolean isDarkTheme,
                              CategoryListModel category) {
        this.activity = activity;
        this.listener = listener;
        this.isDarkTheme = isDarkTheme;

        this.category = category;

        if (this.category == null) {
            isNew = true;
            this.category = new CategoryListModel();
        }

    }


    // Private Functions
    //
    private void onSave() {
        dismiss();

        category.setName(nameEditText.getText().toString());

        listener.onSaveCategoryEditDialog(category);
    }

    private void onCancel() {
        dismiss();
        listener.onCancelCategoryEditDialog();
    }


    // Public Functions
    //
    public void show() {

        if (dialog == null) {

            View customTheme = LayoutInflater.from(activity).inflate(R.layout.con_category_edit_dialog, null);

            dialog = new MaterialDialog.Builder(activity)
                    .theme(isDarkTheme ? Theme.DARK : Theme.LIGHT)
                    .title(isNew ? R.string.dialog_add_category_title : R.string.dialog_edit_category_title)
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
                nameEditText.setText(category.getName());
            }
        }

        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}