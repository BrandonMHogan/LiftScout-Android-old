package com.brandonhogan.liftscout.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import butterknife.Bind;

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


    // Binds
    //
    @Bind(R.id.viewpager)
    InfiniteViewPager viewPager;

//    @Bind(R.id.bottom_sheet)
//    BottomSheetLayout bottomSheetLayout;
//
//    @Bind(R.id.fab)
//    FloatingActionButton fab;
//
//    @Bind(R.id.list_menu)
//    ListView listView;

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

    private String getTransformType() {
        return "blaw";
    }

    private void setupPager() {

        if (adapter != null) {
            return;
        }

        adapter = new TodayPageAdapter(getChildFragmentManager());

        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);
        viewPager.setAdapter(wrappedAdapter);

        viewPager.setPageTransformer(true, new ScaleInOutTransformer());

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

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarLayout.show();
            }
        });
    }
}
