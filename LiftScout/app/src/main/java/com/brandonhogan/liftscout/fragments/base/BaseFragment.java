package com.brandonhogan.liftscout.fragments.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.activities.MainActivity;

import java.util.HashMap;
import java.util.Map;

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
}
