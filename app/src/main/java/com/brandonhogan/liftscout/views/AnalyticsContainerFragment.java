package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.adapters.AnalyticsContainerAdapter;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class AnalyticsContainerFragment extends BaseFragment {


    // Instance
    //
    public static AnalyticsContainerFragment newInstance()
    {
        AnalyticsContainerFragment frag = new AnalyticsContainerFragment();
        return frag;
    }


    // Private Properties
    //
    private View rootView;


    // Binds
    //
    @Bind(R.id.analytics_viewpager)
    ViewPager viewPager;

    @Bind(R.id.analytics_tab_layout)
    TabLayout tabLayout;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_analytics_container, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.nav_graphs));

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.graphs_categories)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.graphs_exercises)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        final AnalyticsContainerAdapter adapter = new AnalyticsContainerAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
