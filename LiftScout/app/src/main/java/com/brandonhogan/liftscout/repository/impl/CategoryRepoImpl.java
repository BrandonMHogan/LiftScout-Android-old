package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.DatabaseRealm;

import javax.inject.Inject;

import io.realm.RealmResults;

public class CategoryRepoImpl implements CategoryRepo {

    private static final String TAG = "CategoryRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public CategoryRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    private int getNextKey() {
        Number max = databaseRealm.getRealmInstance().where(Category.class).max(Category.ID);
        return (max != null) ? max.intValue() + 1 : 0;
    }

    @Override
    public Category getCategory(int categoryId) {
        return databaseRealm.getRealmInstance()
                .where(Category.class)
                .equalTo(Category.ID, categoryId)
                .findFirst();
    }

    @Override
    public RealmResults<Category> getCategories() {
        return databaseRealm.getRealmInstance()
                .where(Category.class)
                .findAll();
    }

    @Override
    public void setCategory(Category category) {
        try {
            if (category.getId() == 0)
                category.setId(getNextKey());

            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(category);
            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        try {
            databaseRealm.getRealmInstance().beginTransaction();

            databaseRealm.getRealmInstance()
                    .where(Category.class)
                    .equalTo(Category.ID, categoryId)
                    .findFirst()
                    .deleteFromRealm();

            databaseRealm.getRealmInstance().commitTransaction();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
        }
    }
}
