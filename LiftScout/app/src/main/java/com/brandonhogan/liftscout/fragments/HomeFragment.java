package com.brandonhogan.liftscout.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.AppController;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.fragments.base.BHDetailFragment;
import com.brandonhogan.liftscout.fragments.base.BHFragment;
import com.brandonhogan.liftscout.foundation.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HomeFragment extends BHDetailFragment {

    private User user;

    @Bind(R.id.welcome_message)
    TextView welcomeMessage;

    public HomeFragment() {
        super(AppController.getInstance().getString(R.string.nav_home));
    }

    @Override
    public ApplicationArea applicationArea() {
        return ApplicationArea.HOME;
    }

    @Override
    public BHFragment parentFragment() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);

        //FAB button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadUserData();

        return view;
    }

    private void loadUserData() {
        Realm realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        if(user.isFirstLoad()) {
            realm.beginTransaction();
            user.setFirstLoad(false);
            realm.commitTransaction();
            welcomeMessage.setText(String.format(getContext().getString(R.string.frag_home_first_load_message), user.getName()));
        }
        else {
            welcomeMessage.setText(String.format(getContext().getString(R.string.frag_home_welcome_back_message),user.getName()));
        }

        realm.close();
    }
}
