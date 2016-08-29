package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Category;

public interface CategoryRepo {
    Category getCategory(int categoryId);
    void setCategory(Category category);
}
