package com.brandonhogan.liftscout.presenters;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.CategoryListContract;
import com.brandonhogan.liftscout.managers.ExerciseManager;
import com.brandonhogan.liftscout.managers.GraphManager;
import com.brandonhogan.liftscout.models.CategoryListModel;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.impl.CategoryRepoImpl;
import com.brandonhogan.liftscout.repository.model.Category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CategoryListPresenter implements CategoryListContract.Presenter {


    @Inject
    GraphManager graphManager;

    @Inject
    ExerciseManager exerciseManager;

    // Private Properties
    //
    private CategoryListContract.View view;
    private boolean isAddSet;
    private ArrayList<CategoryListModel> adapterData;
    CategoryRepo categoryRepo;


    // Constructor
    //
    public CategoryListPresenter(CategoryListContract.View view, boolean isAddSet) {
        Injector.getAppComponent().inject(this);
        this.view = view;
        this.isAddSet = isAddSet;

        categoryRepo = new CategoryRepoImpl();
    }


    // Private Functions
    //
    private void updateAdapter() {
        if (adapterData != null)
            view.updateAdapter(adapterData);

        adapterData = new ArrayList<>();

        List<Category> categories = categoryRepo.getCategories();

        if (categories == null)
            view.updateAdapter(adapterData);


        for (Category category : categories) {
            adapterData.add(new CategoryListModel(category));
        }

        view.updateAdapter(adapterData);
    }


    // Contracts
    //
    @Override
    public void viewCreated() {
        updateAdapter();
    }

    @Override
    public boolean isInSearch() {
        return graphManager.isInSearch();
    }

    @Override
    public void rowClicked(int position) {
        view.itemSelected(adapterData.get(position).getId(), isAddSet);
    }

    @Override
    public void createCategory(CategoryListModel categoryListModel) {
        Category newCategory = new Category();
        newCategory.setName(categoryListModel.getName());
        newCategory.setColor(categoryListModel.getColor());

        categoryRepo.setCategory(newCategory);
        updateAdapter();
    }

    @Override
    public void updateCategory(CategoryListModel categoryListModel) {
        Category newCategory = new Category();
        newCategory.setName(categoryListModel.getName());
        newCategory.setColor(categoryListModel.getColor());
        newCategory.setId(categoryListModel.getId());

        categoryRepo.setCategory(newCategory);
        updateAdapter();
    }

    @Override
    public void deleteCategory(int position) {
        categoryRepo.deleteCategory(adapterData.get(position).getId());

        updateAdapter();
        exerciseManager.categoryUpdated(true);
    }

    @Override
    public CategoryListModel getCategory(int position) {
        return adapterData.get(position);
    }
}
