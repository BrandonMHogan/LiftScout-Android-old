package com.brandonhogan.liftscout.core.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

public class Record extends RealmObject {

    @Ignore
    public static final String REP_ID = "repId";
    @Ignore
    public static final String REP_RANGE = "repRange";
    @Ignore
    public static final String REP_WEIGHT = "repWeight";
    @Ignore
    public static final String EXERCISE_ID = "exerciseId";
    @Ignore
    public static final String IS_RECORD = "isRecord";

    @PrimaryKey
    private int repId;

    private int repRange;
    private double repWeight;
    private int exerciseId;
    private boolean isRecord;

    public int getRepId() {
        return repId;
    }

    public void setRepId(int repId) {
        this.repId = repId;
    }

    public int getRepRange() {
        return repRange;
    }

    public void setRepRange(int repRange) {
        this.repRange = repRange;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public double getRepWeight() {
        return repWeight;
    }

    public void setRepWeight(double repWeight) {
        this.repWeight = repWeight;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }
}
