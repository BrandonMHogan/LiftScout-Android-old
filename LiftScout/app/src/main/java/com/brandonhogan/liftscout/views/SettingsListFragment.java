package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.OnClick;

public class SettingsListFragment extends BaseFragment {

    // Instance
    //
    public static SettingsListFragment newInstance() {
        return new SettingsListFragment();
    }


    // Private Properties
    //
    private View rootView;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_settings, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getResources().getString(R.string.title_frag_settings));

    }


    // Events
    //
    @OnClick(R.id.profileRow)
    public void profileCLicked() {
        getNavigationManager().startSettingsProfile();
    }

    @OnClick(R.id.displayRow)
    public void displayClicked() {
        getNavigationManager().startSettingsDisplay();
    }

    @OnClick(R.id.todayRow)
    public void todayClicked() {
        getNavigationManager().startSettingsHome();
    }
}
