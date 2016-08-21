package com.brandonhogan.liftscout.core.managers;

import android.util.Log;

import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.core.model.Progress;
import com.brandonhogan.liftscout.core.model.Set;

import java.util.Date;

import io.realm.RealmList;

public class ProgressManager {

    private static final String TAG = "ProgressManager";

    private MainActivity mActivity;
    private Progress mTodayProgress;
    private Set mUpdatedSet;

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param activity
     */
    public void init (MainActivity activity) {
        mActivity = activity;
    }

    public Progress getTodayProgress() {
        return mTodayProgress;
    }

    public void setTodayProgress(Progress mTodayProgress) {
        this.mTodayProgress = mTodayProgress;
    }

    public void setTodayProgress(Date date) {
        mTodayProgress = mActivity.getRealm().where(Progress.class)
                .equalTo(Progress.DATE, date).findFirst();


        if (mTodayProgress == null) {
            mTodayProgress = new Progress();
            mTodayProgress.setDate(date);
            mTodayProgress.setSets(new RealmList<Set>());

            mActivity.getRealm().beginTransaction();
            mActivity.getRealm().copyToRealmOrUpdate(mTodayProgress);
            mActivity.getRealm().commitTransaction();
        }
    }

    public boolean isSetUpdated() {
        return mUpdatedSet != null;
    }

    public Set getUpdatedSet() {
        return mUpdatedSet;
    }

    public void updateSet(Set set) {
        mUpdatedSet = set;
    }

    public void clearUpdatedSet() {
        mUpdatedSet = null;
    }
}
