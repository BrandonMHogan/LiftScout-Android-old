package com.brandonhogan.liftscout.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;

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

        if (adapter != null) {
            return;
        }

        adapter = new TodayPageAdapter(getChildFragmentManager());

        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);
        viewPager.setAdapter(wrappedAdapter);
        viewPager.setPageTransformer(true, new ScaleInOutTransformer());
    }
}
