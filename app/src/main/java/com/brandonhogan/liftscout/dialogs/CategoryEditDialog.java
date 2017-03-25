package com.brandonhogan.liftscout.dialogs;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.models.CategoryListModel;
import com.thebluealliance.spectrum.SpectrumPalette;

public class CategoryEditDialog implements SpectrumPalette.OnColorSelectedListener {

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
    private SpectrumPalette palette;


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

        String name = nameEditText.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(activity, R.string.category_edit_no_name, Toast.LENGTH_SHORT).show();
            return ;
        }

        if (category.getColor() == 0) {
            Toast.makeText(activity, R.string.category_edit_no_color, Toast.LENGTH_SHORT).show();
            return ;
        }


        dismiss();

        category.setName(name);
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

            View customTheme = LayoutInflater.from(activity).inflate(R.layout.dialog_category_edit, null);

            dialog = new MaterialDialog.Builder(activity)
                    .title(isNew ? R.string.dialog_add_category_title : R.string.dialog_edit_category_title)
                    .customView(customTheme, true)
                    .positiveText(R.string.save)
                    .autoDismiss(false)
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
            palette = (SpectrumPalette) dialog.getCustomView().findViewById(R.id.palette);


            int[] colors = activity.getResources().getIntArray(R.array.category_colors);
            palette.setColors(colors);
            palette.setOnColorSelectedListener(this);

            if (!isNew) {
                nameEditText.setText(category.getName());
                palette.setSelectedColor(category.getColor());
            }
        }

        dialog.show();
        nameEditText.requestFocus();
    }

    private void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void onColorSelected(@ColorInt int color) {
        category.setColor(color);
    }
}