package com.brandonhogan.liftscout.fragments.base;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

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
    private float oldTranslationZ;

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
    public Animator onCreateAnimator(int transit, final boolean enter, int nextAnim) {

        if (nextAnim != 0) {

            Animator animator = AnimatorInflater.loadAnimator(getActivity(), nextAnim);
            animator.addListener(new Animator.AnimatorListener() {



                @Override
                public void onAnimationStart(Animator animator) {
                    Log.v(getClassTag(), "Fragment Animation started.");
                    getNavigationManager().setInTransition(true);

                    if (getView() != null && enter) {
                        oldTranslationZ = ViewCompat.getTranslationZ(getView());
                        ViewCompat.setTranslationZ(getView(), 300.f);
                    }

                    Log.d(classTag, "onAnimationStart: " + ViewCompat.getTranslationZ(getView()));
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    Log.v(getClassTag(), "Fragment Animation ended.");
                    getNavigationManager().setInTransition(false);

                    if (getView() != null && enter) {
                        ViewCompat.setTranslationZ(getView(), oldTranslationZ);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

        return super.onCreateAnimator(transit, enter, nextAnim);
    }
}
