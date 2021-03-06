package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.CategoryListAdapter;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.interfaces.contracts.CategoryListContract;
import com.brandonhogan.liftscout.models.CategoryListModel;
import com.brandonhogan.liftscout.presenters.CategoryListPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import java.util.List;

import butterknife.BindView;

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

    // Binds
    //
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_data_label)
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
        //fab.setVisibility(presenter.isInSearch() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void searchViewOnQueryTextChange(String newText) {
        if (mAdapter != null)
            mAdapter.filterList(newText);
    }

    @Override
    public void onClick(View v, int position) {
        presenter.rowClicked(position);
    }

    @Override
    public void onLongClick(View v, int position) {
        itemSelectedDialog(position);
    }

    @Override
    public void onListUpdated(boolean isEmpty, boolean isClear) {
        setNoData(R.string.category_list_search_no_data, isEmpty);
    }

    // Private Functions
    //

    private void editCategory(int position) {
        getNavigationManager().startCategoryDetail(presenter.getCategory(position).getId());
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

    private void setNoData(int msgResId, boolean show) {
        noDataLabel.setVisibility(show ? View.VISIBLE : View.GONE);
        noDataLabel.setText(msgResId);
    }

    // Contracts
    //

    @Override
    public void updateAdapter(List<CategoryListModel> data) {
        setNoData(R.string.category_list_no_data, data == null || data.isEmpty());

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
}
