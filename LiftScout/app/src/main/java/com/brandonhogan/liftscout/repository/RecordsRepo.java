package com.brandonhogan.liftscout.repository;

import com.brandonhogan.liftscout.core.model.Record;
import com.brandonhogan.liftscout.core.model.Rep;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

public interface RecordsRepo {
    Observable<Boolean> deleteRecord(int repId);
    Observable<Record> createRecord(int exerciseId, Rep rep, boolean isRecord);
    Observable<Record> getRecord(int repId);
    Observable<List<Record>> getRecords(int exerciseId, int repRange);
    Observable<Boolean> updateRecord(Record record, double weight, int range, boolean isRecord);
    Observable<Boolean> updateRecordRepRange(int exerciseId, int repRange);
    Observable<Boolean> updateRecord(Record record, boolean isRecord);
    boolean isRecord(int repId);
}
