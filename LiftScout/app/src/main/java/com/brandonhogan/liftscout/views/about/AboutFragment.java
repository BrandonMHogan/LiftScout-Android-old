package com.brandonhogan.liftscout.views.about;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.BuildConfig;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.Bind;

public class AboutFragment extends BaseFragment {

    // Instance
    //
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }


    // Private Properties
    //
    private View rootView;


    // Bindings
    //
    @Bind(R.id.version_number)
    TextView versionNumber;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_about, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getResources().getString(R.string.title_frag_about));


        versionNumber.setText(BuildConfig.VERSION_NAME);
    }
}
