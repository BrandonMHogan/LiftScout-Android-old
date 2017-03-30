package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.CategoryDetailContract;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.model.Category;

import javax.inject.Inject;

/**
 * Created by Brandon on 3/30/2017.
 * Description :
 */

public class CategoryDetailPresenter implements CategoryDetailContract.Presenter {

    @Inject
    CategoryRepo categoryRepo;


    // Private Properties
    //
    private CategoryDetailContract.View view;
    private Category category;
    private boolean isNew;
    private int selectedColor;



    // Constructor
    //
    public CategoryDetailPresenter(CategoryDetailContract.View view, int categoryId, boolean isNew) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.isNew = isNew;

         if(!isNew)
            category = categoryRepo.getCategory(categoryId);
    }

    public boolean validation(Category newCategory) {
        if (newCategory.getName().isEmpty())
            view.onSaveFailure(R.string.category_edit_no_name);
        else if (newCategory.getColor() == 0)
            view.onSaveFailure(R.string.category_edit_no_color);
        else
            return true;

        return false;
    }

    @Override
    public void viewCreated() {
        if (!isNew) {
            view.setTitle(category.getName());
            view.setupControls(category);
        }
    }

    @Override
    public void onSave(String name) {
        Category newCategory = new Category();

        if(!isNew)
            newCategory.setId(category.getId());

        newCategory.setName(name.trim());
        newCategory.setColor(selectedColor);

        if (validation(newCategory)) {
            categoryRepo.setCategory(newCategory);
            view.onSaveSuccess();
        }
    }

    @Override
    public void onColorSelected(int color) {
        selectedColor = color;
    }
}
