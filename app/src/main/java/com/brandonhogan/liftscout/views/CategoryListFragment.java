package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.CategoryListAdapter;
import com.brandonhogan.liftscout.dialogs.CategoryEditDialog;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.interfaces.contracts.CategoryListContract;
import com.brandonhogan.liftscout.models.CategoryListModel;
import com.brandonhogan.liftscout.presenters.CategoryListPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CategoryListFragment extends BaseFragment implements
        CategoryListContract.View, RecyclerViewClickListener {


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
    private MaterialDialog dialog;
    private CategoryEditDialog editDialog;

    // Binds
    //
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.no_data_label)
    TextView noDataLabel;

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
        fab.setVisibility(presenter.isInSearch() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(false);
    }

    @Override
    public void onClick(View v, int position) {
        presenter.rowClicked(position);
    }

    @Override
    public void onLongClick(View v, int position) {
        itemSelectedDialog(position);
    }

    // Private Functions
    //

    private void editCategory(int position) {
        fab.setEnabled(false);
        editDialog = new CategoryEditDialog(getActivity(), new CategoryEditDialog.CategoryEditDialogListener() {
            @Override
            public void onCancelCategoryEditDialog() {
                fab.setEnabled(true);
            }

            @Override
            public void onSaveCategoryEditDialog(CategoryListModel category) {
                fab.setEnabled(true);
                presenter.updateCategory(category);
            }
        }, true, presenter.getCategory(position));


        editDialog.show();
    }

    private void createCategory() {
        fab.setEnabled(false);
        editDialog = new CategoryEditDialog(getActivity(), new CategoryEditDialog.CategoryEditDialogListener() {
            @Override
            public void onCancelCategoryEditDialog() {
                fab.setEnabled(true);
            }

            @Override
            public void onSaveCategoryEditDialog(CategoryListModel category) {
                fab.setEnabled(true);
                presenter.createCategory(category);
            }
        }, true, null);

        editDialog.show();
    }


    private void itemSelectedDialog(final int position) {
        CategoryListModel model = presenter.getCategory(position);

        dialog = new MaterialDialog.Builder(getActivity())
                .title(model.getName())
                .content(getString(R.string.dialog_category_long_selected, model.getName()))
                .positiveText(R.string.edit)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        editCategory(position);
                        dialog.cancel();
                    }
                })
                .neutralText(R.string.delete)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteCategory(position);
                        dialog.cancel();
                    }
                })
                .build();

        dialog.show();
    }


    // Contracts
    //

    @Override
    public void updateAdapter(List<CategoryListModel> data) {

        noDataLabel.setVisibility((data == null || data.isEmpty()) ? View.VISIBLE : View.GONE);

        mAdapter = new CategoryListAdapter(getActivity(), data, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void itemSelected(int id, boolean isAddSet) {
        if (isAddSet)
            getNavigationManager().startExerciseListAddSet(id);
        else
            getNavigationManager().startExerciseList(id);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        createCategory();
    }
}
