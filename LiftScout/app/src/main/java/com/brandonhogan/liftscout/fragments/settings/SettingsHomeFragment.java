package com.brandonhogan.liftscout.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.constants.TodayTransforms;
import com.brandonhogan.liftscout.foundation.model.UserSetting;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.Bind;

public class SettingsHomeFragment extends BaseFragment {



    // Instance
    //
    public static SettingsHomeFragment newInstance() {
        return new SettingsHomeFragment();
    }


    // Private Properties
    //
    private View rootView;
    private ArrayList<String> transformsAdapter;
    private UserSetting _todayTransformUserSetting;


    // Bindings
    //
    @Bind(R.id.todayTransformSpinner)
    MaterialSpinner transformSpinner;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_settings_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getResources().getString(R.string.title_frag_settings_home));

        transformsAdapter = new ArrayList<>();
        transformsAdapter.add(TodayTransforms.ACCORDION);
        transformsAdapter.add(TodayTransforms.DEPTH_PAGE);
        transformsAdapter.add(TodayTransforms.FOREGROUND_TO_BACKGROUND);
        transformsAdapter.add(TodayTransforms.ROTATE_DOWN);
        transformsAdapter.add(TodayTransforms.ROTATE_UP);
        transformsAdapter.add(TodayTransforms.SCALE_IN_OUT);
        transformsAdapter.add(TodayTransforms.STACK);
        transformsAdapter.add(TodayTransforms.ZOOM_IN);
        transformsAdapter.add(TodayTransforms.ZOOM_OUT);
        transformsAdapter.add(TodayTransforms.ZOOM_OUT_SLIDE);


        transformSpinner.setItems(transformsAdapter);
        transformSpinner.setSelectedIndex(transformsAdapter.indexOf(getTodayTransform().getValue()));
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
        menu.findItem(R.id.action_save).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSettings();
                getNavigationManager().navigateBack(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSettings() {
        getRealm().beginTransaction();

        // Today Transform
        getTodayTransform().setValue(transformsAdapter.get(transformSpinner.getSelectedIndex()));
        getRealm().copyToRealmOrUpdate(getTodayTransform());

        getRealm().commitTransaction();
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
        }
        return _todayTransformUserSetting;
    }
}
