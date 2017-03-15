package com.brandonhogan.liftscout.views.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.TodayTransforms;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.home.today.TodayPageAdapter;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.Date;

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
        rootView = inflater.inflate(R.layout.frag_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new HomePresenter(this);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(true);
        menu.findItem(R.id.action_about).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                getNavigationManager().startSettings();
                return true;
            case R.id.action_about:
                getNavigationManager().startAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
