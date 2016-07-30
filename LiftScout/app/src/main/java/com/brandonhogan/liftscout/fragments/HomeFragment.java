package com.brandonhogan.liftscout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;

import butterknife.Bind;

public class HomeFragment extends BaseFragment {

    private User user;
    private View rootView;

    @Bind(R.id.welcome_message)
    TextView welcomeMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_home, null);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Home Sweet Home");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadUserData();
    }

    private void loadUserData() {
        user = getRealm().where(User.class).findFirst();

        if(user.isFirstLoad()) {
            getRealm().beginTransaction();
            user.setFirstLoad(false);
            getRealm().commitTransaction();

            welcomeMessage.setText(String.format(getContext().getString(R.string.frag_home_first_load_message), user.getName()));
        }
        else {
            welcomeMessage.setText(String.format(getContext().getString(R.string.frag_home_welcome_back_message),user.getName()));
        }
    }
}
