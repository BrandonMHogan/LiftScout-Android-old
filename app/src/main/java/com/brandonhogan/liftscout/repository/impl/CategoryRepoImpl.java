package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.CategoryRepo;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.model.Category;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class CategoryRepoImpl implements CategoryRepo {

    private static final String TAG = "CategoryRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public CategoryRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    private int getNextKey() {
        Number max = databaseRealm.getRealmInstance().where(Category.class).max(Category.ID);
        return (max != null) ? max.intValue() + 1 : 1;
    }

    @Override
    public Category getCategory(int categoryId) {
        return databaseRealm.getRealmInstance()
                .where(Category.class)
                .equalTo(Category.ID, categoryId)
                .findFirst();
    }

    @Override
    public List<Category> getCategories() {
        return databaseRealm.getRealmInstance()
                .where(Category.class)
                .findAll().sort(Category.NAME);
    }

    @Override
    public Category setCategory(Category category) {
        try {
            if (category.getId() == 0)
                category.setId(getNextKey());

            databaseRealm.getRealmInstance().beginTransaction();
            databaseRealm.getRealmInstance().copyToRealmOrUpdate(category);
            databaseRealm.getRealmInstance().commitTransaction();

            return databaseRealm.getRealmInstance()
                    .where(Category.class)
                    .equalTo(Category.ID, category.getId())
                    .findFirst();
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            databaseRealm.getRealmInstance().cancelTransaction();
            return null;
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

    @Override
    public Observable<Boolean> deleteAllCategories() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {
                    databaseRealm.getRealmInstance().beginTransaction();

                    databaseRealm.getRealmInstance()
                            .where(Category.class)
                            .findAll().deleteAllFromRealm();

                    databaseRealm.getRealmInstance().commitTransaction();

                    e.onNext(true);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                    databaseRealm.getRealmInstance().cancelTransaction();
                    e.onError(ex);
                }
            }
        });
    }
}
