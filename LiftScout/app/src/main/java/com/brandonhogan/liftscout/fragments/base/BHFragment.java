package com.brandonhogan.liftscout.fragments.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public abstract class BHFragment extends Fragment {

    // Private Properties
    private final String bhTAG = this.getClass().getSimpleName();
    private BHFragmentListener callback;
    private String title;
    // Constructors
    public BHFragment(String title) {
        this.title = title;
    }
    public BHFragment() { }

    // Public Functions
    public String getBhTAG() {
        return bhTAG;
    }

    public BHFragmentListener getCallback() {
        return callback;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (title != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        try {
            activity =(Activity) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("BHFragment passed none activity based context");
        }
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (BHFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BHFragmentListener");
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
                Log.d(getBhTAG(), "Fragment Animation started.");
                callback.fragmentTransitionStarted();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(getBhTAG(), "Fragment Animation repeating.");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(getBhTAG(), "Fragment Animation ended.");
                callback.fragmentTransitionEnded();
            }
        });

        return anim;
    }
}
