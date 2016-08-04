package com.brandonhogan.liftscout.fragments.home;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.constants.TodayTransforms;
import com.brandonhogan.liftscout.foundation.controls.WeightDialog;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.foundation.model.UserSetting;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import butterknife.Bind;
import butterknife.OnClick;

public class HomeContainerFragment extends BaseFragment {


    // Instance
    //
    public static HomeContainerFragment newInstance() {
        return new HomeContainerFragment();
    }


    // Private Properties
    //
    private User user;
    private View rootView;
    private TodayPageAdapter adapter;
    private UserSetting _todayTransformUserSetting;
    private String currentTransform;


    // Binds
    //
    @Bind(R.id.viewpager)
    InfiniteViewPager viewPager;

    @Bind(R.id.fabtoolbar)
    FABToolbarLayout toolbarLayout;

    @Bind(R.id.fabtoolbar_fab)
    FloatingActionButton fab;


    // Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null)
            rootView = inflater.inflate(R.layout.frag_home, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Home Sweet Home");

        loadUserData();
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
    private void loadUserData() {
        user = getRealm().where(User.class).findFirst();

        if(user.isFirstLoad()) {
            getRealm().beginTransaction();
            user.setFirstLoad(false);
            getRealm().commitTransaction();

          //  welcomeMessage.setText(String.format(getContext().getString(R.string.frag_home_first_load_message), user.getName()));
        }
        else {
        //    welcomeMessage.setText(String.format(getContext().getString(R.string.frag_home_welcome_back_message),user.getName()));
        }
    }

    private void setupPager() {

        if (adapter == null) {

            adapter = new TodayPageAdapter(getChildFragmentManager());

            PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);
            viewPager.setAdapter(wrappedAdapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    toolbarLayout.hide();
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

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
        }
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarLayout.show();
            }
        });
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

    @OnClick(R.id.weight)
    public void addWeightOnClick() {
        Log.e(getTAG(), "Weight on click clicked!");

        WeightDialog dialog = new WeightDialog(getActivity(), new WeightDialog.WeightDialogListener() {
            @Override
            public void onCancelWeightDialog() {

            }

            @Override
            public void onSaveWeightDialog(double weight) {

            }
        }, 200, true);

        dialog.show();
    }
}
