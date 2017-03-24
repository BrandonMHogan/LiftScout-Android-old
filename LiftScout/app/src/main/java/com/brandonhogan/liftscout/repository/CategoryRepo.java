package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.repository.model.Category;

import io.reactivex.Observable;
import io.realm.RealmResults;

public interface CategoryRepo {
    Category getCategory(int categoryId);
    RealmResults<Category> getCategories();
    Category setCategory(Category category);
    void deleteCategory(int categoryId);
    Observable<Boolean> deleteAllCategories();
}
