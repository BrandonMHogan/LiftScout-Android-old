package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.contracts.CategoryDetailContract;
import com.brandonhogan.liftscout.presenters.CategoryDetailPresenter;
import com.brandonhogan.liftscout.repository.model.Category;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.thebluealliance.spectrum.SpectrumPalette;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brandon on 3/30/2017.
 * Description :
 */

public class CategoryDetailFragment extends BaseFragment implements CategoryDetailContract.View,
        SpectrumPalette.OnColorSelectedListener{


    // Private Static Properties
    //
    private final static String BUNDLE_CATEGORY_ID = "categoryIdBundle";
    private final static String BUNDLE_NEW_CATEGORY = "newCategoryBundle";


    // Instance
    //
    public static CategoryDetailFragment newInstance() {
        Bundle args = new Bundle();

        args.putBoolean(BUNDLE_NEW_CATEGORY, true);
        args.putInt(BUNDLE_CATEGORY_ID, 0);

        CategoryDetailFragment fragment = new CategoryDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static CategoryDetailFragment newInstance(int categoryId) {
        Bundle args = new Bundle();

        args.putBoolean(BUNDLE_NEW_CATEGORY, false);
        args.putInt(BUNDLE_CATEGORY_ID, categoryId);

        CategoryDetailFragment fragment = new CategoryDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    // Private Properties
    //
    private View rootView;
    private CategoryDetailContract.Presenter presenter;
    private Toast toast;

    @Bind(R.id.name_text)
    EditText nameText;

    @Bind(R.id.palette)
    SpectrumPalette palette;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_category_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        toast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);

        presenter = new CategoryDetailPresenter(this,
                this.getArguments().getInt(BUNDLE_CATEGORY_ID),
                this.getArguments().getBoolean(BUNDLE_NEW_CATEGORY));

        setTitle(getString(R.string.category_new));
        setupControls();

        presenter.viewCreated();
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
        menu.findItem(R.id.action_save).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                presenter.onSave(nameText.getText().toString());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupControls() {
        int[] colors = getActivity().getResources().getIntArray(R.array.category_colors);
        palette.setColors(colors);
        palette.setOnColorSelectedListener(this);
    }

    @Override
    public void setupControls(Category category) {
        nameText.setText(category.getName());
        palette.setSelectedColor(category.getColor());
    }


    @Override
    public void onSaveSuccess() {
        toast.setText(R.string.exercise_setting_saved);
        toast.show();
        getNavigationManager().navigateBack(getActivity());
    }

    @Override
    public void onSaveFailure(int errorMsg) {
        toast.setText(errorMsg);
        toast.show();
    }

    @Override
    public void onColorSelected(@ColorInt int color) {
        presenter.onColorSelected(color);
    }
}
