package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.RecordsRepo;
import com.brandonhogan.liftscout.repository.model.Record;
import com.brandonhogan.liftscout.repository.model.Rep;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

public class RecordsRepoImpl implements RecordsRepo {

    private static final String TAG = "RecordsRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public RecordsRepoImpl() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Observable<Boolean> deleteRecord(final int repId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {
                    databaseRealm.getRealmInstance().beginTransaction();
                    databaseRealm.getRealmInstance().where(Record.class)
                            .equalTo(Record.REP_ID, repId)
                            .findAll().deleteAllFromRealm();
                    databaseRealm.getRealmInstance().commitTransaction();

                    e.onNext(true);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: deleteRecord()", ex);
                    databaseRealm.getRealmInstance().cancelTransaction();
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<Record> createRecord(final int exerciseId, final Rep rep, final boolean isRecord) {
        return Observable.create(new ObservableOnSubscribe<Record>() {
            @Override
            public void subscribe(ObservableEmitter<Record> e) throws Exception {

                try {
                    Record newRecord = new Record();

                    newRecord.setRepId(rep.getId());
                    newRecord.setRepRange(rep.getCount());
                    newRecord.setRepWeight(rep.getWeight());
                    newRecord.setExerciseId(exerciseId);
                    newRecord.setRecord(isRecord);
                    newRecord.setDate(new Date());

                    databaseRealm.getRealmInstance().beginTransaction();
                    databaseRealm.getRealmInstance().copyToRealmOrUpdate(newRecord);
                    databaseRealm.getRealmInstance().commitTransaction();

                    e.onNext(newRecord);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: ", ex);
                    databaseRealm.getRealmInstance().cancelTransaction();
                    e.onError(ex);
                }

//                databaseRealm.getRealmInstance().beginTransaction();
//                databaseRealm.copyToRealmOrUpdate(new Record(set.getReps().first().getCount(), set.getExercise().getId(), set.getReps().first()));
//                databaseRealm.getRealmInstance().commitTransaction();


            }
        });
    }


    @Override
    public Observable<Record> getRecord(final int repId) {
        return Observable.create(new ObservableOnSubscribe<Record>() {
            @Override
            public void subscribe(ObservableEmitter<Record> e) throws Exception {
                try {
                    Record record = databaseRealm.getRealmInstance()
                            .where(Record.class)
                            .equalTo(Record.REP_ID, repId)
                            .findFirst();

                    e.onNext(record);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.d(TAG, "subscribe: getRecord() ", ex);
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<List<Record>> getRecords(final int exerciseId, final int repRange) {
        return Observable.create(new ObservableOnSubscribe<List<Record>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Record>> e) throws Exception {
                try {

                    List<Record> models = databaseRealm.getRealmInstance()
                            .where(Record.class)
                            .equalTo(Record.EXERCISE_ID, exerciseId)
                            .equalTo(Record.REP_RANGE, repRange)
                            .findAllSorted(Record.REP_WEIGHT);

                    e.onNext(models);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: ", ex);
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> updateRecord(final Record record, final double weight, final int range, final boolean isRecord) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {

                    databaseRealm.getRealmInstance().beginTransaction();
                    record.setRecord(isRecord);
                    record.setRepWeight(weight);
                    record.setRepRange(range);

                    databaseRealm.getRealmInstance().copyToRealmOrUpdate(record);
                    databaseRealm.getRealmInstance().commitTransaction();

                    e.onNext(true);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: ", ex);
                    databaseRealm.getRealmInstance().cancelTransaction();
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> updateRecord(final Record record, final boolean isRecord) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {

                    databaseRealm.getRealmInstance().beginTransaction();
                    record.setRecord(isRecord);
                    databaseRealm.getRealmInstance().copyToRealmOrUpdate(record);
                    databaseRealm.getRealmInstance().commitTransaction();

                    e.onNext(true);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: ", ex);
                    databaseRealm.getRealmInstance().cancelTransaction();
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> updateRecordRepRange(final int exerciseId, final int repRange) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {
                    databaseRealm.getRealmInstance().beginTransaction();

                    RealmResults<Record> records = databaseRealm.getRealmInstance()
                            .where(Record.class)
                            .equalTo(Record.EXERCISE_ID, exerciseId)
                            .equalTo(Record.REP_RANGE, repRange)
                            .equalTo(Record.IS_RECORD, true)
                            .findAll();

                    if (records == null) {
                        databaseRealm.getRealmInstance().cancelTransaction();
                        e.onNext(false);
                        e.onComplete();
                        return;
                    }

                    for (Record record : records) {
                        record.setRecord(false);
                    }

                    records = databaseRealm.getRealmInstance()
                            .where(Record.class)
                            .equalTo(Record.EXERCISE_ID, exerciseId)
                            .equalTo(Record.REP_RANGE, repRange)
                            .findAll();

                    if (records == null || records.isEmpty()) {
                        databaseRealm.getRealmInstance().cancelTransaction();
                        e.onNext(false);
                        e.onComplete();
                        return;
                    }

                    String[] fieldNames = {Record.REP_WEIGHT, Record.DATE};
                    Sort sort[] = {Sort.DESCENDING, Sort.ASCENDING};

                    Record firstRecord = records.sort(fieldNames, sort).first();
                    firstRecord.setRecord(true);

                    databaseRealm.getRealmInstance().commitTransaction();

                    e.onNext(true);
                    e.onComplete();
                }
                catch (Exception ex) {
                    Log.e(TAG, "subscribe: updateRecordRepRange", ex);
                    databaseRealm.getRealmInstance().cancelTransaction();
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public boolean isRecord(int repId) {
        try {
            Record record = databaseRealm.getRealmInstance().where(Record.class)
                    .equalTo(Record.REP_ID, repId).findFirst();

            return (record != null && record.isRecord());
        }
        catch (Exception ex) {
            Log.e(TAG, "isRecord: ", ex);
            return false;
        }
    }
}
