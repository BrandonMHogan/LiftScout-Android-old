package com.brandonhogan.liftscout.managers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Brandon on 3/31/2017.
 * Description :
 */

public class ExerciseListManager {

    private final PublishSubject<Boolean> bus = PublishSubject.create();

    public void categoryUpdated(final Boolean event) {
        bus.onNext(event);
    }

    public Observable<Boolean> updateExercises() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }


//    public Observable<Boolean> updateExercises() {
//        return Observable.create(new ObservableOnSubscribe<Boolean>() {
//            @Override
//            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
//                e.onNext(true);
//                e.onComplete();
//            }
//        });
//    }

}
