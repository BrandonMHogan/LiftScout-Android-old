package com.brandonhogan.liftscout.core.managers;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Record;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.RecordsRepo;
import com.brandonhogan.liftscout.repository.SetRepo;
import com.brandonhogan.liftscout.repository.impl.RecordRepoImpl;
import com.brandonhogan.liftscout.repository.model.CombinedRecordResult;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.RealmResults;

/**
 * Created by Brandon on 3/14/2017.
 * Description :
 */

public class RecordsManager {
    private static final String TAG = "RecordsManager";


    @Inject
    RecordsRepo recordsRepo;

    @Inject
    SetRepo setRepo;


    // Constructor
    //
    public RecordsManager() {
        Injector.getRepoComponent().inject(this);
    }


    public Observable<Boolean> repUpdated(final int repId, final int exerciseId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                getRep(repId)
                        .subscribe(new Consumer<Rep>() {
                            @Override
                            public void accept(@NonNull Rep rep) throws Exception {
                                try {
                                    e.onNext(true);
                                    e.onComplete();
                                }
                                catch (Exception ex) {
                                    e.onError(ex);
                                }
                            }
                        });
            }
        });
    }


    public Observable<Boolean> repCreated(final int repId, final int exerciseId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                getRecordsForExerciseAndRange(repId, exerciseId)
                        .subscribe(new Consumer<CombinedRecordResult>() {
                            @Override
                            public void accept(@NonNull CombinedRecordResult result) throws Exception {
                                try {
                                    boolean isRecord = true;

                                    if (result.getRecords() != null && !result.getRecords().isEmpty()) {
                                        Record topRecord = result.getRecords().get(result.getRecords().size() -1);

                                        if (topRecord.getRepWeight() >= result.getRep().getWeight())
                                            isRecord = false;
                                        else {
                                            // topRecord is no longer the top record. Update it.
                                            recordsRepo.updateRecord(topRecord, topRecord.getRepWeight(), topRecord.getRepRange(), false)
                                                    .subscribe(new Consumer<Boolean>() {
                                                        @Override
                                                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                                                            try {
                                                                Log.d(TAG, "accept: updated old record successfully");
                                                            }
                                                            catch (Exception ex) {
                                                                Log.e(TAG, "accept: ", ex);
                                                                e.onError(ex);
                                                            }
                                                        }
                                                    });
                                        }

                                    }

                                    recordsRepo.createRecord(exerciseId, result.getRep(), isRecord)
                                            .subscribe(new Consumer<Record>() {
                                                @Override
                                                public void accept(@NonNull Record record) throws Exception {
                                                    try {
                                                        e.onNext(record.isRecord());
                                                        e.onComplete();
                                                    }
                                                    catch (Exception ex) {
                                                        Log.e(TAG, "accept: ", ex);
                                                        e.onError(ex);
                                                    }
                                                }
                                            });
                                }
                                catch (Exception ex) {
                                    Log.e(TAG, "getRecords accept: ", ex);
                                    e.onError(ex);
                                }
                            }
                        });
            }
        });
    }




    // Private helpers

    private Observable<Rep> getRep(final int repId) {
        return Observable.create(new ObservableOnSubscribe<Rep>() {
            @Override
            public void subscribe(final ObservableEmitter<Rep> e) throws Exception {
                try {
                    setRepo.getRep(repId)
                            .subscribe(new Consumer<Rep>() {
                                @Override
                                public void accept(@NonNull final Rep rep) throws Exception {
                                    try {
                                        e.onNext(rep);
                                        e.onComplete();
                                    }
                                    catch (Exception ex) {
                                        e.onError(ex);
                                    }
                                }
                            });
                }
                catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });
    }

    private Observable<CombinedRecordResult> getRecordsForExerciseAndRange(final int repId, final int exerciseId) {
        return Observable.create(new ObservableOnSubscribe<CombinedRecordResult>() {
            @Override
            public void subscribe(final ObservableEmitter<CombinedRecordResult> e) throws Exception {
                try {
                    getRep(repId)
                            .subscribe(new Consumer<Rep>() {
                                @Override
                                public void accept(final @NonNull Rep rep) throws Exception {
                                    recordsRepo.getRecords(exerciseId, rep.getCount())
                                            .subscribe(new Consumer<List<Record>>() {
                                                @Override
                                                public void accept(@NonNull List<Record> records) throws Exception {
                                                    try {
                                                        CombinedRecordResult recordResult = new CombinedRecordResult();
                                                        recordResult.setRep(rep);
                                                        recordResult.setRecords(records);

                                                        e.onNext(recordResult);
                                                        e.onComplete();
                                                    } catch (Exception ex) {
                                                        e.onError(ex);
                                                    }
                                                }
                                            });
                                }
                            });
                }
                catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });
    }
}
