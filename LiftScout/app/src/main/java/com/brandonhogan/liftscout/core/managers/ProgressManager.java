package com.brandonhogan.liftscout.core.managers;

import android.util.Log;

import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.core.model.Progress;

import java.util.Date;

public class ProgressManager {

    private static final String TAG = "ProgressManager";

    private MainActivity mActivity;
    private Progress mTodayProgress;
    private Date todayDate;


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
        if (mTodayProgress == null || !mTodayProgress.isValid()) {
            if (todayDate == null) {
                Log.e(TAG, "No date or Progress was supplied to the manager. Returning null");
                return null;
            }

            mTodayProgress = mActivity.getRealm().where(Progress.class)
                    .equalTo(Progress.DATE, todayDate).findFirst();
        }

        return mTodayProgress;
    }

    public void setTodayProgress(Progress mTodayProgress) {
        this.mTodayProgress = mTodayProgress;
        todayDate = mTodayProgress.getDate();
    }

    public void setTodayProgress(Date date) {
        mTodayProgress = mActivity.getRealm().where(Progress.class)
                .equalTo(Progress.DATE, date).findFirst();


        if (mTodayProgress == null) {
            mTodayProgress = new Progress();
            mTodayProgress.setDate(date);

            mActivity.getRealm().beginTransaction();
            mActivity.getRealm().copyToRealmOrUpdate(mTodayProgress);
            mActivity.getRealm().commitTransaction();
        }

        todayDate = mTodayProgress.getDate();
    }
}
