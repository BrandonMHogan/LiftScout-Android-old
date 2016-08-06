package com.brandonhogan.liftscout.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.constants.TodayTransforms;
import com.brandonhogan.liftscout.foundation.controls.AnimCheckBox;
import com.brandonhogan.liftscout.foundation.controls.CircleCheckBox;
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

    private boolean weightChecked;
    private boolean photosChecked;
    private boolean routineChecked;


    // Bindings
    //
    @Bind(R.id.todayTransformSpinner)
    MaterialSpinner transformSpinner;

    @Bind(R.id.weight_checkbox)
    AnimCheckBox weightCheckbox;

    @Bind(R.id.photo_checkbox)
    AnimCheckBox photoCheckbox;

    @Bind(R.id.routine_checkbox)
    AnimCheckBox routineCheckbox;

    @Bind(R.id.weight_expanding_layout)
    LinearLayout weightSettingsLayout;

    @Bind(R.id.photo_expanding_layout)
    LinearLayout photoSettingsLayout;

    @Bind(R.id.routine_expanding_layout)
    LinearLayout routineSettingsLayout;

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

        setupTransform();
        setupCheckbox(weightCheckbox, weightSettingsLayout);
        setupCheckbox(photoCheckbox, photoSettingsLayout);
        setupCheckbox(routineCheckbox, routineSettingsLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private void setupTransform() {
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

    private void setupCheckbox(AnimCheckBox checkBox, final LinearLayout layout) {
        layout.setVisibility(View.GONE);
        checkBox.setChecked(false);

        checkBox.setOnCheckedChangeListener(new AnimCheckBox.OnCheckedChangeListener() {
            @Override
            public void onChange(boolean checked) {
                if (checked)
                    expand(layout);
                else
                    collapse(layout);
            }
        });
    }


    public void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
