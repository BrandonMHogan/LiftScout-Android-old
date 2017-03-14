package com.brandonhogan.liftscout.core.managers;

import android.util.Log;

import com.brandonhogan.liftscout.core.model.Record;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.repository.RecordsRepo;
import com.brandonhogan.liftscout.repository.SetRepo;
import com.brandonhogan.liftscout.repository.impl.RecordRepoImpl;

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


    public Observable<Boolean> repCreated(final int repId, final int exerciseId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                try {
                    setRepo.getRep(repId)
                            .subscribe(new Consumer<Rep>() {
                                @Override
                                public void accept(@NonNull final Rep rep) throws Exception {

                                    recordsRepo.getRecords(exerciseId, rep.getCount())
                                            .subscribe(new Consumer<List<Record>>() {
                                                @Override
                                                public void accept(@NonNull List<Record> records) throws Exception {
                                                    try {
                                                        boolean isRecord = true;

                                                        if (records != null && !records.isEmpty()) {
                                                            Record topRecord = records.get(records.size() -1);

                                                            if (topRecord.getRepWeight() > rep.getWeight())
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

                                                        recordsRepo.createRecord(exerciseId, rep, isRecord)
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
                catch (Exception ex) {
                    Log.e(TAG, "getRep subscribe: ",ex);
                    e.onError(ex);
                }
            }
        });
    }


    public Observable<Boolean> updatedSetCheck(final int setId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {

                RealmResults<Set> set = setRepo.getSets();

                if (set == null || !set.isValid()) {
                    e.onNext(false);
                    e.onComplete();
                }
                else
                {
                    e.onNext(true);
                    e.onComplete();
                }


//                recordsRepo.createRecord(new Record())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<Record>() {
//                            @Override
//                            public void accept(@io.reactivex.annotations.NonNull Record record) throws Exception {
//                                Log.d(TAG, "accept: " + record.getRepRange());
//                            }
//                        });


            }
        });
    }

}
