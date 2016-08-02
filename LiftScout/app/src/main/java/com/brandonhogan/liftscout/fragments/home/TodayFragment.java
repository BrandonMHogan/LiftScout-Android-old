package com.brandonhogan.liftscout.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;

public class TodayFragment extends BaseFragment {


    // Instance
    //
    public static TodayFragment newInstance(Date date)
    {
        TodayFragment frag = new TodayFragment();
        Bundle bundle = new Bundle();

        bundle.putLong(DATE_BUNDLE, date.getTime());
        frag.setArguments(bundle);

        return frag;
    }


    // Static Properties
    //
    private static final String DATE_BUNDLE = "dateBundle";


    // Private Properties
    //
    private View rootView;
    private long date;


    // Binds
    //
    @Bind(R.id.date)
    TextView dateView;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_today, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        date = getArguments().getLong(DATE_BUNDLE);

        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(date));

        dateView.setText(dateString);

    }
}