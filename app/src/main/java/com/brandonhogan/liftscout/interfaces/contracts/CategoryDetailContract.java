package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.repository.model.Category;

/**
 * Created by Brandon on 3/30/2017.
 * Description :
 */

public interface CategoryDetailContract {
    interface View {
        void setTitle(String name);
        void setupControls(Category category);
        void onSaveSuccess();
        void onSaveFailure(int errorMsg);
    }

    interface Presenter {
        void viewCreated();
        void onSave(String name);
        void onColorSelected(int color);
    }
}
