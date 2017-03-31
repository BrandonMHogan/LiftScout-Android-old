package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.repository.model.Category;

import java.util.List;

import io.reactivex.Observable;

public interface CategoryRepo {
    Category getCategory(int categoryId);
    List<Category> getCategories();
    Category setCategory(Category category);
    void deleteCategory(int categoryId);
    Observable<Boolean> deleteAllCategories();
}
