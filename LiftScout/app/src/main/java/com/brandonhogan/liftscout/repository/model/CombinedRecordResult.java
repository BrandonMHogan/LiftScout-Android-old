package com.brandonhogan.liftscout.repository.model;

import java.util.List;

/**
 * Created by Brandon on 3/14/2017.
 * Description :
 */

public class CombinedRecordResult {
    private List<Record> records;
    private Rep rep;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Rep getRep() {
        return rep;
    }

    public void setRep(Rep rep) {
        this.rep = rep;
    }
}
