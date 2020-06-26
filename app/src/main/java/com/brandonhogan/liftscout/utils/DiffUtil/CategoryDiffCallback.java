package com.brandonhogan.liftscout.utils.DiffUtil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.brandonhogan.liftscout.models.CategoryListModel;

import java.util.List;

/**
 * Created by Brandon on 4/2/2017.
 * Description :
 */

public class CategoryDiffCallback extends DiffUtil.Callback{

    private List<CategoryListModel> oldList;
    private List<CategoryListModel> newList;

    public CategoryDiffCallback(List<CategoryListModel> oldList, List<CategoryListModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}