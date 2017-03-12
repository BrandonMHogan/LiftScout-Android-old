package com.brandonhogan.liftscout.views.Intro.themes;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.utils.AttrUtil;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class IntroThemeSlideFragment  extends SlideFragment implements IntroThemesSlideContract.View {


    // Bindings
    //
    @Bind(R.id.theme_spinner)
    MaterialSpinner themeSpinner;

    @Bind(R.id.primary_color)
    View primaryColor;

    @Bind(R.id.primary_dark_color)
    View primaryDarkColor;

    @Bind(R.id.accent_color)
    View accentColor;


    // Private Properties
    //
    private IntroThemesSlideContract.Presenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_intro_themes, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        themeSpinner.setBackgroundResource(AttrUtil.getAttributeRes(getActivity().getTheme(), android.R.attr.fillColor));

        presenter = new IntroThemeSlidePresenter(this);
        presenter.viewCreated();
    }

    @Override
    public int backgroundColor() {
        return R.color.intro_slide_four;
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

//    @Override
//    public void themeSelected(int primary, int dark, int accent, int background) {
//        primaryColor.setBackgroundColor(primary);
//    }


    @Override
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
