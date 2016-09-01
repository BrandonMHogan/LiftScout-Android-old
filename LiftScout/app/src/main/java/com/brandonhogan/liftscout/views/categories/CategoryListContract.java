package com.brandonhogan.liftscout.views.categories;

import java.util.List;

public interface CategoryListContract {

    interface View {
        void updateAdapter(List<CategoryListModel> data);
        void itemSelected(int id, boolean isAddSet);
    }

    interface Presenter {
        void viewCreated();
        void rowClicked(int position);
        void updateCategory(CategoryListModel categoryListModel);
        void createCategory(CategoryListModel categoryListModel);
        void deleteCategory(int position);
        CategoryListModel getCategory(int position);
    }

}
