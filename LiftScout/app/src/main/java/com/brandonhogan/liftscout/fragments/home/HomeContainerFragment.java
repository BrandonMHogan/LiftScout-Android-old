package com.brandonhogan.liftscout.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.core.constants.TodayTransforms;
import com.brandonhogan.liftscout.core.controls.WeightDialog;
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.User;
import com.brandonhogan.liftscout.core.model.UserSetting;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Sort;

public class HomeContainerFragment extends BaseFragment {


    // Instance
    //
    public static HomeContainerFragment newInstance() {
        return new HomeContainerFragment();
    }


    // Private Properties
    //
    private View rootView;
    private TodayPageAdapter adapter;

    // Do not call directly. Use Helper functions
    private User _user;
    private UserSetting _todayTransformUserSetting;


    // Binds
    //
    @Bind(R.id.viewpager)
    ViewPager viewPager;

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
        setTitle(getString(R.string.app_name));

        setupPager();
        setupFab();
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbarLayout.hide(); // Hides because it bugs out if left open and returned sometimes
        clearTodayTransform();
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

        switch (getTodayTransform().getValue()) {
            case TodayTransforms.ACCORDION:
                viewPager.setPageTransformer(true, new AccordionTransformer());
                break;
            case TodayTransforms.DEPTH_PAGE:
                viewPager.setPageTransformer(true, new DepthPageTransformer());
                break;
            case TodayTransforms.FOREGROUND_TO_BACKGROUND:
                viewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
                break;
            case TodayTransforms.ROTATE_DOWN:
                viewPager.setPageTransformer(true, new RotateDownTransformer());
                break;
            case TodayTransforms.ROTATE_UP:
                viewPager.setPageTransformer(true, new RotateUpTransformer());
                break;
            case TodayTransforms.SCALE_IN_OUT:
                viewPager.setPageTransformer(true, new ScaleInOutTransformer());
                break;
            case TodayTransforms.STACK:
                viewPager.setPageTransformer(true, new StackTransformer());
                break;
            case TodayTransforms.ZOOM_IN:
                viewPager.setPageTransformer(true, new ZoomInTransformer());
                break;
            case TodayTransforms.ZOOM_OUT:
                viewPager.setPageTransformer(true, new ZoomOutTranformer());
                break;
            case TodayTransforms.ZOOM_OUT_SLIDE:
                viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                break;
            default:
                viewPager.setPageTransformer(true, new DefaultTransformer());
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

    private User getUser() {

        if (_user != null)
            return _user;

        _user = getRealm().where(User.class).findFirst();

        return _user;
    }

    private void updateUser() {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(_user);
        getRealm().commitTransaction();
    }

    private void updateUserWeight(double weight) {
        getRealm().beginTransaction();
        getUser().setWeight(weight);
        getRealm().copyToRealmOrUpdate(_user);
        getRealm().commitTransaction();
    }

    private void clearTodayTransform() {
        _todayTransformUserSetting = null;
    }

    private UserSetting getTodayTransform() {

        if (_todayTransformUserSetting != null)
            return _todayTransformUserSetting;

        _todayTransformUserSetting = getRealm().where(UserSetting.class)
                .equalTo(UserSetting.NAME, UserSetting.TODAY_TRANSFORM).findFirst();

        if (_todayTransformUserSetting == null) {
            _todayTransformUserSetting = new UserSetting();
            _todayTransformUserSetting.setName(UserSetting.TODAY_TRANSFORM);
            _todayTransformUserSetting.setValue(TodayTransforms.DEFAULT);

            getRealm().beginTransaction();
            getRealm().copyToRealmOrUpdate(_todayTransformUserSetting);
            getRealm().commitTransaction();
        }
        return _todayTransformUserSetting;
    }

    private void updateTodayProgressWeight(double weight) {
        getRealm().beginTransaction();
        getProgressManager().getTodayProgress().setWeight(weight);
        getRealm().copyToRealmOrUpdate(getProgressManager().getTodayProgress());
        getRealm().commitTransaction();

        Progress topDate = getRealm().where(Progress.class)
                .greaterThanOrEqualTo(Progress.DATE, getProgressManager().getTodayProgress().getDate())
                .findAllSorted(Progress.DATE, Sort.DESCENDING).first();

        if (topDate != null && getProgressManager().getTodayProgress().getDate().compareTo(topDate.getDate()) >= 0 ) {
            Log.d(getTAG(), "Current Date >= Top Progress Date. Will update user weight to " + weight);
            updateUserWeight(weight);
        }


        Bundle params = new Bundle();
        params.putString("date", getProgressManager().getTodayProgress().getDate().toString());
        params.putDouble("weight", weight);
        ((MainActivity)getActivity()).getFirebaseAnalytics().logEvent("weight_set", params);
    }

    private void updateTodayProgress() {
        getProgressManager().setTodayProgress(adapter.dateByPosition(viewPager.getCurrentItem()));
    }

    @OnClick(R.id.set)
    public void addSetOnClick() {
        getNavigationManager().startCategoryListAddSet();
    }

    @OnClick(R.id.weight)
    public void addWeightOnClick() {

        double weight;
        if (getProgressManager().getTodayProgress().getWeight() == 0)
            weight = getUser().getWeight();
        else
            weight = getProgressManager().getTodayProgress().getWeight();

        WeightDialog dialog = new WeightDialog(getActivity(), new WeightDialog.WeightDialogListener() {
            @Override
            public void onCancelWeightDialog() {

            }

            @Override
            public void onSaveWeightDialog(double weight) {
                updateTodayProgressWeight(weight);
                adapter.update();

            }
        }, weight, true);

        dialog.show();
    }
}
