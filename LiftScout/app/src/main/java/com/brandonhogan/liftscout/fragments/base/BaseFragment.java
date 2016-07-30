package com.brandonhogan.liftscout.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.foundation.navigation.NavigationManager;

import butterknife.ButterKnife;
import io.realm.Realm;

public class BaseFragment extends Fragment {

    // Private Properties
    //
    private final String classTag = this.getClass().getSimpleName();
    private Realm realm;
    private final Object realmLock = new Object();


    // Public Functions
    //
    public String getClassTag() {
        return classTag;
    }

    public void setTitle(String title) {
        ((MainActivity)getActivity()).setTitle(title);
    }

    public NavigationManager getNavigationManager() {
        return ((MainActivity)getActivity()).getNavigationManager();
    }

    public Realm getRealm() {
        if (realm == null || realm.isClosed()) {
            synchronized (realmLock) {
                if (realm == null || realm.isClosed()) {
                    realm = Realm.getDefaultInstance();
                }
            }
        }
        return realm;
    }


    // Private Functions
    //
    private void closeRealm() {
        if (realm != null && !realm.isClosed()) {
            synchronized (realmLock) {
                if (realm != null && !realm.isClosed()) {
                    try {
                        realm.close();
                    }
                    catch (Exception ex) {
                        Log.e(getClassTag(), "closeRealm() : Failed to close realm");
                    }
                    finally {
                        realm = null;
                    }
                }
            }
        }
    }


    // Overrides
    //
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public Animation onCreateAnimation(final int transit, boolean enter, int nextAnim) {

        if (nextAnim == 0) {
            return null;
        }
        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(getClassTag(), "Fragment Animation started.");
                getNavigationManager().setInTransition(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.v(getClassTag(), "Fragment Animation repeating.");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(getClassTag(), "Fragment Animation ended.");
                getNavigationManager().setInTransition(false);
            }
        });

        return anim;
    }
}
