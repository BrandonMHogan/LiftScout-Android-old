package com.brandonhogan.liftscout.fragments.home;

import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;

public class TodayListModel {

    private long progressId;
    private Set set;
    private double volume;

    public TodayListModel(long progressId, Set set) {
        this.progressId = progressId;
        this.set = set;

        for (Rep rep : set.getReps()) {
            this.volume += rep.getWeight();
        }
    }

    public long getProgressId() {
        return progressId;
    }

    public void setProgressId(long progressId) {
        this.progressId = progressId;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
