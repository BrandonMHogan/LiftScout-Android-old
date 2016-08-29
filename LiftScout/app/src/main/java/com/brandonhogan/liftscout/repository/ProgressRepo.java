package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Progress;

public interface ProgressRepo {
    Progress getProgress(long progressId);
    void setProgress(Progress progress);

}
