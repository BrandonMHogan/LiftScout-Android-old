package com.brandonhogan.liftscout.views.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.TodayTransforms;
import com.brandonhogan.liftscout.core.interpolators.CubicAccelerateDecelerateInterpolator;
import com.brandonhogan.liftscout.core.interpolators.HesitateInterpolator;
import com.brandonhogan.liftscout.core.interpolators.JellyBounceInterpolator;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.home.today.TodayPageAdapter;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import butterknife.Bind;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements HomeContract.View {

    // Instance
    //
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    // Private Properties
    //
    private View rootView;
    private TodayPageAdapter adapter;
    private HomeContract.Presenter presenter;


    // Binds
    //
    @Bind(R.id.viewpager)
    HorizontalInfiniteCycleViewPager viewPager;

    @Bind(R.id.fabtoolbar)
    FABToolbarLayout toolbarLayout;

    @Bind(R.id.fabtoolbar_fab)
    FloatingActionButton fab;


    // Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new HomePresenter(this);
        presenter.viewCreated();

        setTitle(getString(R.string.app_name));

        setupPager();
        setupFab();
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbarLayout.hide(); // Hides because it bugs out if left open and returned sometimes
    }

    // Private Functions
    //
    private void setupPager() {

        adapter = new TodayPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.TOTAL_DAYS);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolbarLayout.hide();
            }

            @Override
            public void onPageSelected(int position) {
                updateTodayProgress();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        switch (presenter.getTransformValue()) {
            case TodayTransforms.OVERSHOOT:
                viewPager.setInterpolator(new OvershootInterpolator());
                viewPager.setScrollDuration(550);
                break;
            case TodayTransforms.FAST_OUT_LINEAR_IN:
                viewPager.setInterpolator(new FastOutLinearInInterpolator());
                viewPager.setScrollDuration(500);
                break;
            case TodayTransforms.BOUNCE:
                viewPager.setInterpolator(new BounceInterpolator());
                viewPager.setScrollDuration(900);
                break;
            case TodayTransforms.ACCELERATE_DECELERATE:
                viewPager.setInterpolator(new AccelerateDecelerateInterpolator());
                viewPager.setScrollDuration(550);
                break;
            default:
                viewPager.setInterpolator(new LinearInterpolator());
                break;
        }

        updateTodayProgress();
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarLayout.show();
            }
        });
    }

    private void updateTodayProgress() {
        presenter.updateTodayProgress(adapter.dateByPosition(viewPager.getCurrentItem()));
    }

    @OnClick(R.id.routine_add)
    public void addSetOnClick() {
        getNavigationManager().startCategoryListAddSet();
    }

    @OnClick(R.id.exercise_add)
    public void addExerciseOnClick() {
        getNavigationManager().startCategoryListAddSet();
    }

}
