package com.brandonhogan.liftscout.repository.impl;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.Record;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.DatabaseRealm;
import com.brandonhogan.liftscout.repository.RecordsRepo;
import com.brandonhogan.liftscout.repository.SetRepo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.RealmResults;

/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

public class RecordRepoImpl implements RecordsRepo {

    private static final String TAG = "RecordRepoImpl";

    @Inject
    DatabaseRealm databaseRealm;

    public RecordRepoImpl() {
        Injector.getAppComponent().inject(this);
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
}
