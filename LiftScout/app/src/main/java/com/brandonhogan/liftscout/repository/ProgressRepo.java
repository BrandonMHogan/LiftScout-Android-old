package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Progress;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmResults;

public interface ProgressRepo {
    Progress getProgress(long progressId);
    Progress getProgress(Date date);
    Progress setProgress(Progress progress);
}
