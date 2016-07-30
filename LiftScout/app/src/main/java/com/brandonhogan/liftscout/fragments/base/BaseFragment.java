package com.brandonhogan.liftscout.fragments.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.brandonhogan.liftscout.activities.MainActivity;

import butterknife.ButterKnife;
import io.realm.Realm;

public class BaseFragment extends Fragment {

    // Private Properties
    //
    private final String classTag = this.getClass().getSimpleName();
    private FragmentListener callback;
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
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        // Makes sure the context is activity based
        try {
            activity = (Activity)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("BaseFragment passed none activity based " +
                    "context for " + getClassTag());
        }

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = ((FragmentListener) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(getClassTag()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        if (nextAnim == 0) {
            return null;
        }
        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(getClassTag(), "Fragment Animation started.");
                callback.fragmentTransitionStarted();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.v(getClassTag(), "Fragment Animation repeating.");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(getClassTag(), "Fragment Animation ended.");
                callback.fragmentTransitionEnded();
            }
        });

        return anim;
    }
}
