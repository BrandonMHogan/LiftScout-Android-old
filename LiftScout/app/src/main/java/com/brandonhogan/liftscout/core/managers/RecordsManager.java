package com.brandonhogan.liftscout.core.managers;

import com.brandonhogan.liftscout.core.model.Record;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.RecordsRepo;
import com.brandonhogan.liftscout.repository.SetRepo;
import com.brandonhogan.liftscout.repository.model.CombinedRecordResult;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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

    // Gets the rep, then deletes the record, then updates the records old rep range
    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> repDeleted(final int repId, final int exerciseId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                getRep(repId)
                        .subscribe(new Consumer<Rep>() {
                            @Override
                            public void accept(@NonNull Rep rep) throws Exception {
                                final int repRange = rep.getCount();

                                recordsRepo.deleteRecord(repId)
                                        .subscribe(new Consumer<Boolean>() {
                                            @Override
                                            public void accept(@NonNull Boolean aBoolean) throws Exception {

                                                updateRepRange(exerciseId, repRange)
                                                        .subscribe(new Consumer<Boolean>() {
                                                            @Override
                                                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                                                e.onNext(aBoolean);
                                                                e.onComplete();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> repUpdated(final int repId, final int exerciseId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {

            // (1) Get the rep first
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                getRep(repId)
                        .subscribe(new Consumer<Rep>() {
                            @Override
                            public void accept(final @NonNull Rep rep) throws Exception {

                                // (2) Find Rep in records
                                recordsRepo.getRecord(repId)
                                        .subscribe(new Consumer<Record>() {
                                            @Override
                                            public void accept(final @NonNull Record record) throws Exception {

                                                // (3) Store the old rep range, just incase it has changed
                                                final int oldRepRange = record.getRepRange();

                                                // (4) update our record to match the changes to the rep
                                                recordsRepo.updateRecord(record, rep.getWeight(), rep.getCount(), record.isRecord())
                                                        .subscribe(new Consumer<Boolean>() {
                                                            @Override
                                                            public void accept(@NonNull Boolean aBoolean) throws Exception {

                                                                // (5) Now that the record is updated, make sure the rep ranges are still correct.

                                                                if (rep.getCount() != oldRepRange) {
                                                                    // Since the range was changed, update the old range so it also fixes its new isRecord
                                                                    updateRepRange(exerciseId, oldRepRange)
                                                                            .subscribe(new Consumer<Boolean>() {
                                                                                @Override
                                                                                public void accept(@NonNull Boolean aBoolean) throws Exception {

                                                                                }
                                                                            });
                                                                }

                                                                // (6) Update the rep Range, which will clear all isRecord, and set it to the highest
                                                                updateRepRange(exerciseId, rep.getCount())
                                                                        .subscribe(new Consumer<Boolean>() {
                                                                            @Override
                                                                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                                                                // If we reached this point, then the record should be saved,
                                                                                // record ranges should be corrected.
                                                                                recordsRepo.getRecord(repId)
                                                                                        .subscribe(new Consumer<Record>() {
                                                                                            @Override
                                                                                            public void accept(@NonNull Record record) throws Exception {
                                                                                                e.onNext(record.isRecord());
                                                                                                e.onComplete();
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });

                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> repCreated(final int repId, final int exerciseId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                getRep(repId)
                        .subscribe(new Consumer<Rep>() {
                            @Override
                            public void accept(final @NonNull Rep rep) throws Exception {

                                // Don't set Records for entries with no weight.
                                if (rep.getWeight() == 0) {
                                    e.onNext(true);
                                    e.onComplete();
                                    return;
                                }

                                recordsRepo.createRecord(exerciseId, rep, false)
                                        .subscribe(new Consumer<Record>() {
                                            @Override
                                            public void accept(@NonNull Record record) throws Exception {

                                                updateRepRange(exerciseId, rep.getCount())
                                                        .subscribe(new Consumer<Boolean>() {
                                                            @Override
                                                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                                                e.onNext(aBoolean);
                                                                e.onComplete();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> updateRepRange(final int exerciseId, final int repRange) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                recordsRepo.updateRecordRepRange(exerciseId, repRange)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                e.onNext(aBoolean);
                                e.onComplete();
                            }
                        });
            }
        });
    }

    public boolean isRecord(int repId) {
        return recordsRepo.isRecord(repId);
    }

    // Set deleted functions

    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> setDeleted(final int setId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                setRepo.getSet(setId)
                        .subscribe(new Consumer<Set>() {
                            @Override
                            public void accept(@NonNull Set set) throws Exception {

                                for (Rep rep : set.getReps()) {
                                    repDeleted(rep.getId(), set.getExercise().getId())
                                            .subscribe(new Consumer<Boolean>() {
                                                @Override
                                                public void accept(@NonNull Boolean aBoolean) throws Exception {
                                                }
                                            });
                                }

                                e.onNext(true);
                                e.onComplete();
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
