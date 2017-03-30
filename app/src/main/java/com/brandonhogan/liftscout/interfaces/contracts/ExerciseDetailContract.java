package com.brandonhogan.liftscout.interfaces.contracts;

import com.brandonhogan.liftscout.repository.model.Category;
import com.brandonhogan.liftscout.repository.model.Exercise;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/27/2017.
 * Description :
 */

public interface ExerciseDetailContract {
    interface View {
        void setTitle(String name);
        void setupControlValues(Exercise exercise);
        void setupControlCategory(Category category);
        void setFav(boolean isSet);
        void onSaveSuccess();
        void onSaveFailure(int errorMsg);
    }

    interface Presenter {
        void viewCreated();
        double getDefaultIncrement();
        ArrayList<String> getCategories();
        void onSave(int categoryPosition, String name, int increment, int restTimer, boolean isAuto, boolean isSound, boolean isVibrate);
        void onFavClicked();
    }
}
