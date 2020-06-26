package com.brandonhogan.liftscout.views;

import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.IntroThemesContract;
import com.brandonhogan.liftscout.utils.AttrUtil;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.dreierf.materialintroscreen.SlideFragment;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroThemesFragment extends SlideFragment implements IntroThemesContract.View {

    // Injections
    //
    @Inject
    IntroThemesContract.Presenter presenter;

    // Bindings
    //
    @BindView(R.id.theme_spinner)
    MaterialSpinner themeSpinner;

    @BindView(R.id.primary_color)
    View primaryColor;

    @BindView(R.id.primary_dark_color)
    View primaryDarkColor;

    @BindView(R.id.accent_color)
    View accentColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_intro_themes, container, false);
        Injector.getFragmentComponent().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.setView(this);

        themeSpinner.setBackgroundResource(AttrUtil.getAttributeRes(getActivity().getTheme(), R.attr.colorFill));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public int backgroundColor() {
        return R.color.light_theme_background;
    }

    @Override
    public int buttonsColor() {
        return R.color.theme_original_accent;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }


    @Override
    public void populateThemes(ArrayList<String> screens, int position) {
        themeSpinner.setItems(screens);
        themeSpinner.setSelectedIndex(position);

        themeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onThemeSelected(position);
            }
        });

        presenter.onThemeSelected(position);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void themeSelected(int theme) {
        primaryColor.setBackgroundColor(getResources().getColor(setColor(theme, R.attr.colorPrimary)));
        primaryDarkColor.setBackgroundColor(getResources().getColor(setColor(theme, R.attr.colorPrimaryDark)));
        accentColor.setBackgroundColor(getResources().getColor(setColor(theme, R.attr.colorAccent)));
    }

    private int setColor(int theme, int attr) {
        TypedArray primaryType = getActivity().getTheme().obtainStyledAttributes(theme, new int[] {attr});
        int color = primaryType.getResourceId(0, 0);
        primaryType.recycle();
        return color;
    }
}
