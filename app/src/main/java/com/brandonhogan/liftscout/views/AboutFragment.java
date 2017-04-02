package com.brandonhogan.liftscout.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.BuildConfig;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.utils.IntentUtil;
import com.brandonhogan.liftscout.views.base.BaseFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class AboutFragment extends BaseFragment {

    // Instance
    //
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }


    // Bindings
    //
    @Bind(R.id.version_number)
    TextView versionNumber;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_about, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getResources().getString(R.string.title_frag_about));
        versionNumber.setText(BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.contact_container)
    void onFeedbackClicked() {
        IntentUtil.sendEmailIntent(getActivity(), getString(R.string.app_feedback_subject));
    }

    @OnClick(R.id.developer_container)
    void onDeveloperClicked() {
        IntentUtil.sendURLIntent(getActivity(), getString(R.string.app_developer_website));
    }

    @OnClick(R.id.designer_container)
    void onDesignerClicked() {
        IntentUtil.sendURLIntent(getActivity(), getString(R.string.app_designer_website));
    }
}
