package com.brandonhogan.liftscout.views.base;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.core.managers.NavigationManager;
import com.brandonhogan.liftscout.events.SearchViewEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.realm.Realm;

public class BaseFragment extends Fragment {


    // Private Properties
    //
    private final String TAG = this.getClass().getSimpleName();
    private Realm realm;
    private final Object realmLock = new Object();
    private float oldTranslationZ;


    // Protected Properties
    //
    protected Bundle saveState;
    protected static final String STATE_BUNDLE = "todayState";


    // Protected Functions
    //
    // This should be overridden on any fragment that wishes to save state
    protected Bundle saveState() {
        return null;
    }


    // Public Functions
    //
    public String getTAG() {
        return TAG;
    }

    public void setTitle(String title) {
        ((MainActivity)getActivity()).setTitle(title);
    }

    public NavigationManager getNavigationManager() {
        return ((MainActivity)getActivity()).getNavigationManager();
    }


    // Private Functions
    //

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    // Overrides
    //
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null && saveState == null) {
            saveState = savedInstanceState.getBundle(STATE_BUNDLE);
        }

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle screenView = new Bundle();

        screenView.putString(
                FirebaseAnalytics.Param.ITEM_NAME,
                TAG
        );

        mFirebaseAnalytics.logEvent(
                FirebaseAnalytics.Event.VIEW_ITEM,
                screenView
        );
    }


    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
        hideKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveState = saveState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(STATE_BUNDLE, saveState());
    }

    @Override
    public Animator onCreateAnimator(int transit, final boolean enter, int nextAnim) {

        if (nextAnim != 0) {

            Animator animator = AnimatorInflater.loadAnimator(getActivity(), nextAnim);
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animator) {
                    Log.v(getTAG(), "Fragment Animation started.");
                    getNavigationManager().setInTransition(true);

                    if (getView() != null && enter) {
                        oldTranslationZ = ViewCompat.getTranslationZ(getView());
                        ViewCompat.setTranslationZ(getView(), 300.f);
                    }

                    Log.d(TAG, "onAnimationStart: " + ViewCompat.getTranslationZ(getView()));
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    Log.v(getTAG(), "Fragment Animation ended.");
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


    // Bus Subscriptions
    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchEvent(SearchViewEvent event) {

        if(!event.isActive())
            searchViewOnClose();
        else if (event.isActive() && event.getNewText() != null)
            searchViewOnQueryTextChange(event.getNewText());
        else
            searchViewOnOpen();
    }

    public void searchViewOnQueryTextChange(String newText) {
        // Override in child fragment if you want to be notified of changes
    }

    public void searchViewOnClose() {
        // Override in child fragment if you want to be notified of changes
    }

    public void searchViewOnOpen() {
        // Override in child fragment if you want to be notified of changes
    }
}
