package com.brandonhogan.liftscout.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

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


        transformSpinner.setItems("Scale In Out", "Depth Page", "Accordion", "Flip Horizontal", "Flip Vertical");
        transformSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

    }
}
