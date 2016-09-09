package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Progress;

import java.util.Date;

public interface ProgressRepo {
    Progress getProgress(long progressId);
    Progress getProgress(Date date);
    Progress setProgress(Progress progress);

}
