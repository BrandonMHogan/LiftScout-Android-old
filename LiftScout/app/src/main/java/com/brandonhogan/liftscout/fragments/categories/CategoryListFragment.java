package com.brandonhogan.liftscout.fragments.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Themes;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.UserSetting;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

public class CategoryListFragment extends BaseFragment implements RecyclerTouchListener.RecyclerTouchListenerHelper {


    // Instance
    //
    public static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }


    // Private Properties
    //
    private View rootView;
    private CategoryListAdapter mAdapter;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    private SweetAlertDialog dialog;

    private List<CategoryListModel> _categories;

    // Binds
    //
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_category_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getResources().getString(R.string.title_frag_category_list));

        setupAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    @OnClick(R.id.fab)
    public void addOnClick() {
        addCategory();
    }

    // Private Functions
    //
    private void setupAdapter() {
        mAdapter = new CategoryListAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);

        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        getNavigationManager().startExerciseList(getData().get(position).getId());
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                    }
                })
//                .setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
//                    @Override
//                    public void onRowLongClicked(int position) {
//                    }
//                })
                .setSwipeOptionViews(R.id.delete, R.id.edit)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {

                        if (viewID == R.id.delete) {
                            removeCategoryAlert(position);
                        } else if (viewID == R.id.edit) {
                            editCategory(position);
                        }
                    }
                });
    }

    private List<CategoryListModel> getData() {

        if (_categories != null)
            return _categories;

        _categories = new ArrayList<>();


        for (Category category : getCategories().sort(Category.NAME)) {
            _categories.add(new CategoryListModel(category));
        }

        return _categories;
    }

    private void update() {
        _categories = null;
        mAdapter.setList(getData());
    }

    private RealmResults<Category> getCategories() {
        return getRealm().where(Category.class).findAll();
    }

    private void saveCategory(Category category) {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(category);
        getRealm().commitTransaction();

        update();
    }

    private void removeCategory(int id) {
        getRealm().beginTransaction();
        getRealm().where(Category.class).equalTo(Category.ID, id).findFirst().deleteFromRealm();
        getRealm().commitTransaction();

        update();
    }

    private UserSetting getDisplayTheme() {
        return getRealm().where(UserSetting.class)
                .equalTo(UserSetting.NAME, UserSetting.THEME).findFirst();
    }

    private void editCategory(int position) {
        boolean isDarkTheme = false;

        if (getDisplayTheme().getValue().equals(Themes.DARK))
            isDarkTheme = true;


        CategoryEditDialog dialog = new CategoryEditDialog(getActivity(), new CategoryEditDialog.CategoryEditDialogListener() {
            @Override
            public void onCancelCategoryEditDialog() {
            }

            @Override
            public void onSaveCategoryEditDialog(CategoryListModel category) {

                Category newCategory = new Category();
                newCategory.setName(category.getName());
                newCategory.setColor(category.getColor());
                newCategory.setId(category.getId());

                saveCategory(newCategory);
            }
        }, isDarkTheme, getData().get(position));

        dialog.show();
    }

    private void addCategory() {
        boolean isDarkTheme = false;

        if (getDisplayTheme().getValue().equals(Themes.DARK))
            isDarkTheme = true;


        CategoryEditDialog dialog = new CategoryEditDialog(getActivity(), new CategoryEditDialog.CategoryEditDialogListener() {
            @Override
            public void onCancelCategoryEditDialog() {

            }

            @Override
            public void onSaveCategoryEditDialog(CategoryListModel category) {
                Category newCategory = new Category();
                newCategory.setName(category.getName());
                newCategory.setColor(category.getColor());
                saveCategory(newCategory);
            }
        }, isDarkTheme, null);

        dialog.show();
    }


    private void removeCategoryAlert(final int position) {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_category_remove_title))
                .setContentText(getString(R.string.dialog_category_remove_message))
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        removeCategory(getData().get(position).getId());
                        dialog.cancel();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }
}
