package com.brandonhogan.liftscout.views.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoryListFragment extends BaseFragment implements
        CategoryListContract.View,
        RecyclerTouchListener.RecyclerTouchListenerHelper {


    // Private Static Properties
    //
    private final static String BUNDLE_ADD_SET = "addSetBundle";


    // Instance
    //
    public static CategoryListFragment newInstance(boolean addSet) {

        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_ADD_SET, addSet);

        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(args);

        return fragment;
    }


    // Private Properties
    //
    private View rootView;
    private CategoryListContract.Presenter presenter;
    private CategoryListAdapter mAdapter;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    private SweetAlertDialog dialog;


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
        presenter = new CategoryListPresenter(this,
                getArguments().getBoolean(BUNDLE_ADD_SET));

        presenter.viewCreated();

        setTitle(getResources().getString(R.string.title_frag_category_list));
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
        createCategory();
    }


    // Private Functions
    //
    private void editCategory(int position) {
        CategoryEditDialog dialog = new CategoryEditDialog(getActivity(), new CategoryEditDialog.CategoryEditDialogListener() {
            @Override
            public void onCancelCategoryEditDialog() {
            }

            @Override
            public void onSaveCategoryEditDialog(CategoryListModel category) {
                presenter.updateCategory(category);
            }
        }, true, presenter.getCategory(position));

        dialog.show();
    }

    private void createCategory() {
        CategoryEditDialog dialog = new CategoryEditDialog(getActivity(), new CategoryEditDialog.CategoryEditDialogListener() {
            @Override
            public void onCancelCategoryEditDialog() {

            }

            @Override
            public void onSaveCategoryEditDialog(CategoryListModel category) {
                presenter.createCategory(category);
            }
        }, true, null);

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
                        presenter.deleteCategory(position);
                        dialog.cancel();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }


    // Contracts
    //

    @Override
    public void updateAdapter(List<CategoryListModel> data) {
        mAdapter = new CategoryListAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);

        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        presenter.rowClicked(position);
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

    @Override
    public void itemSelected(int id, boolean isAddSet) {
        if (isAddSet)
            getNavigationManager().startExerciseListAddSet(id);
        else
            getNavigationManager().startExerciseList(id);
    }
}
