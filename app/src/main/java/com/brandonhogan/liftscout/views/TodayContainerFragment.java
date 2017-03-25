package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.contracts.TodayContainerContract;
import com.brandonhogan.liftscout.utils.constants.TodayTransforms;
import com.brandonhogan.liftscout.presenters.TodayContainerPresenter;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.adapters.TodayPageAdapter;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class TodayContainerFragment extends BaseFragment implements TodayContainerContract.View {

    // Instance
    //
    public static TodayContainerFragment newInstance() {
        return new TodayContainerFragment();
    }


    // Private Properties
    //
    private View rootView;
    private TodayPageAdapter adapter;
    private TodayContainerContract.Presenter presenter;
    private ViewPager.OnPageChangeListener onPageChangeListener;


    // Binds
    //
    @Bind(R.id.viewpager)
    HorizontalInfiniteCycleViewPager viewPager;


    // Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_today_container, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new TodayContainerPresenter(this);
        presenter.viewCreated();
        presenter.updateTodayProgress(new Date());

        setTitle(getString(R.string.app_name));

        setupPager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.addOnPageChangeListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    // Private Functions
    //
    private void setupPager() {

        adapter = new TodayPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.TOTAL_DAYS);


        if(onPageChangeListener == null) {
            onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    updateTodayProgress();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

            viewPager.setOnPageChangeListener(onPageChangeListener);
        }

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
    }


    private void updateTodayProgress() {
        presenter.updateTodayProgress(adapter.dateByPosition(viewPager.getCurrentItem()));
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        getNavigationManager().startCategoryListAddSet();
    }
}
