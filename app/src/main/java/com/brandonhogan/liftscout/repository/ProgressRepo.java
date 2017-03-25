package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.repository.model.Progress;

import java.util.Date;

import io.realm.RealmResults;

public interface ProgressRepo {
    Progress getProgress(long progressId);
    Progress getProgress(Date date);
    RealmResults<Progress> getAllProgressForMonth(int month, int year);
    Progress setProgress(Progress progress);
}
